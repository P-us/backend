package projectus.pus.chat.dto;

import lombok.*;
import projectus.pus.chat.entity.ChatRoom;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ChatDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessage {
        private Long roomId;
        private String writer;
        private String message;
        private String type;

        public ChatMessage updateMessage(ChatMessage chatMessage, String msg) {
            this.roomId = chatMessage.getRoomId();
            this.writer = chatMessage.getWriter();
            this.message = msg;
            this.type = chatMessage.getType();

            return chatMessage;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Chat {
        private Long userId;
        private String writer;
        private String message;
        private LocalDateTime sendAt;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRoomRequest {
        private String title;
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRoomResponse {
        private Long roomId;
        private String title;
        private String host;
        private List<String> userName;

        public ChatRoomResponse(ChatRoom chatRoom){
            this.roomId = chatRoom.getId();
            this.title = chatRoom.getTitle();
            this.host = chatRoom.getHost().getUserName();
            this.userName = chatRoom.getParticipants()
                    .stream()
                    .map(participant -> participant.getUser().getUserName())
                    .collect(Collectors.toList());
        }
    }
}
