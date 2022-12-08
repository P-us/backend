package projectus.pus.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectus.pus.entity.post.Tag;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    List<Tag> findAllByCategoryId(Long categoryId);
}
