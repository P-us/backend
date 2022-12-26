package projectus.pus.team.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import projectus.pus.common.entity.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@Entity
@NoArgsConstructor
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="member_id")
    private Member member;

    private String day;

    private LocalTime start;
    private LocalTime end;

    @Builder
    public Schedule(String day, LocalTime start, LocalTime end, Member member){
        this.day = day;
        this.start = start;
        this.end = end;
        this.member = member;
    }

//    public void addMember(Member member){
//        this.member = member;
//    }
}
