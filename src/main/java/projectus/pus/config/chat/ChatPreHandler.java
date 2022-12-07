package projectus.pus.config.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import projectus.pus.config.jwt.JwtTokenUtil;

@Component
@RequiredArgsConstructor
public class ChatPreHandler implements ChannelInterceptor {
    //    private final JwtTokenUtil jwtTokenUtil;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        String authorizationHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization"));
        if(authorizationHeader ==null || authorizationHeader.equals("null")){
            throw new MessageDeliveryException("메세지 예외");
        }
        String token = authorizationHeader.substring(7);
        System.out.println(token);
        //        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//            jwtTokenUtil.validateToken(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7),"ss");
//        }
        return message;
    }

}
