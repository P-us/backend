package projectus.pus.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectus.pus.entity.chat.ChatRoom;
import projectus.pus.entity.chat.Participant;
import projectus.pus.entity.user.User;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant,Long> {
    boolean existsByUserAndChatRoom(User user, ChatRoom chatRoom);
    Optional<Participant> findByUserAndChatRoom(User user, ChatRoom chatRoom);
}
