package kr.soft.autofeed.relatedWord.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.soft.autofeed.domain.RelatedWord;
import kr.soft.autofeed.relatedWord.dao.RelatedWordRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RelatedWordService {
    private final RelatedWordRepository relatedWordRepository;

     public List<String> getWordsByHashtagIdx(Long hashtagIdx) {
        return relatedWordRepository.findByHashtagHashtagIdx(hashtagIdx).stream()
                .map(RelatedWord::getWord)
                .collect(Collectors.toList());
    }
}
