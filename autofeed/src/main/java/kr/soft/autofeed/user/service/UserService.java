package kr.soft.autofeed.user.service;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.soft.autofeed.UserHashtag.dao.UserHashtagRepository;
import kr.soft.autofeed.domain.Hashtag;
import kr.soft.autofeed.domain.User;
import kr.soft.autofeed.domain.UserHashtag;
import kr.soft.autofeed.domain.UserHashtagId;
import kr.soft.autofeed.hashtag.dao.HashtagRepository;
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
    private final HashtagRepository hashtagRepository;
    private final UserHashtagRepository userHashtagRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass());

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

        return ResponseData.success("회원가입성공");
    }

    @Transactional
    public ResponseData login(UserLoginDTO userLoginDTO) {

        if (!userRepository.existsByEmailPhone(userLoginDTO.getInputId())) {

            return ResponseData.error(400, "아이디가 존재하지 않음!");
        }

        String userPw = userRepository.findPasswordByEmailPhone(userLoginDTO.getInputId());

        if (!userPw.equals(userLoginDTO.getInputPw())) {
            return ResponseData.error(400, "비밀번호가 서로 다름!");
        }

        return ResponseData.success("로그인 성공!");

    }

    @Transactional
    public void accountUpdate(UserAccountUpdateDTO userAccountUpdateDTO) {
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

    }

    @Transactional
    public void profileUpdate(UserProfileUpdateDTO userProfileUpdateDTO) throws IOException {
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
        user.setPrivateCheck(userProfileUpdateDTO.isPrivateCheck());

        if (userProfileUpdateDTO.getProfileImage() != null &&
                !userProfileUpdateDTO.getProfileImage().isEmpty()) {

            String userProfileUrl = UserProfileUtil.saveImage(userProfileUpdateDTO.getProfileImage(),
                    userProfileUpdateDTO.getUserIdx());
            user.setProfileImage(userProfileUrl);
        }

        // 기존 해시태그 전부 soft delete
        userHashtagRepository.findAllByUserUserIdx(user.getUserIdx())
                .forEach(uh -> uh.setDelCheck(true));
        if (userProfileUpdateDTO.getHashtagName() != null) {
            for (String hashtagName : userProfileUpdateDTO.getHashtagName()) {
                Hashtag hashtag = hashtagRepository.findByHashtagName(hashtagName)
                        .orElseThrow(() -> new IllegalArgumentException("해당 해쉬태그가 없습니다."));

                UserHashtagId userHashtagId = new UserHashtagId(user.getUserIdx(), hashtag.getHashtagIdx());

                UserHashtag userHashtag = userHashtagRepository.findById(userHashtagId)
                        .map(existing -> {
                            existing.setDelCheck(false);
                            return existing;
                        })
                        .orElseGet(() -> UserHashtag.builder()
                                .userHashtagId(userHashtagId)
                                .user(user)
                                .hashtag(hashtag)
                                .build());

                userHashtagRepository.save(userHashtag);

            }
        }

    }
}
