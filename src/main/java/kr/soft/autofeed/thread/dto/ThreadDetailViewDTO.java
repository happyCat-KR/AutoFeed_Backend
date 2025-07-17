package kr.soft.autofeed.thread.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreadDetailViewDTO {
    private ThreadViewDTO thread;
    private List<ThreadViewDTO> replies;
}
