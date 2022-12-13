package projectus.pus.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import projectus.pus.chat.dto.ChatDto;
import projectus.pus.chat.service.ChatService;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달

    @MessageMapping(value = "/chat/message")
    public void chat(ChatDto.ChatMessage message){
        if (message.getType().equals("ENTER")){
            message.updateMessage(message,message.getWriter() + "님이 채팅방에 참여하였습니다.");
        }
        ChatDto.Chat chat = chatService.saveChat(message, message.getRoomId(), message.getWriter());
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @GetMapping("/api/chats/room/{roomId}/history")
    public ResponseEntity<?> getChatInfo(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.getChatHistory(roomId));
    }
}
