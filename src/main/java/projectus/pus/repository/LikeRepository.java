package projectus.pus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectus.pus.entity.Like;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
     Like findByPostIdAndUserId(Long postId, Long userId);

     List<Like> findAllByPostId(Long postId);
}
