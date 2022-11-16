package projectus.pus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import projectus.pus.dto.PostDto;
import projectus.pus.service.PostService;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> addPost(@Validated @RequestBody PostDto.Request requestDto, Errors errors) {
        Long postId = postService.addPost(requestDto);
        return ResponseEntity.created(URI.create("/api/posts/"+postId)).build();  //201 코드 반환 + 데이터받아서 처리할필요 x+상세정보 uri
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto.Response> findPost(@PathVariable Long postId) {  //get보다 find가 나은듯
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
        return ResponseEntity.noContent().build(); //별도로 반환해야 할 데이터가 없을경우 +204
    }
}
