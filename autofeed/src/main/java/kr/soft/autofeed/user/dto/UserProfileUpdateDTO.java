package kr.soft.autofeed.user.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateDTO {

    private Long userIdx;
    private String userId;
    private String bio;
    private List<String> hashtagName;
    private boolean privateCheck;
    private MultipartFile profileImage;
}
