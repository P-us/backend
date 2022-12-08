package projectus.pus.controller.user;

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
import projectus.pus.dto.user.UserDto;
import projectus.pus.entity.user.User;
import projectus.pus.repository.user.UserRepository;
import projectus.pus.service.user.MailService;
import projectus.pus.service.user.UserService;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final MailService mailService;
    @Autowired
    private final UserRepository userRepository;

    @PostMapping("/join")
    public ResponseEntity<Long> addUser(@Validated @RequestBody UserDto.Request requestDto, Errors errors) {
        Long userId = userService.addUser(requestDto);
        return ResponseEntity.created(URI.create("/api/users/"+userId)).build();
    }

    @PostMapping("/join/mailConfirm")
    @ResponseBody
    public String mailConfirm(@RequestParam String email) throws Exception {
        String code = mailService.sendSimpleMessage(email);
        log.info("인증코드 : " + code);
        return code;
    }

    @PostMapping("/join/mailCheck")
    @ResponseBody
    public void mailCheck(@RequestParam String check) {
        mailService.checkMail(check);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto.Response> getUser(@PathVariable Long userId) {
        UserDto.Response responseDto = userService.findUser(userId);
        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable Long userId, @RequestBody UserDto.Request requestDto) {
        userService.updateUser(userId, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build(); //별도로 반환해야 할 데이터가 없을경우 +201
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/users/me")
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
