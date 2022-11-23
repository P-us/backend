package projectus.pus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import projectus.pus.dto.PostDto;
import projectus.pus.entity.Category;
import projectus.pus.entity.Photo;
import projectus.pus.entity.Post;
import projectus.pus.entity.Tag;
import projectus.pus.repository.PhotoRepository;
import projectus.pus.repository.PostRepository;
import projectus.pus.repository.TagRepository;
import projectus.pus.utils.PhotoHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PhotoHandler photoHandler;
    private final PhotoRepository photoRepository;
    private final TagRepository tagRepository;
    @Transactional
    public Long addPost(PostDto.Request requestDto, List<MultipartFile> files) throws Exception {
        Post post = requestDto.toEntity();
        if(!CollectionUtils.isEmpty(requestDto.getTag())) {
            List<Tag> tagList = Tag.of(requestDto.getTag());
            if (!tagList.isEmpty()) {
                for (Tag tag : tagList) {
                    tag.setPost(post);
                    tagRepository.save(tag);
                }
            }
        }
        List<Photo> photoList = photoHandler.parseFileInfo(files); //todo 시간 남는다면 PhotoService, TagService 생성해서 리팩토링
        if (!photoList.isEmpty()) {
            for (Photo photo : photoList) {
                post.addPhoto(photoRepository.save(photo));
            }
        }
        return postRepository.save(post).getId();
    }

    @Transactional(readOnly = true)
    public PostDto.Response getDetailPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        List<String> tagList = tagRepository.findAllByPostId(postId)
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
        //List<Long> fileId = post.getPhoto().stream().map(Photo::getId).collect(Collectors.toList());
        List<Long> fileId = photoRepository.findAllByPostId(postId)
                 .stream()
                 .map(Photo::getId)
                 .collect(Collectors.toList());
        return new PostDto.Response(post,fileId,tagList);
    }
    @Transactional(readOnly = true)
    public byte[] getImage(Long photoId) throws IOException {
        String path = photoRepository.findById(photoId).orElseThrow(
                () -> new IllegalArgumentException("해당 사진이 존재하지 않습니다.")).getFilePath();
        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
        InputStream imageStream = new FileInputStream(absolutePath + path);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();
        return imageByteArray;
    }
    @Transactional
    public void updatePost(Long postId, PostDto.Request requestDto, List<MultipartFile> files) throws Exception {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        List<Photo> dbPhotoList = photoRepository.findAllByPostId(postId);
        List<Tag> dbTagList = tagRepository.findAllByPostId(postId);
        if(!CollectionUtils.isEmpty(dbTagList)){
            for(Tag dbTag : dbTagList)
                tagRepository.delete(dbTag);
        }
        if(!CollectionUtils.isEmpty(requestDto.getTag())) {
            List<Tag> tagList = Tag.of(requestDto.getTag());
            if (!tagList.isEmpty()) {
                for (Tag tag : tagList) {
                    tag.setPost(post);
                    tagRepository.save(tag);
                }
            }
        }
        if(!CollectionUtils.isEmpty(dbPhotoList)){
            for(Photo dbPhoto : dbPhotoList)
                photoRepository.delete(dbPhoto); //todo 아마존s3와 연결시 파일삭제
            post.getPhoto().clear();
        }
        if(!CollectionUtils.isEmpty(files)){
            List<Photo> photoList = photoHandler.parseFileInfo(files);
            if(!photoList.isEmpty()) {
                for(Photo photo : photoList) {
                    post.addPhoto(photoRepository.save(photo));
                }
            }
        }
        post.update(requestDto.toEntity());
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        postRepository.delete(post);
    }
    @Transactional(readOnly = true)
    public Page<PostDto.Response> search(String category,String title,List<String> tags, Pageable pageable) {
        Page<Post> postList = postRepository.findByCategoryAndTitleContains(Category.of(category),title,pageable);
        //todo tag별로 조회를 하든 다 받아와서 처리를 하든 페이징 깨지지않게
        return postList.map(PostDto.Response::new);
    }
}
