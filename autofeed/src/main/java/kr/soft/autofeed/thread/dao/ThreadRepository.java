package kr.soft.autofeed.thread.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import kr.soft.autofeed.domain.Thread;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Long> {
    List<Thread> findAllByUserUserIdx(Long userIdx);

    @Query("SELECT t FROM Thread t WHERE t.delCheck = true AND t.deletedAt <= :threshold")
    List<Thread> findAllForCleanup(@Param("threshold") LocalDateTime threshold);

}
