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

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;

    // 문장단어 추출 및 카테고리 분류
    @PostMapping("/classify")
    public String classify(@RequestBody String sentence) {
        return TextClassifier.classifyText(sentence);
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

    @PostMapping("/update/account")
    public void accountUpdate(@RequestBody UserAccountUpdateDTO userAccountUpdateDTO) {
        userService.accountUpdate(userAccountUpdateDTO);
    }

    @PostMapping("/update/profile")
    public void profileUpdate(MultipartHttpServletRequest request) throws IOException{
        Long userIdx = Long.parseLong(request.getParameter("userIdx"));
        String userId = request.getParameter("userId");
        String bio = request.getParameter("bio");

        String[] hashtagName = request.getParameterValues("hashtagName");
        List<String> hashtagList = (hashtagName != null) ? Arrays.asList(hashtagName) : List.of();

        boolean privateCheck = Boolean.parseBoolean(request.getParameter("privateCheck"));
        MultipartFile profileImage = request.getFile("profileImage");

        UserProfileUpdateDTO userProfileUpdateDTO = new UserProfileUpdateDTO(userIdx, userId, bio, hashtagList, privateCheck, profileImage);

        userService.profileUpdate(userProfileUpdateDTO);
    }
}
