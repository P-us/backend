package projectus.pus.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projectus.pus.chat.entity.ChatRoom;
import projectus.pus.team.entity.Team;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("SELECT distinct t FROM Team t JOIN Member m on t.id = m.team.id " +
            "WHERE m.user.id =:userId ")
    List<Team> findByUserId(@Param("userId") Long userId);

}
