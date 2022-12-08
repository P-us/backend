package projectus.pus.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projectus.pus.entity.chat.ChatRoom;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    @Query("SELECT distinct c FROM ChatRoom c WHERE c.host.id =:userId ")
    List<ChatRoom> findByHost(@Param("userId") Long userId);
}
