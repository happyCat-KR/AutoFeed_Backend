package kr.soft.autofeed.user.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegistDTO {
    private String userName;
    private String emailPhone;
    private String password;
    private String userId;

}
