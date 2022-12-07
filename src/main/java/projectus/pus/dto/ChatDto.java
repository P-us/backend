package projectus.pus.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChatDto {
    @Getter
    @Setter //todo setter 대신 다른거로 대체
    public static class ChatMessage {

        private String roomId;
        private String writer;
        private String message;
    }

    @Getter
    @Setter
    public static class ChatRoom {
        private String roomId;
        private String name;
        private Set<WebSocketSession> sessions = new HashSet<>();

        public static ChatRoom create(String name){
            ChatRoom room = new ChatDto.ChatRoom();
            room.roomId = UUID.randomUUID().toString();
            room.name = name;
            return room;
        }

    }
}
