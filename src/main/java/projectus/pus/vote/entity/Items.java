package projectus.pus.vote.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import projectus.pus.common.entity.BaseTimeEntity;
import projectus.pus.post.entity.Post;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Items extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "items_id")
    private Long id;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="vote_id")
    private Vote vote;

    private String field;

    @Builder
    public Items(String field){
        this.field = field;
    }

    public void setVote(Vote vote){
        this.vote = vote;
        vote.getItems().add(this);
    }

}

