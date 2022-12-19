package projectus.pus.meet.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import projectus.pus.common.entity.BaseTimeEntity;
import projectus.pus.user.entity.User;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Meet extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "meet_id")
    private Long id;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id")
    private User host;


}
