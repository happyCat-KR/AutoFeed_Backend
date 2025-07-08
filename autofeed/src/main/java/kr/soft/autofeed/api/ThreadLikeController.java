package kr.soft.autofeed.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.soft.autofeed.threadLike.dto.LikeDTO;
import kr.soft.autofeed.threadLike.service.ThreadLikeService;
import kr.soft.autofeed.util.ResponseData;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseData> LikeRegist(@RequestBody LikeDTO likeRegistDTO){
        ResponseData responseData = threadLikeService.LikeRegist(likeRegistDTO);
    
        return ResponseEntity.ok(responseData);
    }

    // 좋아요취소
    @PostMapping("/cancel")
    public ResponseEntity<ResponseData> LikeCancel(@RequestBody LikeDTO likeCancelDTO){
        ResponseData responseData = threadLikeService.LikeCancel(likeCancelDTO);

        return ResponseEntity.ok(responseData);
    }
}
