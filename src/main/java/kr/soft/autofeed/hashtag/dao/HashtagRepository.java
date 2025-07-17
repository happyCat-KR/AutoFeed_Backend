package kr.soft.autofeed.hashtag.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.soft.autofeed.domain.Hashtag;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long>{
    Optional<Hashtag> findByHashtagName(String hashtagName);
}
