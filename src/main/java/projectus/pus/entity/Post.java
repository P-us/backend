package projectus.pus.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "post_id")
    private Long id;
    private String title;
    private String content;
    //todo user
    @Enumerated(EnumType.STRING)
    private Category category;
    //private List<String> tag;
    @OneToMany(
            mappedBy = "post",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<Photo> photo = new ArrayList<>();
    @Builder
    public Post(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
    public void update(Post post) {
        this.title = post.title;
        this.content = post.content;
        this.category = post.category;
    }
    public void addPhoto(Photo photo) {
        this.photo.add(photo);
        if(photo.getPost() !=this)
            photo.setPost(this);
    }
}
