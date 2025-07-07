package kr.soft.autofeed.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.soft.autofeed.threadLike.dto.LikeDTO;
import kr.soft.autofeed.threadLike.service.ThreadLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class ThreadLikeController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    final private ThreadLikeService threadLikeService;

    // 좋아요
    @PostMapping("/regist")
    public void LikeRegist(@RequestBody LikeDTO likeRegistDTO){
        threadLikeService.LikeRegist(likeRegistDTO);
    }

    // 좋아요취소
    @PostMapping("/cancel")
    public void LikeCancel(@RequestBody LikeDTO likeCancelDTO){
        threadLikeService.LikeCancel(likeCancelDTO);
    }
}
