package projectus.pus.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectus.pus.chat.dto.ChatDto;
import projectus.pus.chat.entity.ChatMessage;
import projectus.pus.chat.entity.ChatRoom;
import projectus.pus.user.entity.User;
import projectus.pus.chat.repository.ChatRepository;
import projectus.pus.chat.repository.ChatRoomRepository;
import projectus.pus.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;


    @Transactional
    public ChatDto.Chat saveChat(ChatDto.ChatMessage chatMessage, Long roomId, String host) {
        User user = userRepository.findByUserName(host).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        ChatMessage chatmsg = chatRepository.save(ChatMessage.of(chatMessage.getMessage(), user, chatRoom));

        return ChatDto.Chat.builder()
                .userId(user.getId())
                .writer(user.getUserName())
                .message(chatmsg.getMessage())
                .sendAt(chatmsg.getCreatedDate())
                .build();

    }

    @Transactional
    public List<ChatDto.Chat> getChatHistory(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        List<ChatMessage> chatEntityList = chatRepository.findByRoomOrderByCreatedDate(chatRoom);
        List<ChatDto.Chat> history = chatEntityList.stream().map(
                chat -> ChatDto.Chat.builder()
                        .writer(chat.getUser().getUserName())
                        .userId(chat.getUser().getId())
                        .sendAt(chat.getCreatedDate())
                        .message(chat.getMessage())
                        .build()
        ).collect(Collectors.toList());

        return history;
    }
}
