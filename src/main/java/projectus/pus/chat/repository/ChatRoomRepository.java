package projectus.pus.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projectus.pus.chat.entity.ChatRoom;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    @Query("SELECT distinct c FROM ChatRoom c JOIN Participant p on c.id = p.chatRoom.id " +
            "WHERE p.user.id =:userId ")
    List<ChatRoom> findByUserId(@Param("userId") Long userId);
}
