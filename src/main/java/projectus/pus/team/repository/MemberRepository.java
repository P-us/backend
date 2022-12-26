package projectus.pus.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectus.pus.team.entity.Member;
import projectus.pus.team.entity.Team;
import projectus.pus.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUserAndTeam(User user, Team team);
    Optional<Member> findByUserAndTeam(User user, Team team);
    List<Member> findAllByTeam(Team team);
    Optional<Member> findByUser(User user);
}
