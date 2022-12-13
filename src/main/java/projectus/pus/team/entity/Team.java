package projectus.pus.team.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import projectus.pus.chat.entity.Participant;
import projectus.pus.common.entity.BaseTimeEntity;
import projectus.pus.user.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Team extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "team_id")
    private Long id;

    private String name;
    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id")
    private User leader;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

    @Builder
    public Team(String name, User user){
        this.name = name;
        this.leader = user;
    }

    public void out(Member memberEntity){
        int i = 0;
        for(Member member : getMembers()){
            if(member.getId() == memberEntity.getId()){
                getMembers().remove(i);
                break;
            }
            i++;
        }
    }

}
