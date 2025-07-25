package kr.soft.autofeed.threadLike.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.soft.autofeed.domain.ThreadLike;
import kr.soft.autofeed.domain.ThreadLikeId;

@Repository
public interface ThreadLikeRepository extends JpaRepository<ThreadLike, ThreadLikeId>{
    boolean existsByThreadLikeIdAndDelCheckFalse(ThreadLikeId id);
    List<ThreadLike> findAllByThreadThreadIdx(Long threadIdx);
    List<ThreadLike> findAllByUserUserIdx(Long userIdx);
}
