package projectus.pus.dto;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;
import projectus.pus.entity.ChatRoom;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

        public ChatRoomResponse(ChatRoom chatRoom){
            this.roomId = chatRoom.getId();
            this.title = chatRoom.getTitle();
            this.host = chatRoom.getHost().getUserName();
        }
    }
}
