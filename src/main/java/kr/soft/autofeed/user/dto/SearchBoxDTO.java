package kr.soft.autofeed.user.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchBoxDTO {
    private List<String> hashtagList;
    private List<UserSimpleProfileDTO> userList;
}
