package projectus.pus.vote.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import projectus.pus.common.entity.BaseTimeEntity;
import projectus.pus.post.entity.Post;
import projectus.pus.user.dto.UserDto;
import projectus.pus.user.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Vote extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "vote_id")
    private Long id;

    private String title;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "vote")
    private List<Items> items = new ArrayList<>();

    @Builder
    public Vote(String title, User user) {
        this.title = title;
        this.user = user;
    }

    public void update(Vote vote) {
        this.title =  vote.title;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addItems(Items items) {
        this.items.add(items);
    }
}

