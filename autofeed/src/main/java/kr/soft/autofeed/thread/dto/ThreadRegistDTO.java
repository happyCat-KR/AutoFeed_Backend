package kr.soft.autofeed.thread.dto;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThreadRegistDTO {
    private long userIdx;
    private String content;
    private List<String> hashtagName;
    private List<MultipartFile> postImages; 

}
