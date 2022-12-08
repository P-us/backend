package projectus.pus.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoom extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "room_id")
    private Long id;

    private String title;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id")
    private User host;

    //todo 참여자 manytomany participant

    @Builder
    public ChatRoom(String title, User user){
        this.title = title;
        this.host = user;
    }
}
