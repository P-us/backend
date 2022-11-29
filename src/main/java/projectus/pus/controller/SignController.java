package projectus.pus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import projectus.pus.config.jwt.JwtTokenUtil;
import projectus.pus.dto.TokenDto;
import projectus.pus.dto.UserDto;
import projectus.pus.service.UserService;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class SignController {

    @Autowired
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/join")
    public ResponseEntity<Long> addUser(@Validated @RequestBody UserDto.Request requestDto, Errors errors) {
        Long userId = userService.addUser(requestDto);
        return ResponseEntity.created(URI.create("/api/users/"+userId)).build();
    }


    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserDto.Login requestDto) {
        return ResponseEntity.ok(userService.login(requestDto));
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String accessToken,
                       @RequestHeader("RefreshToken") String refreshToken) {
        String username = jwtTokenUtil.getEmail(resolveToken(accessToken));
        userService.logout(TokenDto.toToken(accessToken, refreshToken), username);
    }

    private String resolveToken(String accessToken) {
        return accessToken.substring(7);
    }
}
