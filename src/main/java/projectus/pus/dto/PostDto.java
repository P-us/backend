package projectus.pus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import projectus.pus.entity.Post;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class PostDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{
        @NotBlank(message = "제목은 필수 입력 값입니다.")
        private String title;
        private String content;
        //todo user
        private List<CategoryRequest> category;
        public Post toEntity(){
            return Post.builder()
                    .title(title)
                    .content(content)
                    .build();
        }
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryRequest{
        private String field;
        private List<String> tag;
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private Long postId;
        private String title;
        private String content;
        //user
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
        private List<CategoryResponse> category;
        private List<Long> photoId;
        private Long likeCount;
        private boolean check;
        public Response(Post entity, List<CategoryResponse> category,LikeResponse likeResponse){
            this.postId = entity.getId();
            this.title = entity.getTitle();
            this.content = entity.getContent();
            this.createdDate = entity.getCreatedDate();
            this.modifiedDate = entity.getModifiedDate();
            this.category = category;
            this.likeCount = likeResponse.getLikeCount();
        }

        public Response(Post entity, List<Long> photoId, List<CategoryResponse> category, LikeResponse likeResponse){
            this.title = entity.getTitle();
            this.content = entity.getContent();
            this.createdDate = entity.getCreatedDate();
            this.modifiedDate = entity.getModifiedDate();
            this.category = category;
            this.photoId = photoId;
            this.likeCount = likeResponse.getLikeCount();
            this.check = likeResponse.isCheck();
        }
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryResponse{
        private String field;
        private List<String> tag;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikeResponse{
        private long likeCount;
        private boolean check;
    }
}
