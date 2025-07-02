package kr.soft.autofeed.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginDTO {
    private String inputId;
    private String inputPw;
}
