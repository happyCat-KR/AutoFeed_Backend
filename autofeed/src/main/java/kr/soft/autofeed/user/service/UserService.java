package kr.soft.autofeed.user.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.soft.autofeed.UserAction.service.UserActionService;
import kr.soft.autofeed.UserHashtag.dao.UserHashtagRepository;
import kr.soft.autofeed.domain.Hashtag;
import kr.soft.autofeed.domain.User;
import kr.soft.autofeed.domain.Thread;
import kr.soft.autofeed.domain.UserHashtag;
import kr.soft.autofeed.domain.UserHashtagId;
import kr.soft.autofeed.follow.dao.FollowRepository;
import kr.soft.autofeed.hashtag.dao.HashtagRepository;
import kr.soft.autofeed.thread.dao.ThreadRepository;
import kr.soft.autofeed.thread.service.ThreadService;
import kr.soft.autofeed.threadLike.dao.ThreadLikeRepository;
import kr.soft.autofeed.user.dao.UserRepository;
import kr.soft.autofeed.user.dto.UserAccountUpdateDTO;
import kr.soft.autofeed.user.dto.UserLoginDTO;
import kr.soft.autofeed.user.dto.UserProfileUpdateDTO;
import kr.soft.autofeed.user.dto.UserRegistDTO;
import kr.soft.autofeed.util.ResponseData;
import kr.soft.autofeed.util.UserProfileUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ThreadRepository threadRepository;
    private final HashtagRepository hashtagRepository;
    private final UserHashtagRepository userHashtagRepository;
    private final FollowRepository followRepository;
    private final ThreadLikeRepository threadLikeRepository;
    private final ThreadService threadService;
    private final UserActionService userActionService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void userCleanInsert() {
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusMinutes(2);
        List<User> users = userRepository.findAllForCleanup(twoHoursAgo);

        for (User user : users) {
            user.setUserName("탈퇴한 사용자");
            user.setEmailPhone("deleted_" + UUID.randomUUID().toString());
            user.setUserId("deleted_user_" + UUID.randomUUID().toString());
            user.setPassword("");
            user.setBio(null);

            userHashtagRepository.findAllByUserUserIdx(user.getUserIdx())
                    .forEach(delHashtag -> delHashtag.setDelCheck(true));

            followRepository.findAllByFollowerUserIdx(user.getUserIdx())
                    .forEach(follower -> follower.setDelCheck(true));

            followRepository.findAllByFollowingUserIdx(user.getUserIdx())
                    .forEach(following -> following.setDelCheck(true));

            threadLikeRepository.findAllByUserUserIdx(user.getUserIdx())
                    .forEach(like -> like.setDelCheck(true));

        }
    }

    // 이메일 or 전화번호 중복체크
    @Transactional
    public ResponseData emailPhoneCheck(String inputEmailPhone) {
        if (userRepository.existsByEmailPhone(inputEmailPhone)) {
            return ResponseData.error(400, "이미 등록된 이메일 또는 전화번호입니다.");
        }
        return ResponseData.success();
    }

    // id 중복체크
    @Transactional
    public ResponseData userIdCheck(String inputUserId) {
        if (userRepository.existsByUserId(inputUserId)) {
            return ResponseData.error(400, "이미 사용 중인 사용자 ID입니다.");
        }

        return ResponseData.success();
    }

    @Transactional
    public ResponseData delete(Long userIdx) {
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        user.setDelCheck(true);
        user.setDelUser(user);
        user.setDeletedAt(LocalDateTime.now());

        threadRepository.findAllByUserUserIdx(userIdx)
                .forEach(thread -> threadService.threadDelete(thread.getThreadIdx()));

        return ResponseData.success();
    }

    @Transactional
    public ResponseData restore(Long userIdx) {
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        user.setDelCheck(false);
        user.setDelUser(null);
        user.setDeletedAt(null);

        threadRepository.findAllByUserUserIdx(userIdx)
                .forEach(thread -> threadService.threadRestore(thread.getThreadIdx()));

        return ResponseData.success();
    }

    @Transactional
    public ResponseData regist(UserRegistDTO userRegistDTO) {

        if (userRepository.existsByEmailPhone(userRegistDTO.getEmailPhone())) {

            return ResponseData.error(400, "이미 등록된 이메일 또는 전화번호입니다.");
        }

        if (userRepository.existsByUserId(userRegistDTO.getUserId())) {
            return ResponseData.error(400, "이미 사용 중인 사용자 ID입니다.");
        }

        User user = new User();
        user.setUserName(userRegistDTO.getUserName());
        user.setEmailPhone(userRegistDTO.getEmailPhone());
        user.setPassword(userRegistDTO.getPassword());
        user.setUserId(userRegistDTO.getUserId());

        userRepository.save(user);

        return ResponseData.success();
    }

    @Transactional
    public ResponseData login(UserLoginDTO userLoginDTO) {

        return userRepository.findByEmailPhone(userLoginDTO.getInputId())
                .map(user -> {
                    if (!user.getPassword().equals(userLoginDTO.getInputPw())) {
                        return ResponseData.error(400, "비밀번호가 서로 다름!");
                    }
                    if (user.getDelCheck()) {
                        return ResponseData.error(400, "탈퇴한 회원입니다.");
                    }

                    return ResponseData.success();
                })
                .orElse(ResponseData.error(400, "아이디가 존재하지 않습니다."));

    }

    @Transactional
    public ResponseData accountUpdate(UserAccountUpdateDTO userAccountUpdateDTO) {
        User user = userRepository.findById(userAccountUpdateDTO.getUserIdx())
                .orElseThrow(() -> new IllegalArgumentException("업데이트할 유저가 존재하지 않습니다."));

        if (userAccountUpdateDTO.getEmailPhone() != null &&
                !userAccountUpdateDTO.getEmailPhone().equals(user.getEmailPhone()) &&
                userRepository.existsByEmailPhone(userAccountUpdateDTO.getEmailPhone())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일 또는 전화번호 입니다.");
        }

        if (userAccountUpdateDTO.getPassword() != null && !userAccountUpdateDTO.getPassword().isBlank()) {
            user.setPassword(userAccountUpdateDTO.getPassword());
        }

        if (userAccountUpdateDTO.getEmailPhone() != null) {
            user.setEmailPhone(userAccountUpdateDTO.getEmailPhone());
        }

        if (userAccountUpdateDTO.getUserName() != null) {
            user.setUserName(userAccountUpdateDTO.getUserName());
        }

        return ResponseData.success();

    }

    @Transactional
    public ResponseData profileUpdate(UserProfileUpdateDTO userProfileUpdateDTO) throws IOException {
        User user = userRepository.findById(userProfileUpdateDTO.getUserIdx())
                .orElseThrow(() -> new IllegalArgumentException("업데이트할 유저가 존재하지 않습니다."));

        if (userProfileUpdateDTO.getUserId() != null &&
                !userProfileUpdateDTO.getUserId().equals(user.getUserId()) &&
                userRepository.existsByUserId(userProfileUpdateDTO.getUserId())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        if (userProfileUpdateDTO.getUserId() != null) {
            user.setUserId(userProfileUpdateDTO.getUserId());
        }
        user.setBio(userProfileUpdateDTO.getBio());

        if (userProfileUpdateDTO.getPrivateCheck() != null) {
            user.setPrivateCheck(userProfileUpdateDTO.getPrivateCheck().booleanValue());
        }

        if (userProfileUpdateDTO.getProfileImage() != null &&
                !userProfileUpdateDTO.getProfileImage().isEmpty()) {

            String userProfileUrl = UserProfileUtil.saveImage(userProfileUpdateDTO.getProfileImage(),
                    userProfileUpdateDTO.getUserIdx());
            user.setProfileImage(userProfileUrl);
        } else {
            user.setProfileImage("http://blog.naver.com/yomyi00/222556494236");
        }

        // 기존 해시태그 전부 soft delete
        List<UserHashtag> existingHashtags = userHashtagRepository.findAllByUserUserIdx(user.getUserIdx());
        Set<String> incomingHashtagSet = new HashSet<>(userProfileUpdateDTO.getHashtagName());

        // 먼저 remove 기록 처리 (DTO에 없는 것들만)
        for (UserHashtag uh : existingHashtags) {
            String name = uh.getHashtag().getHashtagName();
            if (!incomingHashtagSet.contains(name) && !uh.isDelCheck()) {
                userActionService.regist(uh.getUser(), uh.getHashtag(), "user_hashtag_remove");
                uh.setDelCheck(true);
            }
        }

        // 이후 add 처리
        for (String hashtagName : userProfileUpdateDTO.getHashtagName()) {
            Hashtag hashtag = hashtagRepository.findByHashtagName(hashtagName)
                    .orElseThrow(() -> new IllegalArgumentException("해당 해쉬태그가 없습니다."));

            UserHashtagId userHashtagId = new UserHashtagId(user.getUserIdx(), hashtag.getHashtagIdx());

            UserHashtag userHashtag = userHashtagRepository.findById(userHashtagId)
                    .map(existing -> {
                        if (existing.isDelCheck()) {
                            existing.setDelCheck(false);
                            userActionService.regist(user, hashtag, "user_hashtag_add");
                        }
                        return existing;
                    })
                    .orElseGet(() -> {
                        UserHashtag newOne = UserHashtag.builder()
                                .userHashtagId(userHashtagId)
                                .user(user)
                                .hashtag(hashtag)
                                .build();
                        userActionService.regist(user, hashtag, "user_hashtag_add");
                        return newOne;
                    });

            userHashtagRepository.save(userHashtag);
        }
        return ResponseData.success();

    }
}
