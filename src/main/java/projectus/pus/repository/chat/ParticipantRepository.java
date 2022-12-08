package projectus.pus.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectus.pus.entity.chat.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant,Long> {
}
