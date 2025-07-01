package kr.soft.autofeed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import kr.soft.autofeed.user.dto.RegistDTO;
import kr.soft.autofeed.util.TextClassifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class ApiController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/classify")
    public String classify(@RequestBody String sentence) {
        return TextClassifier.classifyText(sentence);
    }

    @PostMapping("/regist")
    public void regist(@RequestBody RegistDTO registDTO) {
        logger.info(registDTO.getUserId());
        
    }
    
}
