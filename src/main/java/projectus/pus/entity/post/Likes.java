package projectus.pus.entity.post;

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

public class Likes extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id")
    private User user;

    @Builder
    public Likes(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}
