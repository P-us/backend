package projectus.pus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import projectus.pus.dto.UserDto;
import projectus.pus.service.UserService;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping
//    추후 @PreAuthorize("hasRole('')) 추가해야함
    public ResponseEntity<Long> addUser(@Validated @RequestBody UserDto.Request requestDto, Errors errors) {
//        if(errors.hasErrors()) {
//            return ResponseEntity.ok().body());
//        }
        Long userId = userService.addUser(requestDto);
        return ResponseEntity.ok().body(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto.Response> getUser(@PathVariable Long userId) {
        UserDto.Response responseDto = userService.getUser(userId);
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
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().build();
    }
}
