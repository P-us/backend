package projectus.pus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projectus.pus.config.security.CurrentUser;
import projectus.pus.config.security.CustomUserDetails;
import projectus.pus.dto.PostDto;
import projectus.pus.service.LikesService;
import projectus.pus.service.PostService;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private final PostService postService;
    @Autowired
    private final LikesService likesService;

    @PostMapping
    public ResponseEntity<Void> addPost(
            @Validated @RequestPart(value = "photo",required = false) List<MultipartFile> files,
            @RequestPart(value="requestDto") PostDto.Request requestDto, @CurrentUser CustomUserDetails currentUser, Errors errors) throws Exception {
        Long postId = postService.addPost(requestDto, files, currentUser.getUserId());
        return ResponseEntity.created(URI.create("/api/posts/"+postId)).build();
    }
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto.Response> getDetailPost(@PathVariable Long postId,@RequestParam Long userId ) { //todo userId jwt 바꾸기
        return ResponseEntity.ok().body(postService.getDetailPost(postId,userId));
    }
    @GetMapping("/image/{photoId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long photoId) throws IOException {
        return ResponseEntity.ok().body(postService.getImage(photoId));
    }
    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @RequestPart(value = "photo",required = false) List<MultipartFile> files,
            @RequestPart(value="requestDto") PostDto.Request requestDto,
            @CurrentUser CustomUserDetails currentUser) throws Exception {
        postService.updatePost(postId, requestDto, files,currentUser.getUserId());
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, @CurrentUser CustomUserDetails currentUser) {
        postService.deletePost(postId,currentUser.getUserId());
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public ResponseEntity<Page<PostDto.Response>> search(
            @RequestParam String category, @RequestParam String title, @RequestParam List<String> tag, @RequestParam Long userId,  //todo userId jwt 바꾸기
            @PageableDefault(sort="modifiedDate",direction = Sort.Direction.DESC)Pageable pageable) {
        return ResponseEntity.ok().body(postService.search(category,title,tag,userId, pageable));
    }

    @GetMapping("/likes/{postId}")
    public ResponseEntity<Void> likes(@PathVariable Long postId, @CurrentUser CustomUserDetails currentUser){
        likesService.addLikes(postId,currentUser.getUserId());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/dislikes/{postId}")
    public ResponseEntity<Void> dislikes(@PathVariable Long postId, @CurrentUser CustomUserDetails currentUser){
        likesService.deleteLikes(postId,currentUser.getUserId());
        return ResponseEntity.ok().build();
    }
}
