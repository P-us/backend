package projectus.pus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projectus.pus.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAllByPostId(Long postId);
    @Query("SELECT distinct c.post.id FROM Category c JOIN Tag t on t.category.id=c.id " +
            "WHERE c.field =:category and t.name in :tag ")
    List<Long> findByCategoryAndTag(@Param("category") String category, @Param("tag") List<String> tag);

    @Query("SELECT distinct c.post.id FROM Category c JOIN Tag t on t.category.id=c.id " +
            "WHERE t.name in :tag ")
    List<Long> findByTag(@Param("tag") List<String> tag);

    @Query("SELECT distinct c.post.id FROM Category c JOIN Tag t on t.category.id=c.id " +
            "WHERE c.field =:category ")
    List<Long> findByCategory(@Param("category") String category);

    @Query("SELECT distinct c.post.id FROM Category c ")
    List<Long> findAllPost();
}
