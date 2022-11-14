package projectus.pus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projectus.pus.dto.UserDto;
import projectus.pus.entity.User;
import projectus.pus.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Long addUser(UserDto.Request requestDto) {
        User user = userRepository.save(requestDto.toEntity());
        return user.getId();
    }

    public UserDto.Response getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        return new UserDto.Response(user);
    }

    public void updateUser(Long userId, UserDto.Request requestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        userRepository.delete(user);
    }
}
