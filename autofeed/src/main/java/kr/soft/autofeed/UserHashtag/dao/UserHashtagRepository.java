package kr.soft.autofeed.UserHashtag.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.soft.autofeed.domain.UserHashtag;
import kr.soft.autofeed.domain.UserHashtagId;

@Repository
public interface UserHashtagRepository extends JpaRepository<UserHashtag, UserHashtagId> {
    // UserHashtagRepository.java
    List<UserHashtag> findAllByUserUserIdx(Long userIdx);

}
