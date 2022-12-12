package projectus.pus.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import projectus.pus.entity.chat.ChatMessage;
import projectus.pus.entity.chat.ChatRoom;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByRoomOrderByCreatedDate(@Param("ChatRoom") ChatRoom room);

}
