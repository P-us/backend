package projectus.pus.vote.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import projectus.pus.post.entity.Photo;
import projectus.pus.user.entity.User;
import projectus.pus.user.repository.UserRepository;
import projectus.pus.vote.dto.VoteDto;
import projectus.pus.vote.entity.Items;
import projectus.pus.vote.entity.Vote;
import projectus.pus.vote.repository.VoteRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    private final ItemService itemsService;

    public Long createVote(VoteDto.Request requestDto, List<Items> items, Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다.")
        );

        Vote vote = requestDto.toEntity();
        vote.setUser(user);
        itemsService.addItems(requestDto, vote);

        return voteRepository.save(vote).getId();
    }

    public void updateVote(Long voteId, Long userId, VoteDto.Request requestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다.")
        );
        Vote vote = voteRepository.findById(voteId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        if(vote.getUser().getId().equals(userId)) {
            itemsService.updateItems(vote, voteId, requestDto);
            vote.update(requestDto.toEntity());
        }
        else{
            throw new IllegalArgumentException("해당 계정은 글에 대한 권한이 없습니다.");
        }
    }

    public void deleteVote(Long voteId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다.")
        );
        Vote vote = voteRepository.findById(voteId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        if(vote.getUser().getId().equals(userId)) {
            voteRepository.delete(vote);
        }
        else {
            throw new IllegalArgumentException("해당 계정은 삭제에 대한 권한이 없습니다.");
        }
    }


}
