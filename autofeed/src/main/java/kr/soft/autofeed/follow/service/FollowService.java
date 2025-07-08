package kr.soft.autofeed.follow.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.soft.autofeed.domain.Follow;
import kr.soft.autofeed.domain.FollowId;
import kr.soft.autofeed.domain.User;
import kr.soft.autofeed.follow.dao.FollowRepository;
import kr.soft.autofeed.follow.dto.FollowDTO;
import kr.soft.autofeed.user.dao.UserRepository;
import kr.soft.autofeed.util.ResponseData;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional
    public ResponseData followRegist(FollowDTO followRegistDTO){
        User follower = userRepository.findById(followRegistDTO.getFollowerIdx())
                .orElseThrow(() -> new IllegalArgumentException("팔로우 요청 유저가 존재하지 않습니다."));

        User following = userRepository.findById(followRegistDTO.getFollowingIdx())
                .orElseThrow(() -> new IllegalArgumentException("팔로우 대상 유저가 존재하지 않습니다."));

        FollowId followId = new FollowId(followRegistDTO.getFollowerIdx(), followRegistDTO.getFollowingIdx());

        Follow follow = followRepository.findById(followId).map(existing -> {
            if(!existing.isDelCheck()){
                throw new IllegalArgumentException("이미 팔로우한 유저입니다.");
            }
            existing.setDelCheck(false);    // 언팔 상태에서 재팔로우
            return existing;
        }).orElseGet(() -> Follow.builder()
                .followId(followId)
                .follower(follower)
                .following(following)
                .build());

        followRepository.save(follow);

        return ResponseData.success();
    }

    @Transactional
    public ResponseData followCancel(FollowDTO followCancelDTO){

        FollowId followId = new FollowId(followCancelDTO.getFollowerIdx(), followCancelDTO.getFollowingIdx());

        Follow follow = followRepository.findById(followId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 관계가 존재하지 않습니다."));
        
        if (follow.isDelCheck()) {
            throw new IllegalArgumentException("이미 언팔된 유저입니다.");
        }

        follow.setDelCheck(true);

        return ResponseData.success();
    }

}
