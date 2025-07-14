package kr.soft.autofeed.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowCntDTO {
    private int followerCnt;
    private int followingCnt;
}
