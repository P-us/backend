package projectus.pus.entity.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import projectus.pus.entity.BaseTimeEntity;
import projectus.pus.entity.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "room_id")
    private Long id;

    private String title;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id")
    private User host;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<Participant> participants = new ArrayList<>();

    @Builder
    public ChatRoom(String title, User user){
        this.title = title;
        this.host = user;
    }
    public void out(Participant participantEntity){
        int i = 0;
        for(Participant participant : getParticipants()){
            if(participant.getId() == participantEntity.getId()){
                getParticipants().remove(i);
                break;
            }
            i++;
        }
    }
}
