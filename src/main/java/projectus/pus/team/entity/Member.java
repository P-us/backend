package projectus.pus.team.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import projectus.pus.common.entity.BaseTimeEntity;
import projectus.pus.user.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="team_id")
    private Team team;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();
    @Builder
    public Member(User user, Team team){
        this.user = user;
        this.team = team;
    }
    public void addTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }

}
