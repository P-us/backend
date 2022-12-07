package projectus.pus.repository;

import org.springframework.stereotype.Repository;
import projectus.pus.dto.ChatDto;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ChatRoomRepository { //todo 우선은 Map Collection에 저장하지만 DB에 저장해야한다 무조건
    private Map<String, ChatDto.ChatRoom> chatRoomDTOMap;

    @PostConstruct
    private void init(){
        chatRoomDTOMap = new LinkedHashMap<>();
    }

    public List<ChatDto.ChatRoom> findAllRooms(){
        //채팅방 생성 순서 최근 순으로 반환
        List<ChatDto.ChatRoom> result = new ArrayList<>(chatRoomDTOMap.values());
        Collections.reverse(result);

        return result;
    }

    public ChatDto.ChatRoom findRoomById(String id){
        return chatRoomDTOMap.get(id);
    }

    public ChatDto.ChatRoom createChatRoomDTO(String name){
        ChatDto.ChatRoom room = ChatDto.ChatRoom.create(name);
        chatRoomDTOMap.put(room.getRoomId(), room);

        return room;
    }
}
