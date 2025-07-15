package kr.soft.autofeed.thread.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.soft.autofeed.thread.dto.ThreadViewDTO;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ThreadCustomRepositoryImpl implements ThreadCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ThreadViewDTO> findThreadsByHashtag(String hashtagName) {
        String sql = """
                    SELECT
                        t.thread_idx AS threadIdx,
                        u.profile_image AS profileImage,
                        u.user_id AS userId,
                        t.content AS content,
                        GROUP_CONCAT(DISTINCT m.file_url) AS fileUrls,
                        COUNT(DISTINCT tl.user_idx) AS likeCount,
                        (SELECT COUNT(*) FROM thread c WHERE c.parent_idx = t.thread_idx) AS commentCount
                    FROM USER u
                    LEFT JOIN thread t ON u.user_idx = t.user_idx
                    LEFT JOIN media m ON t.thread_idx = m.thread_idx
                    LEFT JOIN thread_like tl ON t.thread_idx = tl.thread_idx AND tl.del_check = 0
                    WHERE t.thread_idx IN (
                        SELECT thread_idx FROM thread_hashtag WHERE hashtag_idx = (
                            SELECT hashtag_idx FROM hashtag WHERE hashtag_name = :hashtagName
                        )
                    )
                    AND t.parent_idx IS NULL AND t.del_check = 0
                    GROUP BY t.thread_idx, u.profile_image, u.user_id, t.content
                    ORDER BY likeCount DESC
                """;

        return em.createNativeQuery(sql, "ThreadViewMapping")
                .setParameter("hashtagName", hashtagName)
                .getResultList();
    }

    @Override
    public List<ThreadViewDTO> findThreadsByContentKeyword(String keyword) {
        String sql = """
                    SELECT
                        t.thread_idx AS threadIdx,
                        u.profile_image AS profileImage,
                        u.user_id AS userId,
                        t.content AS content,
                        GROUP_CONCAT(DISTINCT m.file_url) AS fileUrls,
                        COUNT(DISTINCT tl.user_idx) AS likeCount,
                        (SELECT COUNT(*) FROM thread c WHERE c.parent_idx = t.thread_idx) AS commentCount
                    FROM USER u
                    LEFT JOIN thread t ON u.user_idx = t.user_idx
                    LEFT JOIN media m ON t.thread_idx = m.thread_idx
                    LEFT JOIN thread_like tl ON t.thread_idx = tl.thread_idx AND tl.del_check = 0
                    WHERE t.thread_idx IN (
                        SELECT thread_idx FROM thread WHERE content LIKE CONCAT('%', :keyword, '%')
                    )
                    AND t.parent_idx IS NULL AND t.del_check = 0
                    GROUP BY t.thread_idx, u.profile_image, u.user_id, t.content
                    ORDER BY likeCount DESC
                """;

        return em.createNativeQuery(sql, "ThreadViewMapping")
                .setParameter("keyword", keyword)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ThreadViewDTO> findTopThreadsByUser(Long userIdx) {
        return em.createNativeQuery("""
                    SELECT
                        t.thread_idx AS threadIdx,
                        u.profile_image AS profileImage,
                        u.user_id AS userId,
                        t.content AS content,
                        GROUP_CONCAT(DISTINCT m.file_url) AS fileUrls,
                        COUNT(DISTINCT tl.user_idx) AS likeCount,
                        (SELECT COUNT(*) FROM thread c WHERE c.parent_idx = t.thread_idx) AS commentCount
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
                    ) AND t.parent_idx IS NULL
                    GROUP BY t.thread_idx, u.profile_image, u.user_id, t.content
                    ORDER BY likeCount DESC
                """, "ThreadViewMapping")
                .setParameter("userIdx", userIdx)
                .getResultList();
    }

    @Override
    public List<ThreadViewDTO> findFollowingThreads(Long userIdx) {
        return em.createNativeQuery("""
                    SELECT
                        t.thread_idx AS threadIdx,
                        u.profile_image AS profileImage,
                        u.user_id AS userId,
                        t.content AS content,
                        GROUP_CONCAT(DISTINCT m.file_url) AS fileUrls,
                        COUNT(DISTINCT tl.user_idx) AS likeCount,
                        (SELECT COUNT(*) FROM thread c WHERE c.parent_idx = t.thread_idx) AS commentCount
                    FROM USER u
                    LEFT JOIN thread t ON u.user_idx = t.user_idx
                    LEFT JOIN media m ON t.thread_idx = m.thread_idx
                    LEFT JOIN thread_like tl ON t.thread_idx = tl.thread_idx AND tl.del_check = 0
                    WHERE t.thread_idx IN (
                        SELECT thread_idx
                        FROM thread
                        WHERE user_idx IN (
                            SELECT following_id
                            FROM follow
                            WHERE follower_id = :userIdx AND del_check = 0
                        )
                    ) AND t.parent_idx IS NULL
                    GROUP BY t.thread_idx, u.profile_image, u.user_id, t.content
                    ORDER BY likeCount DESC
                """, "ThreadViewMapping")
                .setParameter("userIdx", userIdx)
                .getResultList();
    }


    @Override
    public ThreadViewDTO findDetailThread(Long threadIdx) {
        String sql = """
        WITH RECURSIVE reply_tree AS (
            SELECT thread_idx, parent_idx
            FROM thread
            WHERE parent_idx = :threadIdx
            UNION ALL
            SELECT t.thread_idx, t.parent_idx
            FROM thread t
            JOIN reply_tree rt ON t.parent_idx = rt.thread_idx
        )
        SELECT 
            t.thread_idx AS threadIdx,
            u.profile_image AS profileImage,
            u.user_id AS userId,
            t.content AS content,
            GROUP_CONCAT(DISTINCT m.file_url) AS fileUrls,
            COUNT(DISTINCT tl.user_idx) AS likeCount,
            (SELECT COUNT(*) FROM reply_tree) AS commentCount
        FROM USER u
        LEFT JOIN thread t ON u.user_idx = t.user_idx
        LEFT JOIN media m ON t.thread_idx = m.thread_idx
        LEFT JOIN thread_like tl ON t.thread_idx = tl.thread_idx AND tl.del_check = 0
        WHERE t.thread_idx = :threadIdx AND t.parent_idx IS NULL AND t.del_check = 0
        GROUP BY t.thread_idx, u.profile_image, u.user_id, t.content
        ORDER BY likeCount DESC
    """;
        return (ThreadViewDTO) em.createNativeQuery(sql, "ThreadViewMapping")
                .setParameter("threadIdx", threadIdx)
                .getSingleResult();
    }


     @Override
    public List<ThreadViewDTO> findRepliesOfThread(Long parentIdx) {
        String sql = """
        SELECT 
            t.thread_idx AS threadIdx,
            u.profile_image AS profileImage,
            u.user_id AS userId,
            t.content AS content,
            GROUP_CONCAT(DISTINCT m.file_url) AS fileUrls,
            COUNT(DISTINCT tl.user_idx) AS likeCount,
            (SELECT COUNT(*) FROM thread c WHERE c.parent_idx = t.thread_idx) AS commentCount
        FROM USER u
        LEFT JOIN thread t ON u.user_idx = t.user_idx
        LEFT JOIN media m ON t.thread_idx = m.thread_idx
        LEFT JOIN thread_like tl ON t.thread_idx = tl.thread_idx AND tl.del_check = 0
        WHERE t.thread_idx IN (
            SELECT thread_idx FROM thread WHERE parent_idx = :parentIdx
        ) AND t.del_check = 0
        GROUP BY t.thread_idx, u.profile_image, u.user_id, t.content
        ORDER BY likeCount DESC
    """;
        return em.createNativeQuery(sql, "ThreadViewMapping")
                .setParameter("parentIdx", parentIdx)
                .getResultList();
    }


}
