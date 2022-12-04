package projectus.pus.service;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import projectus.pus.dto.PostDto;
import projectus.pus.entity.Photo;
import projectus.pus.entity.Post;
import projectus.pus.entity.User;
import projectus.pus.repository.PhotoRepository;
import projectus.pus.repository.PostRepository;
import projectus.pus.repository.UserRepository;
import projectus.pus.utils.PhotoHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final CategoryService categoryService;
    private final LikesService likesService;
    private final PostRepository postRepository;
    private final PhotoHandler photoHandler;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    @Transactional
    public Long addPost(PostDto.Request requestDto, List<MultipartFile> files, Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다.")
        );
        Post post = requestDto.toEntity();
        post.setUser(user);
        categoryService.addCategory(requestDto,post);
        List<Photo> photoList = photoHandler.parseFileInfo(files); //todo 시간 남는다면 PhotoService 리팩토링
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
        //List<Long> fileId = post.getPhoto().stream().map(Photo::getId).collect(Collectors.toList());
        List<Long> fileId = photoRepository.findAllByPostId(postId)
                 .stream()
                 .map(Photo::getId)
                 .collect(Collectors.toList());
        return new PostDto.Response(post,fileId,categoryService.getCategoryList(postId), likesService.getLikesCount(postId));
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
    @Transactional(readOnly = true)
    public byte[] getImageS3(Long photoId) throws IOException {
        String path = photoRepository.findById(photoId).orElseThrow(
                () -> new IllegalArgumentException("해당 사진이 존재하지 않습니다.")).getFilePath();
        S3ObjectInputStream objectInputStream = photoHandler.getS3InputStream(path);
        byte[] bytes = IOUtils.toByteArray(objectInputStream);
        String fileName = URLEncoder.encode(path, "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);
        return bytes;
    }
    @Transactional
    public void updatePost(Long postId, PostDto.Request requestDto, List<MultipartFile> files, Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다.")
        );
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        if(post.getUser().getId().equals(userId)) {
            categoryService.updateCategory(post, postId, requestDto);
            List<Photo> dbPhotoList = photoRepository.findAllByPostId(postId);
            if (!CollectionUtils.isEmpty(dbPhotoList)) {
                for (Photo dbPhoto : dbPhotoList) {
                    photoRepository.delete(dbPhoto);
                    //            photoHandler.deleteFile(dbPhoto.getFilePath()); //s3
                }
                post.getPhoto().clear();
            }
            if (!CollectionUtils.isEmpty(files)) {
                List<Photo> photoList = photoHandler.parseFileInfo(files);
                if (!photoList.isEmpty()) {
                    for (Photo photo : photoList) {
                        post.addPhoto(photoRepository.save(photo));
                    }
                }
            }
            post.update(requestDto.toEntity());
        }
        else{
            throw new IllegalArgumentException("해당 계정은 글에 대한 권한이 없습니다.");
        }
    }
    @Transactional
    public void deletePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다.")
        );
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        if(post.getUser().getId().equals(userId)){
            postRepository.delete(post);
        }
        else{
            throw new IllegalArgumentException("해당 계정은 글에 대한 권한이 없습니다.");
        }

//        List<Photo> dbPhotoList = photoRepository.findAllByPostId(postId);
//        if(!CollectionUtils.isEmpty(dbPhotoList)){
//            for(Photo dbPhoto : dbPhotoList) {
//                photoHandler.deleteFile(dbPhoto.getFilePath()); //s3
//            }
//        }
    }
    @Transactional(readOnly = true)
    public Page<PostDto.Response> search(String category,String title,List<String> tag, Pageable pageable) {
        List<Long> searchId = categoryService.search(category,tag);
        List<Post> postList;
        if(!searchId.isEmpty()) {
            postList = postRepository.findByIdInAndTitleContains(searchId, title, pageable);
        }
        else {
            if(category==null&&category.isBlank()&& tag.isEmpty()) {
                postList = postRepository.findByTitleContains(title, pageable);
            }
            else{
                postList = new ArrayList<>(); //todo 이거 예외처리 다시 생각
            }
        }
        List<PostDto.Response> collect = postList
                .stream()
                .map(post ->
                        new PostDto.Response(post,categoryService.getCategoryList(post.getId()), likesService.getLikesCount(post.getId()))) //todo postDto에 좋아요 숫자 추가하고, response에 likeService.getLike
                .collect(Collectors.toList());
        int start = (int)pageable.getOffset();
        int end = Math.min((start+pageable.getPageSize()),collect.size());
        return new PageImpl<>(collect.subList(start, end), pageable, collect.size());
    }
}
