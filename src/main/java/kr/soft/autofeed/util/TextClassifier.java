package kr.soft.autofeed.util;

import kr.soft.autofeed.domain.Hashtag;
import kr.soft.autofeed.hashtag.dao.HashtagRepository;
import kr.soft.autofeed.hashtag.service.HashtagService;
import kr.soft.autofeed.relatedWord.dao.RelatedWordRepository;
import kr.soft.autofeed.relatedWord.service.RelatedWordService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TextClassifier {

    private final HashtagService hashtagService;
    private final RelatedWordService relatedWordService;
    private final HashtagRepository hashtagRepository;


    public String classifyText(String sentence) {
        // DB에 저장된 카테고리명 리스트 가져오기 (해시태그 이름)
        List<String> categories = hashtagService.findAllCategoryNames();

        Map<String, Integer> scores = new HashMap<>();

        for (String category : categories) {
            // 카테고리명에 해당하는 관련 단어 리스트 가져오기
            Hashtag hashtag = hashtagRepository.findByHashtagName(category)
                    .orElseThrow(() -> new IllegalArgumentException("해시태그가 존재하지 않습니다: " + category));

            List<String> relatedWords = relatedWordService.getWordsByHashtagIdx(hashtag.getHashtagIdx());

            int score = 0;
            for (String word : relatedWords) {
                if (sentence.contains(word)) {
                    score++;
                }
            }
            scores.put(category, score);
        }

        String bestCategory = "분류불가";
        int maxScore = 0;

        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                bestCategory = entry.getKey();
            }
        }

        return maxScore == 0 ? "분류불가" : bestCategory;
    }
}
