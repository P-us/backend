package projectus.pus.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectus.pus.config.jwt.JwtTokenUtil;
import projectus.pus.user.dto.TokenDto;
import projectus.pus.user.dto.UserDto;
import projectus.pus.user.service.UserService;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class SignController {

    @Autowired
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserDto.Login requestDto) {
        return ResponseEntity.ok(userService.login(requestDto));
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String accessToken,
                       @RequestHeader("RefreshToken") String refreshToken) {
        String username = jwtTokenUtil.getEmail(resolveToken(accessToken));
        userService.logout(TokenDto.of(accessToken, refreshToken), username);
    }

    private String resolveToken(String accessToken) {
        return accessToken.substring(7);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestHeader("Authorization") String accessToken,
                                            @RequestHeader("RefreshToken") String refreshToken) {
        return ResponseEntity.ok(userService.reissue(accessToken, refreshToken));
    }
}
