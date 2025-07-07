package kr.soft.autofeed.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountUpdateDTO {
    private Long userIdx;
    private String password;
    private String emailPhone;
    private String userName;
}
