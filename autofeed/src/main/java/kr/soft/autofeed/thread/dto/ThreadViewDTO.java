package kr.soft.autofeed.thread.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreadViewDTO {
    private Long threadIdx;
    private String profileImage;
    private String userId;
    private String content;
    private String fileUrls;
    private Integer likeCount;
    private Integer commentCount;

    // getter 생략 가능 (Lombok 사용 시 @Getter)
}


