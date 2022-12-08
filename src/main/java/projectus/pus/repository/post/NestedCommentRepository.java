package projectus.pus.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectus.pus.dto.post.CommentDto;
import projectus.pus.entity.post.NestedComment;

import java.util.List;

@Repository
public interface NestedCommentRepository extends JpaRepository<NestedComment, Long> {
    List<CommentDto.NestedResponse> findAllByCommentId(Long commentId);
}
