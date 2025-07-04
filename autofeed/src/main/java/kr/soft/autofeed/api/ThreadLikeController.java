package kr.soft.autofeed.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.soft.autofeed.threadLike.dto.LikeCancelDTO;
import kr.soft.autofeed.threadLike.dto.LikeRegistDTO;
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

    @PostMapping("/regist")
    public void LikeRegist(@RequestBody LikeRegistDTO likeRegistDTO){
        threadLikeService.LikeRegist(likeRegistDTO);
    }

    @PostMapping("/cancel")
    public void LikeCancel(@RequestBody LikeCancelDTO likeCancelDTO){
        threadLikeService.LikeCancel(likeCancelDTO);
    }
}
