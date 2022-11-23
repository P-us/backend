package projectus.pus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import projectus.pus.entity.Category;
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
        private String category;
        private List<String> tag;
        public Post toEntity(){
            return Post.builder()
                    .title(title)
                    .content(content)
                    .category(Category.of(category))
                    .build();
        }
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private String title;
        private String content;
        //user
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
        private String category;
        private List<String> tag;
        private List<Long> photoId;
        public Response(Post entity){
            this.title = entity.getTitle();
            this.content = entity.getContent();
            this.createdDate = entity.getCreatedDate();
            this.modifiedDate = entity.getModifiedDate();
            this.category = String.valueOf(entity.getCategory());
     //       this.tag = entity.getTag()
     //               .stream()
     //               .map(Tag::getName)
     //               .collect(Collectors.toList());
        }

        public Response(Post entity, List<Long> photoId,List<String> tag){
            this.title = entity.getTitle();
            this.content = entity.getContent();
            this.createdDate = entity.getCreatedDate();
            this.modifiedDate = entity.getModifiedDate();
            this.category = String.valueOf(entity.getCategory());
            this.tag = tag;
            this.photoId = photoId;
        }
    }
}
