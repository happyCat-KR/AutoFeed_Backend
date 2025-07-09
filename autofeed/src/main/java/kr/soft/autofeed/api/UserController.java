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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;

    // 문장단어 추출 및 카테고리 분류
    @PostMapping("/classify")
    public ResponseEntity<ResponseData> classify(@RequestBody String sentence) {
        ResponseData responseData = TextClassifier.classifyText(sentence);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/check/email-phone")
    public ResponseEntity<ResponseData> emailPhoneCheck(@RequestParam("inputEmailPhone") String inputEmailPhone){
        ResponseData responseData = userService.emailPhoneCheck(inputEmailPhone);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/check/id")
    public ResponseEntity<ResponseData> userIdCheck(@RequestParam("inputUserId") String inputUserId){
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

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseData> login(@RequestBody UserLoginDTO userLoginDTO) {
        logger.info(userLoginDTO.toString());
        ResponseData responseData = userService.login(userLoginDTO);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseData> delete(@RequestParam("userIdx") Long userIdx){
        ResponseData responseData = userService.delete(userIdx);

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/update/account")
    public ResponseEntity<ResponseData> accountUpdate(@RequestBody UserAccountUpdateDTO userAccountUpdateDTO) {
        ResponseData responseData = userService.accountUpdate(userAccountUpdateDTO);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/update/profile")
    public ResponseEntity<ResponseData> profileUpdate(MultipartHttpServletRequest request) throws IOException{
        Long userIdx = Long.parseLong(request.getParameter("userIdx"));
        String userId = request.getParameter("userId");
        String bio = request.getParameter("bio");

        String[] hashtagName = request.getParameterValues("hashtagName");
        List<String> hashtagList = (hashtagName != null) ? Arrays.asList(hashtagName) : List.of();

        String privateCheckStr = request.getParameter("privateCheck");
        Boolean privateCheck = null;
        if(privateCheckStr != null){
            privateCheck = Boolean.valueOf(privateCheckStr);
        }

        MultipartFile profileImage = request.getFile("profileImage");

        UserProfileUpdateDTO userProfileUpdateDTO = new UserProfileUpdateDTO(userIdx, userId, bio, hashtagList, privateCheck, profileImage);

        ResponseData responseData = userService.profileUpdate(userProfileUpdateDTO);

        return ResponseEntity.ok(responseData);
    }
}
