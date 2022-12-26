package projectus.pus.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectus.pus.vote.entity.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
}
