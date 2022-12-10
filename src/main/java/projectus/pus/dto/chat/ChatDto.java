package projectus.pus.dto.chat;

import lombok.*;
import projectus.pus.entity.chat.ChatRoom;

import java.util.List;
import java.util.stream.Collectors;

public class ChatDto {
    @Getter
    @Setter //todo setter 대신 다른거로 대체
    public static class ChatMessage {
        private Long roomId;
        private String writer;
        private String message;
        private String type;
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
