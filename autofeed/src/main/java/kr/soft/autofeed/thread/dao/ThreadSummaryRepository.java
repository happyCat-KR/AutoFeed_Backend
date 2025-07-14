package kr.soft.autofeed.thread.dao;

import java.util.List;

import kr.soft.autofeed.thread.dto.ThreadSummaryDTO;

public interface ThreadSummaryRepository {
    List<ThreadSummaryDTO> findTopThreadsByUser(Long userIdx);
}

