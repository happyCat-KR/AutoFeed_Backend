package kr.soft.autofeed.post.dto;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRegistDTO {
    private long userIdx;
    private String content;
    private List<String> hashtagName;


}
