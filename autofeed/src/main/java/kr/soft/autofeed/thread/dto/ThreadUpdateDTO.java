package kr.soft.autofeed.thread.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreadUpdateDTO {
    private Long threadIdx;
    private String content;
    private List<String> hashtagName;
    private List<MultipartFile> threadImages;
}
