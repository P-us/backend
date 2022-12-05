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
import projectus.pus.dto.CommentDto;
import projectus.pus.dto.PostDto;
import projectus.pus.service.CommentService;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<Void> addComment(
            @RequestBody CommentDto.Request requestDto, @CurrentUser CustomUserDetails currentUser) throws Exception {
        Long commentId = commentService.addComment(requestDto, currentUser.getUserId());
        return ResponseEntity.created(URI.create("/api/comments/"+commentId)).build();
    }
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId, @CurrentUser CustomUserDetails currentUser) throws Exception {
        commentService.deleteComment(commentId, currentUser.getUserId());
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentDto.Request requestDto, @CurrentUser CustomUserDetails currentUser) throws Exception {
        commentService.updateComment(commentId, requestDto, currentUser.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comments/nested")
    public ResponseEntity<Void> addNestedComment(
            @RequestBody CommentDto.NestedRequest requestDto, @CurrentUser CustomUserDetails currentUser) throws Exception {
        Long commentId = commentService.addNestedComment(requestDto, currentUser.getUserId());
        return ResponseEntity.created(URI.create("/api/comments/"+commentId)).build();
    }
    @GetMapping("/comments/nested/{nestedCommentId}")
    public ResponseEntity<CommentDto.NestedResponse> getNestedComment(@PathVariable Long nestedCommentId){
        return ResponseEntity.ok().body(commentService.getNestedComment(nestedCommentId));
    }
    @DeleteMapping("/comments/nested/{nestedCommentId}")
    public ResponseEntity<Void> deleteNestedComment(
            @PathVariable Long nestedCommentId, @CurrentUser CustomUserDetails currentUser) throws Exception {
        commentService.deleteNestedComment(nestedCommentId, currentUser.getUserId());
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/comments/nested/{nestedCommentId}")
    public ResponseEntity<Void> updateNestedComment(
            @PathVariable Long nestedCommentId,
            @RequestBody CommentDto.NestedRequest requestDto, @CurrentUser CustomUserDetails currentUser) throws Exception {
        commentService.updateNestedComment(nestedCommentId, requestDto, currentUser.getUserId());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Page<CommentDto.Response>> getCommentList(
            @PathVariable Long postId,
            @PageableDefault(sort="modifiedDate",direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok().body(commentService.getCommentList(postId,pageable));
    }
}
