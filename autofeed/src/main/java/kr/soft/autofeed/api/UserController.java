package kr.soft.autofeed.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kr.soft.autofeed.user.dto.UserAccountUpdateDTO;
import kr.soft.autofeed.user.dto.UserLoginDTO;
import kr.soft.autofeed.user.dto.UserProfileUpdateDTO;
import kr.soft.autofeed.user.dto.UserRegistDTO;
import kr.soft.autofeed.user.service.UserService;
import kr.soft.autofeed.util.ResponseData;
import kr.soft.autofeed.util.TextClassifier;
import lombok.RequiredArgsConstructor;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;

    @PostMapping("/search")
    public ResponseEntity<ResponseData> getSearchBox(@RequestParam("inputStr") String inputStr) {
        ResponseData responseData = userService.getSearchBox(inputStr);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("page/count/follow")
    public ResponseEntity<ResponseData> getUserFollowCount(@RequestParam("userIdx") Long userIdx) {
        ResponseData responseData = userService.getUserFollowCount(userIdx);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("page/list/following")
    public ResponseEntity<ResponseData> getUserFollowing(@RequestParam("userIdx") Long userIdx) {
        ResponseData responseData = userService.getUserFollowingList(userIdx);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("page/list/follower")
    public ResponseEntity<ResponseData> getUserFollower(@RequestParam("userIdx") Long userIdx) {
        ResponseData responseData = userService.getUserFollowerList(userIdx);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("page/media-threads")
    public ResponseEntity<ResponseData> getUserMediaThreads(@RequestParam("userIdx") Long userIdx) {
        ResponseData responseData = userService.getUserMediaThreads(userIdx);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("page/replies")
    public ResponseEntity<ResponseData> getUserReplies(@RequestParam("userIdx") Long userIdx) {
        ResponseData responseData = userService.getUserReplies(userIdx);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("page/threads")
    public ResponseEntity<ResponseData> getUserThreads(@RequestParam("userIdx") Long userIdx) {
        ResponseData responseData = userService.getUserThreads(userIdx);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("page/profile")
    public ResponseEntity<ResponseData> getUserProfile(@RequestParam("userIdx") Long userIdx) {
        ResponseData responseData = userService.getUserProfile(userIdx);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/check/email-phone")
    public ResponseEntity<ResponseData> emailPhoneCheck(@RequestParam("inputEmailPhone") String inputEmailPhone) {
        ResponseData responseData = userService.emailPhoneCheck(inputEmailPhone);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/check/id")
    public ResponseEntity<ResponseData> userIdCheck(@RequestParam("inputUserId") String inputUserId) {
        ResponseData responseData = userService.userIdCheck(inputUserId);
        return ResponseEntity.ok(responseData);
    }

    // 회원가입
    @PostMapping("/regist")
    public ResponseEntity<ResponseData> regist(@RequestBody UserRegistDTO userRegistDTO) {
        logger.info(userRegistDTO.toString());
        ResponseData responseData = userService.regist(userRegistDTO);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseData> postMethodName(HttpServletResponse response) {
        // userIdx 쿠키 삭제 (만료)
        Cookie cookie = new Cookie("userIdx", null);
        cookie.setPath("/"); // 로그인 때 설정한 path와 동일하게
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
        return ResponseEntity.ok(ResponseData.success());
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseData> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) {
        logger.info(userLoginDTO.toString());
        ResponseData responseData = userService.login(userLoginDTO);

        if (responseData.getCode() == 200 && responseData.getData() != null) {
            Long userIdx = Long.valueOf(responseData.getData().toString());
            Cookie cookie = new Cookie("userIdx", userIdx.toString());
            cookie.setPath("/"); // 전체 경로에 적용
            cookie.setHttpOnly(true); // JS에서 접근 못하도록
            cookie.setMaxAge(60 * 60 * 24 * 7); // 7일
            response.addCookie(cookie);
        }

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseData> delete(@RequestParam("userIdx") Long userIdx) {
        ResponseData responseData = userService.delete(userIdx);

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/update/account")
    public ResponseEntity<ResponseData> accountUpdate(@RequestBody UserAccountUpdateDTO userAccountUpdateDTO) {
        ResponseData responseData = userService.accountUpdate(userAccountUpdateDTO);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/update/profile")
    public ResponseEntity<ResponseData> profileUpdate(MultipartHttpServletRequest request) throws IOException {
        Long userIdx = Long.parseLong(request.getParameter("userIdx"));
        String userId = request.getParameter("userId");
        String bio = request.getParameter("bio");

        String[] hashtagName = request.getParameterValues("hashtagName");
        List<String> hashtagList = (hashtagName != null) ? Arrays.asList(hashtagName) : List.of();

        String privateCheckStr = request.getParameter("privateCheck");
        Boolean privateCheck = null;
        if (privateCheckStr != null) {
            privateCheck = Boolean.valueOf(privateCheckStr);
        }

        MultipartFile profileImage = request.getFile("profileImage");

        UserProfileUpdateDTO userProfileUpdateDTO = new UserProfileUpdateDTO(userIdx, userId, bio, hashtagList,
                privateCheck, profileImage);

        ResponseData responseData = userService.profileUpdate(userProfileUpdateDTO);

        return ResponseEntity.ok(responseData);
    }
}
