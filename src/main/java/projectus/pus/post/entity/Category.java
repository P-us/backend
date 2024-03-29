package projectus.pus.post.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import projectus.pus.common.entity.BaseTimeEntity;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "category_id")
    private Long id;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="post_id")
    private Post post;

    private String field;

    @Builder
    public Category(String field){
        this.field = field;
    }
    public void setPost(Post post){
        this.post = post;
        post.getCategory().add(this);
    }
}
