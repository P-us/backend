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
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="post_id")
    private Post post;

    @Builder
    public Comment(String content, User user, Post post){
        this.content = content;
        this.user = user;
        this.post = post;
    }

    public void update(String content) {
        this.content = content;
    }
}
