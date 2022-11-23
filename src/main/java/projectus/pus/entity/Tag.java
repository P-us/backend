package projectus.pus.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor
public class Tag extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "tag_id")
    private Long id;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="post_id")
    private Post post;

    private String name;

    @Builder
    public Tag(String name){
        this.name = name;
    }

    public static List<Tag> of(List<String> tag) {
        return tag.stream()
                .map(Tag::new)
                .collect(Collectors.toList());
    }

    public void setPost(Post post){
        this.post = post;
    }
}
