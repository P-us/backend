package projectus.pus.service.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import projectus.pus.repository.chat.ParticipantRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParticipantService {
    public final ParticipantRepository participantRepository;
}
