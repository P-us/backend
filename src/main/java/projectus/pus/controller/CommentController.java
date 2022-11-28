package projectus.pus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projectus.pus.service.CommentService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {
//    @Autowired
//    private final CommentService commentService;

    //Post  /{postId}/{userId} addComment
    //Delete /{commentId} deleteComment
    //Post  /{commentId} updateComment
    //Get /{commentId} getComment

    //Post  /{commentId}  addNestedComment
    //Delete  /{nestedCommentId}  deleteNestedComment
    //Post  /{nestedCommentId}  updateNestedComment
    //Get /{nestedCommentId} getNestedComment

    //Get /{postId} getCommentList
}
