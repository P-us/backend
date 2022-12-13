package projectus.pus.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectus.pus.post.dto.CommentDto;
import projectus.pus.post.entity.Comment;
import projectus.pus.post.entity.NestedComment;
import projectus.pus.post.entity.Post;
import projectus.pus.user.entity.User;
import projectus.pus.post.repository.CommentRepository;
import projectus.pus.post.repository.NestedCommentRepository;
import projectus.pus.post.repository.PostRepository;
import projectus.pus.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NestedCommentRepository nestedCommentRepository;
    @Transactional
    public Long addComment(CommentDto.Request requestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("글이 존재하지 않습니다."));
        Comment comment = new Comment(requestDto.getContent(), user, post);
        return commentRepository.save(comment).getId();
    }
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        if(comment.getUser().getId().equals(userId)) {
            commentRepository.delete(comment);
        }
    }
    @Transactional
    public void updateComment(Long commentId, CommentDto.Request requestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        if(comment.getUser().getId().equals(userId)) {
            comment.update(requestDto.getContent());
        }
    }
    @Transactional
    public Long addNestedComment(CommentDto.NestedRequest requestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        Comment comment = commentRepository.findById(requestDto.getCommentId()).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        NestedComment nestedComment = new NestedComment(requestDto.getContent(), user, comment);
        return nestedCommentRepository.save(nestedComment).getId();
    }
    @Transactional(readOnly = true)
    public CommentDto.NestedResponse getNestedComment(Long nestedCommentId){
        NestedComment nestedComment = nestedCommentRepository.findById(nestedCommentId).orElseThrow(
                () -> new IllegalArgumentException("대댓글이 존재하지 않습니다."));
        return new CommentDto.NestedResponse(nestedComment);
    }
    @Transactional
    public void deleteNestedComment(Long nestedCommentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        NestedComment nestedComment = nestedCommentRepository.findById(nestedCommentId).orElseThrow(
                () -> new IllegalArgumentException("대댓글이 존재하지 않습니다."));
        if(nestedComment.getUser().getId().equals(userId)) {
            nestedCommentRepository.delete(nestedComment);
        }
    }
    @Transactional
    public void updateNestedComment(Long nestedCommentId, CommentDto.NestedRequest requestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        NestedComment nestedComment = nestedCommentRepository.findById(nestedCommentId).orElseThrow(
                () -> new IllegalArgumentException("대댓글이 존재하지 않습니다."));
        if(nestedComment.getUser().getId().equals(userId)) {
            nestedComment.update(requestDto.getContent());
        }
    }
    @Transactional(readOnly = true)
    public Page<CommentDto.Response> getCommentList(Long postId, Pageable pageable){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("글이 존재하지 않습니다."));
        List<Comment> commentList = commentRepository.findAllByPostId(postId);
        List<CommentDto.Response> collect = commentList
                .stream()
                .map(comment -> new CommentDto.Response(comment,nestedCommentRepository.findAllByCommentId(comment.getId())))
                .collect(Collectors.toList());
        int start = (int)pageable.getOffset();
        int end = Math.min((start+pageable.getPageSize()),collect.size());
        return new PageImpl<>(collect.subList(start, end), pageable, collect.size());
    }
}
