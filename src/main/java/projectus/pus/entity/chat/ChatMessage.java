package projectus.pus.entity.chat;

import lombok.*;
import projectus.pus.dto.chat.ChatDto;
import projectus.pus.entity.BaseTimeEntity;
import projectus.pus.entity.user.User;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chat_id")
    private Long id;

    @Column(name = "MESSAGE")
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "rood_id")
    private ChatRoom room;

    @Builder(access = AccessLevel.PRIVATE)
    private ChatMessage(String message, User user, ChatRoom room) {
        this.message = message;
        this.user = user;
        this.room = room;
    }

    public static ChatMessage of(String message, User user, ChatRoom room){
        return ChatMessage.builder()
                .message(message)
                .user(user)
                .room(room)
                .build();
    }

}
