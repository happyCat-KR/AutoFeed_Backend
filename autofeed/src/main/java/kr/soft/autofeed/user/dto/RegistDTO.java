package kr.soft.autofeed.user.dto;

import kr.soft.autofeed.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistDTO {
    private String userName;
    private String emailPhone;
    private String password;
    private String userId;

}
