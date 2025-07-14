package kr.soft.autofeed.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileViewDTO {
    private Long userIdx;
    private String userName;
    private String userId;
    private String bio;
    private String userHashtag;
    private String profileImage;
    private Integer followerCount;
}
