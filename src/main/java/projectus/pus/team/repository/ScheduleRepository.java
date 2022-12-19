package projectus.pus.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectus.pus.team.entity.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
