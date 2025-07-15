package kr.soft.autofeed.relatedWord.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.soft.autofeed.domain.RelatedWord;

@Repository
public interface RelatedWordRepository extends JpaRepository<RelatedWord, Long>{
    List<RelatedWord> findByHashtagHashtagIdx(Long hashtagIdx);
}
