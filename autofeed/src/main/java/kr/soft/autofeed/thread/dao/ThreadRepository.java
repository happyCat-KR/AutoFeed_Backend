package kr.soft.autofeed.thread.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import kr.soft.autofeed.domain.Thread;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Long>{
    List<Thread> findAllByUserUserIdx(Long userIdx);
}
