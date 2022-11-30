package projectus.pus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectus.pus.dto.PostDto;
import projectus.pus.entity.Likes;
import projectus.pus.entity.Post;
import projectus.pus.entity.User;
import projectus.pus.repository.LikesRepository;
import projectus.pus.repository.PostRepository;
import projectus.pus.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikesService {
    private LikesRepository likesRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;

    @Transactional
    public void addLikes(Long postId, Long userId){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        if(likesRepository.findByPostIdAndUserId(postId, userId)==null){
            Likes likes = Likes.builder()
                    .post(post)
                    .user(user)
                    .build();
            likesRepository.save(likes);
        }
        else{
            throw new IllegalArgumentException("이미 좋아요 상태입니다.");
        }
    }

    @Transactional
    public void deleteLikes(Long postId, Long userId){
        validatePostAndUser(postId, userId);
        Likes likes = likesRepository.findByPostIdAndUserId(postId, userId);
        if(likes ==null){
            throw new IllegalArgumentException("취소할 좋아요가 없습니다.");
        }
        else{
            likesRepository.delete(likes);
        }
    }
    @Transactional(readOnly = true) //해당 유저가 좋아요를 눌렀는지 여부랑 그 게시글의 총 좋아요 수
    public PostDto.LikesResponse getLikes(Long postId, Long userId){
        validatePostAndUser(postId, userId);
        int likeCount = likesRepository.findAllByPostId(postId).size();
        if(likesRepository.findByPostIdAndUserId(postId, userId)==null){
            return new PostDto.LikesResponse(likeCount,false);
        }
        else {
            return new PostDto.LikesResponse(likeCount, true);
        }
    }

    private void validatePostAndUser(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
    }
}
