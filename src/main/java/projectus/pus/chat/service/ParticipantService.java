package projectus.pus.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectus.pus.chat.entity.ChatRoom;
import projectus.pus.chat.entity.Participant;
import projectus.pus.user.entity.User;
import projectus.pus.chat.repository.ParticipantRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParticipantService {
    public final ParticipantRepository participantRepository;

    @Transactional
    public void save(User user, ChatRoom chatRoom) {
        if(!participantRepository.existsByUserAndChatRoom(user, chatRoom)){
            Participant participant = new Participant(user, chatRoom);
            participant.addChatRoom(chatRoom);
            participantRepository.save(participant);
        }
        else{
            throw new IllegalArgumentException("이미 참여중인 방입니다.");
        }
    }

    @Transactional
    public void out(User user, ChatRoom chatRoom){
        Participant participant = participantRepository.findByUserAndChatRoom(user, chatRoom)
                .orElseThrow(()-> new IllegalArgumentException("해당 방에 참여하지 않은 유저입니다."));
        chatRoom.out(participant);
        participantRepository.delete(participant);
    }
}
