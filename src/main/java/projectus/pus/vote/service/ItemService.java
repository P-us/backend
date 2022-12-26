package projectus.pus.vote.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import projectus.pus.post.dto.PostDto;
import projectus.pus.post.entity.Category;
import projectus.pus.post.entity.Post;
import projectus.pus.vote.dto.VoteDto;
import projectus.pus.vote.entity.Items;
import projectus.pus.vote.entity.Vote;
import projectus.pus.vote.repository.ItemsRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemsRepository itemsRepository;

    @Transactional
    public void addItems(VoteDto.Request requestDto, Vote vote) {
        if(!CollectionUtils.isEmpty(requestDto.getItems())) {
            for (VoteDto.ItemsRequest itemsRequest : requestDto.getItems()){
                Items items = Items
                        .builder()
                        .field(itemsRequest.getField())
                        .build();
                items.setVote(vote);
                itemsRepository.save(items);
            }
        }
    }

    @Transactional
    public void updateItems(Vote vote, Long voteId, VoteDto.Request requestDto) {
        List<Items> itemsList = itemsRepository.findAllByVoteId(voteId);
        if (!CollectionUtils.isEmpty(itemsList)){
            for(Items items : itemsList)
                itemsRepository.delete(items);
        }
        if(!CollectionUtils.isEmpty(requestDto.getItems())) {
            for (VoteDto.ItemsRequest itemsRequest : requestDto.getItems()){
                Items items = Items
                        .builder()
                        .field(itemsRequest.getField())
                        .build();
                items.setVote(vote);
                itemsRepository.save(items);
            }
        }
    }
}
