package kr.soft.autofeed.text_classifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TextClassifierController {

    @Autowired
    private TextClassifierService classifierService;

    @PostMapping("/classify")
    public String classify(@RequestBody String sentence) {
        return classifierService.classifyText(sentence);
    }
}
