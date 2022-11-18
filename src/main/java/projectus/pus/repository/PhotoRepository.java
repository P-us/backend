package projectus.pus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectus.pus.entity.Photo;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findAllByPostId(Long postId);
}
