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

    @OneToMany(mappedBy = "post")
    private List<Category> category = new ArrayList<>();
    @OneToMany(
            mappedBy = "post",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<Photo> photo = new ArrayList<>();
    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public void update(Post post) {
        this.title = post.title;
        this.content = post.content;
    }
    public void addCategory(Category category){
        this.category.add(category);
        if(category.getPost() !=this)
            category.setPost(this);
    }
    public void addPhoto(Photo photo) {
        this.photo.add(photo);
        if(photo.getPost() !=this)
            photo.setPost(this);
    }
}
