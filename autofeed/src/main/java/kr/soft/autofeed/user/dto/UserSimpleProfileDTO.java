package kr.soft.autofeed.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleProfileDTO {
    private Long userIdx;
    private String userId;
    private String userName;
    private String profileImage;
}
