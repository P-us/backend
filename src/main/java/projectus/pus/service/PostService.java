package projectus.pus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectus.pus.dto.PostDto;
import projectus.pus.entity.Post;
import projectus.pus.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    @Transactional
    public Long addPost(PostDto.Request requestDto) {
        Post post = postRepository.save(requestDto.toEntity());
        return post.getId();
    }
    @Transactional(readOnly = true)
    public PostDto.Response findPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        return new PostDto.Response(post);
    }

    @Transactional
    public void updatePost(Long postId, PostDto.Request requestDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        post.update(requestDto.toEntity());
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        postRepository.delete(post);
    }
}
