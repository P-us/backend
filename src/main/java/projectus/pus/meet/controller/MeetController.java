package projectus.pus.meet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projectus.pus.chat.dto.ChatDto;
import projectus.pus.config.security.CurrentUser;
import projectus.pus.config.security.CustomUserDetails;
import projectus.pus.meet.service.MeetService;
import projectus.pus.post.dto.PostDto;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/meets")
public class MeetController {
    @Autowired
    private final MeetService meetService;

//    @PostMapping("/room")
//    public ResponseEntity<Void> createMeet(
//            @RequestBody ChatDto.ChatRoomRequest request, @CurrentUser CustomUserDetails currentUser){
//        Long roomId = chatRoomService.createRoom(request.getTitle(),currentUser.getUserId());
//        return ResponseEntity.created(URI.create("/api/chats/"+roomId)).build();
}
