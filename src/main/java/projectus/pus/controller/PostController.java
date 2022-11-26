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
import projectus.pus.dto.PostDto;
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

    @PostMapping
    public ResponseEntity<Void> addPost(
            @Validated @RequestPart(value = "photo",required = false) List<MultipartFile> files,
            @RequestPart(value="requestDto") PostDto.Request requestDto, Errors errors) throws Exception {
        Long postId = postService.addPost(requestDto, files);
        return ResponseEntity.created(URI.create("/api/posts/"+postId)).build();
    }
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto.Response> getDetailPost(@PathVariable Long postId) {
        return ResponseEntity.ok().body(postService.getDetailPost(postId));
    }
    @GetMapping("/image/{photoId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long photoId) throws IOException {
        return ResponseEntity.ok().body(postService.getImage(photoId));
    }
    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @RequestPart(value = "photo",required = false) List<MultipartFile> files,
            @RequestPart(value="requestDto") PostDto.Request requestDto) throws Exception {
        postService.updatePost(postId, requestDto, files);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public ResponseEntity<Page<PostDto.Response>> search(
            @RequestParam String category, @RequestParam String title, @RequestParam List<String> tag,
            @PageableDefault(sort="modifiedDate",direction = Sort.Direction.DESC)Pageable pageable) {
        return ResponseEntity.ok().body(postService.search(category,title,tag, pageable));
    }
}
