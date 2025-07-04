package kr.soft.autofeed.threadLike.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeRegistDTO {
    private Long userIdx;
    private Long threadIdx;
}
