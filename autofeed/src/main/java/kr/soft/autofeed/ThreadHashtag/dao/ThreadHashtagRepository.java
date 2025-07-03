package kr.soft.autofeed.ThreadHashtag.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import kr.soft.autofeed.domain.ThreadHashtag;
import kr.soft.autofeed.domain.ThreadHashtagId;

@Repository
public interface ThreadHashtagRepository extends JpaRepository<ThreadHashtag, ThreadHashtagId> {
    
}
