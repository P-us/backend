package projectus.pus.service.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectus.pus.dto.chat.ChatDto;
import projectus.pus.entity.chat.ChatRoom;
import projectus.pus.entity.user.User;
import projectus.pus.repository.chat.ChatRoomRepository;
import projectus.pus.repository.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createRoom(String title, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        ChatRoom chatRoom = new ChatRoom(title, user);
        return chatRoomRepository.save(chatRoom).getId();
    }
    @Transactional(readOnly = true)
    public ChatDto.ChatRoomResponse getRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
        return new ChatDto.ChatRoomResponse(chatRoom);
    }

    @Transactional(readOnly = true)
    public Page<ChatDto.ChatRoomResponse> getAllRooms(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        List<ChatRoom> roomList = chatRoomRepository.findByHost(userId);
        List<ChatDto.ChatRoomResponse> collect = roomList
                .stream()
                .map(ChatDto.ChatRoomResponse::new)
                .collect(Collectors.toList());
        int start = (int)pageable.getOffset();
        int end = Math.min((start+pageable.getPageSize()),collect.size());
        return new PageImpl<>(collect.subList(start, end), pageable, collect.size());
    }
}
