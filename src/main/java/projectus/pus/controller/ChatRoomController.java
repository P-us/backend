package projectus.pus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectus.pus.repository.ChatRoomRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/chats")
public class ChatRoomController { //이곳에서 여러 상황에 따라 케이스별로 만들기 1. 글작성자와 1:1 채팅버튼 누를때, 2. 글 작성자가 개인이랑 채팅할때 톡에 있는 초대하기 버튼 누를때
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/rooms")
    public ResponseEntity<?> getRooms(){ // todo ? 바꾸기
        return ResponseEntity.ok().body(chatRoomRepository.findAllRooms());
    }

    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@RequestBody String name){
        chatRoomRepository.createChatRoomDTO(name);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable String roomId){
        return ResponseEntity.ok().body(chatRoomRepository.findRoomById(roomId));
    }
}
