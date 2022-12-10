package projectus.pus.entity.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import projectus.pus.entity.BaseTimeEntity;
import projectus.pus.entity.user.User;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Participant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "participant_id")
    private Long id;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="room_id")
    private ChatRoom chatRoom;

    @Builder
    public Participant(User user, ChatRoom chatRoom){
        this.user = user;
        this.chatRoom = chatRoom;
    }
    public void addChatRoom(ChatRoom chatRoom){
        this.chatRoom = chatRoom;
        chatRoom.getParticipants().add(this);
    }
}
