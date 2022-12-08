package projectus.pus.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import projectus.pus.dto.post.PostDto;
import projectus.pus.entity.post.Likes;
import projectus.pus.entity.post.Post;
import projectus.pus.entity.user.User;
import projectus.pus.repository.post.LikesRepository;
import projectus.pus.repository.post.PostRepository;
import projectus.pus.repository.user.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void addLikes(Long postId, Long userId){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        if(ObjectUtils.isEmpty(likesRepository.findByPostIdAndUserId(postId, userId))){
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
    @Transactional(readOnly = true)
    public PostDto.LikesResponse getLikesCount(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        int likeCount = likesRepository.findAllByPostId(postId).size();
        return new PostDto.LikesResponse(likeCount);
    }

    @Transactional(readOnly = true)
    public boolean checkLikes(Long postId, Long userId) {
        validatePostAndUser(postId,userId);
        return likesRepository.findByPostIdAndUserId(postId, userId) != null;
    }
    private void validatePostAndUser(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
    }
}
