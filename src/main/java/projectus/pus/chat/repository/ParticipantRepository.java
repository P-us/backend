package projectus.pus.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectus.pus.chat.entity.ChatRoom;
import projectus.pus.chat.entity.Participant;
import projectus.pus.user.entity.User;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant,Long> {
    boolean existsByUserAndChatRoom(User user, ChatRoom chatRoom);
    Optional<Participant> findByUserAndChatRoom(User user, ChatRoom chatRoom);
}
