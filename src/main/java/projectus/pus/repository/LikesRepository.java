package projectus.pus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectus.pus.entity.Likes;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
     Likes findByPostIdAndUserId(Long postId, Long userId);

     List<Likes> findAllByPostId(Long postId);
}
