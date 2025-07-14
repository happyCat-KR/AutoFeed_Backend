package kr.soft.autofeed.thread.dao;

import java.util.List;

import kr.soft.autofeed.thread.dto.ThreadViewDTO;

public interface ThreadCustomRepository {
    List<ThreadViewDTO> findTopThreadsByUser(Long userIdx);
    List<ThreadViewDTO> findFollowingThreads(Long userIdx);

    List<ThreadViewDTO> findThreadsByHashtag(String hashtags);
    List<ThreadViewDTO> findThreadsByContentKeyword(String userIdPrefix);
    
}

