package projectus.pus.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectus.pus.post.entity.Category;
import projectus.pus.vote.entity.Items;
import projectus.pus.vote.entity.Vote;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {
      List<Items> findAllByVoteId(Long voteId);
}
