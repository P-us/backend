package projectus.pus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectus.pus.dto.PostDto;
import projectus.pus.entity.Like;
import projectus.pus.entity.Post;
import projectus.pus.entity.User;
import projectus.pus.repository.LikeRepository;
import projectus.pus.repository.PostRepository;
import projectus.pus.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {
    private LikeRepository likeRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;

    @Transactional
    public void addLike(Long postId, Long userId){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        if(likeRepository.findByPostIdAndUserId(postId, userId)==null){
            Like like = Like.builder()
                    .post(post)
                    .user(user)
                    .build();
            likeRepository.save(like);
        }
        else{
            throw new IllegalArgumentException("이미 좋아요 상태입니다.");
        }
    }

    @Transactional
    public void deleteLike(Long postId, Long userId){
        validatePostAndUser(postId, userId);
        Like like = likeRepository.findByPostIdAndUserId(postId, userId);
        if(like==null){
            throw new IllegalArgumentException("취소할 좋아요가 없습니다.");
        }
        else{
            likeRepository.delete(like);
        }
    }
    @Transactional(readOnly = true) //해당 유저가 좋아요를 눌렀는지 여부랑 그 게시글의 총 좋아요 수
    public PostDto.LikeResponse getLike(Long postId, Long userId){
        validatePostAndUser(postId, userId);
        int likeCount = likeRepository.findAllByPostId(postId).size();
        if(likeRepository.findByPostIdAndUserId(postId, userId)==null){
            return new PostDto.LikeResponse(likeCount,false);
        }
        else {
            return new PostDto.LikeResponse(likeCount, true);
        }
    }

    private void validatePostAndUser(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
    }
}
