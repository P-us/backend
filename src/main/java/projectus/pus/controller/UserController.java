package projectus.pus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import projectus.pus.config.security.CurrentUser;
import projectus.pus.config.security.CustomUserDetails;
import projectus.pus.dto.UserDto;
import projectus.pus.entity.User;
import projectus.pus.repository.UserRepository;
import projectus.pus.service.UserService;

import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final UserRepository userRepository;

    @PostMapping("/join")
    public ResponseEntity<Long> addUser(@Validated @RequestBody UserDto.Request requestDto, Errors errors) {
        Long userId = userService.addUser(requestDto);
        return ResponseEntity.created(URI.create("/api/users/"+userId)).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto.Response> getUser(@PathVariable Long userId) {
        UserDto.Response responseDto = userService.findUser(userId);
        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable Long userId, @RequestBody UserDto.Request requestDto) {
        userService.updateUser(userId, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build(); //별도로 반환해야 할 데이터가 없을경우 +201
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/me")
    public void getCurrentUser(@CurrentUser CustomUserDetails user) {
        log.info(user.getUsername());
        log.info(user.getPassword());
        log.info(user.getAuthorities().toString());
        User user1 = userRepository.findByEmail(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        log.info(user1.getUserName());
        log.info(user1.getEmail());

    }
}
