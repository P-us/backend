package projectus.pus.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import projectus.pus.post.entity.Comment;
import projectus.pus.post.entity.NestedComment;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{
        private String content;
        private Long postId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NestedRequest{
        private String content;
        private Long commentId;
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response { //todo 글쓴이인지 여부
        private Long commentId;
        private String content;
        private String userName;
        private List<NestedResponse> nestedResponse;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
        public Response(Comment entity, List<NestedResponse> nestedResponse) {
            this.commentId = entity.getId();
            this.content = entity.getContent();
            this.userName = entity.getUser().getUserName();
            this.createdDate = entity.getCreatedDate();
            this.modifiedDate = entity.getModifiedDate();
            this.nestedResponse = nestedResponse;
        }
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NestedResponse { //todo 글쓴이인지 여부
        private Long nestedCommentId;
        private String userName;
        private String content;
        private Long commentId ;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
        public NestedResponse(NestedComment entity) {
            this.nestedCommentId = entity.getId();
            this.userName = entity.getUser().getUserName();
            this.content = entity.getContent();
            this.commentId = entity.getComment().getId();
            this.createdDate = entity.getCreatedDate();
            this.modifiedDate = entity.getModifiedDate();
        }
    }
}
