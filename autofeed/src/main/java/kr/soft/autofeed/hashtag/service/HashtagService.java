package kr.soft.autofeed.hashtag.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.soft.autofeed.domain.Hashtag;
import kr.soft.autofeed.domain.RelatedWord;
import kr.soft.autofeed.hashtag.dao.HashtagRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagService {

    final HashtagRepository hashtagRepository;

    public List<String> findAllCategoryNames() {
        return hashtagRepository.findAll().stream()
                .map(Hashtag::getHashtagName)
                .collect(Collectors.toList());
    }

}
