package projectus.pus.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectus.pus.post.dto.CommentDto;
import projectus.pus.post.entity.NestedComment;

import java.util.List;

@Repository
public interface NestedCommentRepository extends JpaRepository<NestedComment, Long> {
    List<CommentDto.NestedResponse> findAllByCommentId(Long commentId);
}
