package projectus.pus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projectus.pus.dto.PostDto;
import projectus.pus.service.PostService;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> addPost(
            @Validated @RequestPart(value = "photo",required = false) List<MultipartFile> files,
            @RequestPart(value="requestDto") PostDto.Request requestDto, Errors errors) throws Exception {
        Long postId = postService.addPost(requestDto, files);
        return ResponseEntity.created(URI.create("/api/posts/"+postId)).build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto.Response> findPost(@PathVariable Long postId) {
        return ResponseEntity.ok().body(postService.findPost(postId));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId, @RequestBody PostDto.Request requestDto) {
        postService.updatePost(postId, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    //todo 게시판 전체 조회, 검색
}
