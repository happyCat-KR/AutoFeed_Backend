package kr.soft.autofeed.UserHashtag.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.soft.autofeed.domain.UserHashtag;
import kr.soft.autofeed.domain.UserHashtagId;

public interface UserHashtagRepository extends JpaRepository<UserHashtag, UserHashtagId>{

}
