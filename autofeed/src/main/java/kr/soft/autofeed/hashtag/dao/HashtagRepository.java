package kr.soft.autofeed.hastag.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.soft.autofeed.domain.Hashtag;

public interface HashtagRepository extends JpaRepository<Hashtag, Long>{
    Optional<Hashtag> findByHashtagName(String hashtagName);
}
