package projectus.pus.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import projectus.pus.post.dto.PostDto;
import projectus.pus.post.entity.Post;
import projectus.pus.vote.entity.Items;
import projectus.pus.vote.entity.Vote;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VoteDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{
        @NotBlank(message = "제목은 필수 입력 값입니다.")
        private String title;
        private List<ItemsRequest> items;

        public Vote toEntity() {
            return Vote.builder()
                    .title(title)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemsRequest{
        private String field;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private Long voteId;
        private String title;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
        private List<ItemsResponse> items;
        private Long checkCount;

        public Response(Vote vote, List<ItemsResponse> items, ResultResponse resultResponse) {
            this.voteId = vote.getId();
            this.title = vote.getTitle();
            this.createdDate = vote.getCreatedDate();
            this.modifiedDate = vote.getModifiedDate();
            this.items = items;
            this.checkCount = resultResponse.getCheckCount();
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ItemsResponse{
            private String field;
        }
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ResultResponse{
            private long checkCount;
        }
    }



}
