package kr.soft.autofeed.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kr.soft.autofeed.user.dto.UserLoginDTO;
import kr.soft.autofeed.user.dto.UserRegistDTO;
import kr.soft.autofeed.user.service.UserService;
import kr.soft.autofeed.util.ResponseData;
import kr.soft.autofeed.util.TextClassifier;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;

    @PostMapping("/classify")
    public String classify(@RequestBody String sentence) {
        return TextClassifier.classifyText(sentence);
    }

    @PostMapping("/regist")
    public ResponseEntity<ResponseData> regist(@RequestBody UserRegistDTO userRegistDTO) {
        logger.info(userRegistDTO.toString());
        ResponseData responseData = userService.regist(userRegistDTO);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseData> login(@RequestBody UserLoginDTO userLoginDTO) {
        logger.info(userLoginDTO.toString());
        ResponseData responseData = userService.login(userLoginDTO);
        return ResponseEntity.ok(responseData);
    }
    
}
