package projectus.pus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import projectus.pus.dto.PostDto;
import projectus.pus.entity.Photo;
import projectus.pus.entity.Post;
import projectus.pus.repository.PhotoRepository;
import projectus.pus.repository.PostRepository;
import projectus.pus.utils.PhotoHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PhotoHandler photoHandler;
    private final PhotoRepository photoRepository;
    @Transactional
    public Long addPost(PostDto.Request requestDto, List<MultipartFile> files) throws Exception {
        Post post = requestDto.toEntity();
        List<Photo> photoList = photoHandler.parseFileInfo(files);
        if(!photoList.isEmpty()) {
            for(Photo photo : photoList) {
                post.addPhoto(photoRepository.save(photo));
            }
        }
        return postRepository.save(post).getId();
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
