package projectus.pus.meet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectus.pus.meet.entity.Meet;

@Repository
public interface MeetRepository extends JpaRepository<Meet,Long> {
}
