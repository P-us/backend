package projectus.pus.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectus.pus.config.security.CurrentUser;
import projectus.pus.config.security.CustomUserDetails;
import projectus.pus.chat.dto.ChatDto;
import projectus.pus.chat.service.ChatRoomService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/chats")
public class ChatRoomController { //이곳에서 여러 상황에 따라 케이스별로 만들기 1. 글작성자와 1:1 채팅버튼 누를때, 2. 글 작성자가 개인이랑 채팅할때 톡에 있는 초대하기 버튼 누를때
    private final ChatRoomService chatRoomService;

    @PostMapping("/room")
    public ResponseEntity<Void> createRoom(
            @RequestBody ChatDto.ChatRoomRequest request, @CurrentUser CustomUserDetails currentUser){
        Long roomId = chatRoomService.createRoom(request.getTitle(),currentUser.getUserId());
        return ResponseEntity.created(URI.create("/api/chats/"+roomId)).build();
    }
//    @DeleteMapping("/room/{roomId}") //todo DeleteRoom, UpdateRoom   , currentUser 와 host가 같은지 체크
//    public ResponseEntity<Void> deleteRoom(@CurrentUser CustomUserDetails currentUser){
//        chatRoomService.deleteRoom(currentUser.getUserId());
//        return ResponseEntity.ok().build();
//    }
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatDto.ChatRoomResponse> getRoom(@PathVariable Long roomId){
        System.out.println("roomId = " + roomId);
        return ResponseEntity.ok().body(chatRoomService.getRoom(roomId));
    }
    @GetMapping("/rooms")
    public ResponseEntity<Page<ChatDto.ChatRoomResponse>> getRoomList(
            @CurrentUser CustomUserDetails currentUser,
            @PageableDefault(sort="modifiedDate",direction = Sort.Direction.DESC) Pageable pageable){ // todo ? 바꾸기
        return ResponseEntity.ok().body(chatRoomService.getRoomList(currentUser.getUserId(),pageable));
    }

    @PostMapping("/room/{roomId}/user")
    public ResponseEntity<Void> participateRoom(@PathVariable Long roomId, @CurrentUser CustomUserDetails currentUser){
        chatRoomService.participateRoom(currentUser.getUserId(),roomId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/room/{roomId}/user")
    public ResponseEntity<Void> leaveRoom(@PathVariable Long roomId, @CurrentUser CustomUserDetails currentUser){
        chatRoomService.leaveRoom(currentUser.getUserId(),roomId);
        return ResponseEntity.ok().build();
    }
}
