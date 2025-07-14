package kr.soft.autofeed.thread.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.soft.autofeed.thread.dto.ThreadSummaryDTO;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ThreadSummaryRepositoryImpl implements ThreadSummaryRepository {

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    @Override
    public List<ThreadSummaryDTO> findTopThreadsByUser(Long userIdx) {
        return em.createNativeQuery("""
                    SELECT
                        t.thread_idx AS threadIdx,
                        u.profile_image AS profileImage,
                        u.user_id AS userId,
                        t.content AS content,
                        GROUP_CONCAT(DISTINCT m.file_url) AS fileUrls,
                        COUNT(DISTINCT tl.user_idx) AS likeCount
                    FROM USER u
                    LEFT JOIN thread t ON u.user_idx = t.user_idx
                    LEFT JOIN media m ON t.thread_idx = m.thread_idx
                    LEFT JOIN thread_like tl ON t.thread_idx = tl.thread_idx AND tl.del_check = 0
                    WHERE t.thread_idx IN (
                        SELECT th.thread_idx
                        FROM thread_hashtag th
                        LEFT JOIN thread_like tl ON th.thread_idx = tl.thread_idx AND tl.del_check = 0
                        WHERE th.hashtag_idx IN (
                            SELECT hashtag_idx FROM (
                                SELECT ua.hashtag_idx
                                FROM user_action ua
                                LEFT JOIN action_type at ON ua.action_type = at.type_code
                                WHERE ua.user_idx = :userIdx
                                GROUP BY ua.hashtag_idx
                                ORDER BY SUM(at.score_value) DESC
                                LIMIT 2
                            ) AS limited_hashtags
                        )
                        GROUP BY th.thread_idx
                    )
                    GROUP BY t.thread_idx, u.profile_image, u.user_id, t.content
                    ORDER BY likeCount DESC
                """, "ThreadSummaryMapping")
                .setParameter("userIdx", userIdx)
                .getResultList();
    }

}
