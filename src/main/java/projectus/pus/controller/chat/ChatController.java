package projectus.pus.controller.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import projectus.pus.dto.chat.ChatDto;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
    //private final ChatService chatService    이곳에서 .save로 메시지 저장
    @MessageMapping(value = "/chat/message")
    public void message(ChatDto.ChatMessage message){
        if (message.getType().equals("ENTER")){
            message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        }
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
