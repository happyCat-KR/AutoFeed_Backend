package kr.soft.autofeed.user.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.soft.autofeed.domain.User;
import kr.soft.autofeed.user.dao.UserRepository;
import kr.soft.autofeed.user.dto.UserLoginDTO;
import kr.soft.autofeed.user.dto.UserRegistDTO;
import kr.soft.autofeed.util.ResponseData;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

        if(!userPw.equals(userLoginDTO.getInputPw())){
            return ResponseData.error(400, "비밀번호가 서로 다름!");
        }


        return ResponseData.success("로그인 성공!");

        
    }

}
