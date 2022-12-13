package projectus.pus.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import projectus.pus.chat.entity.ChatMessage;
import projectus.pus.chat.entity.ChatRoom;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByRoomOrderByCreatedDate(@Param("ChatRoom") ChatRoom room);

}
