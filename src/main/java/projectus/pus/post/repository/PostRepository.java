package projectus.pus.post.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectus.pus.post.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByIdInAndTitleContains(List<Long> postId,String title, Pageable pageable);

    List<Post> findByTitleContains(String title, Pageable pageable);
}
