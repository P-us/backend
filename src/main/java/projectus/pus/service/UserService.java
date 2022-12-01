package projectus.pus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectus.pus.config.cache.CacheKey;
import projectus.pus.config.jwt.JwtExpirationEnums;
import projectus.pus.config.jwt.JwtTokenUtil;
import projectus.pus.dto.TokenDto;
import projectus.pus.dto.UserDto;
import projectus.pus.entity.LogoutAccessToken;
import projectus.pus.entity.RefreshToken;
import projectus.pus.entity.User;
import projectus.pus.repository.LogoutAccessTokenRedisRepository;
import projectus.pus.repository.RefreshTokenRedisRepository;
import projectus.pus.repository.UserRepository;

import java.util.NoSuchElementException;

import static projectus.pus.config.jwt.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public Long addUser(UserDto.Request requestDto) {
        validateDuplicateUser(requestDto);
        User user = userRepository.save(User.toEntity(requestDto, passwordEncoder));
        return user.getId();
    }

    public UserDto.Response findUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        return new UserDto.Response(user);
    }

    public void updateUser(Long userId, UserDto.Request requestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        validateDuplicateUser(requestDto);
        user.updateData(requestDto, passwordEncoder);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        userRepository.delete(user);
    }

    public TokenDto login(UserDto.Login requestDto){
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new NoSuchElementException("회원이 없습니다."));
        checkPassword(requestDto.getPassword(), user.getPassword());

        String email = user.getEmail();
        String accessToken = jwtTokenUtil.generateAccessToken(email);
        RefreshToken refreshToken = saveRefreshToken(email);

        return TokenDto.of(accessToken, refreshToken.getRefreshToken());

    }

    @CacheEvict(value = CacheKey.USER, key = "#email")
    public void logout(TokenDto tokenDto, String email) {
        String accessToken = resolveToken(tokenDto.getAccessToken());
        long remainMilliSeconds = jwtTokenUtil.getRemainMilliSeconds(accessToken);
        refreshTokenRedisRepository.deleteById(email);
        logoutAccessTokenRedisRepository.save(LogoutAccessToken.of(accessToken, email, remainMilliSeconds));
    }

    public TokenDto reissue(String refreshToken) {
        refreshToken = resolveToken(refreshToken);
        String email = getCurrentEmail();
        RefreshToken redisRefreshToken = refreshTokenRedisRepository.findById(email).orElseThrow(NoSuchElementException::new);

        if (refreshToken.equals(redisRefreshToken.getRefreshToken())) {
            return reissueRefreshToken(refreshToken, email);
        }
        throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
    }

    private TokenDto reissueRefreshToken(String refreshToken, String email) {
        if (lessThanReissueExpirationTimesLeft(refreshToken)) {
            String accessToken = jwtTokenUtil.generateAccessToken(email);
            return TokenDto.of(accessToken, saveRefreshToken(email).getRefreshToken());
        }
        return TokenDto.of(jwtTokenUtil.generateAccessToken(email), refreshToken);
    }

    private void validateDuplicateUser(UserDto.Request requestDto) {
        if(userRepository.existsByEmail(requestDto.getEmail()))
            throw new IllegalArgumentException("해당 이메일이 이미 존재합니다.");
        if(userRepository.existsByUserName(requestDto.getUserName()))
            throw new IllegalArgumentException("해당 닉네임이 이미 존재합니다.");
    }

    private void checkPassword(String rawPassword, String findMemberPassword) {
        if (!passwordEncoder.matches(rawPassword, findMemberPassword)) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }
    }

    private RefreshToken saveRefreshToken(String email) {
        return refreshTokenRedisRepository.save(RefreshToken.createRefreshToken(email,
                jwtTokenUtil.generateRefreshToken(email), REFRESH_TOKEN_EXPIRATION_TIME.getValue()));
    }

    private String resolveToken(String token) {
        return token.substring(7);
    }

    private String getCurrentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return principal.getUsername();
    }

    private boolean lessThanReissueExpirationTimesLeft(String refreshToken) {
        return jwtTokenUtil.getRemainMilliSeconds(refreshToken) < JwtExpirationEnums.REISSUE_EXPIRATION_TIME.getValue();
    }



}
