package kr.soft.autofeed.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.soft.autofeed.follow.dto.FollowDTO;
import kr.soft.autofeed.follow.service.FollowService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/follow")
@RequiredArgsConstructor
public class FollowController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final FollowService followService;

    // 팔로우
    @PostMapping("/regist")
    public void followRegist(@RequestBody FollowDTO followRegistDTO){
        followService.followRegist(followRegistDTO);
    }

    // 언팔로우
    @PostMapping("/cancel")
    public void followCancel(@RequestBody FollowDTO followCancelDTO){
        followService.followCancel(followCancelDTO);
    }
    
}
