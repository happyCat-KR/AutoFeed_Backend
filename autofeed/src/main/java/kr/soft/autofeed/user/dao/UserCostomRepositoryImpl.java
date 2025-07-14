package kr.soft.autofeed.user.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.soft.autofeed.thread.dto.ThreadViewDTO;
import kr.soft.autofeed.user.dto.UserSimpleProfileDTO;
import kr.soft.autofeed.user.dto.UserProfileViewDTO;

@Repository
public class UserCostomRepositoryImpl implements UserCostomRepository {
    // 현재는 비어 있지만, 필요에 따라 사용자 정의 메소드 구현 가능
    @PersistenceContext
    private EntityManager em;

    @Override
    public UserProfileViewDTO findUserProfile(Long userIdx) {
        return (UserProfileViewDTO) em.createNativeQuery("""
                    SELECT u.user_idx AS userIdx,
                           u.user_name AS userName,
                           u.user_id AS userId,
                           u.bio AS bio,
                           GROUP_CONCAT(DISTINCT h.hashtag_name) AS userHashtag,
                           u.profile_image AS profileImage,
                            (
                                SELECT COUNT(f2.follower_id)
                                FROM follow f2
                                WHERE f2.following_id = u.user_idx AND f2.del_check = 0
                            ) AS followerCount
                    FROM USER u
                    LEFT JOIN user_hashtag uh ON u.user_idx = uh.user_idx
                    RIGHT JOIN hashtag h ON uh.hashtag_idx = h.hashtag_idx
                    LEFT JOIN follow f ON u.user_idx = f.following_id AND f.del_check = 0
                    WHERE u.user_idx = :userIdx
                    GROUP BY u.user_idx
                """, "UserProfileViewMapping")
                .setParameter("userIdx", userIdx)
                .getSingleResult();
    }

    @Override
    public List<ThreadViewDTO> findUserThreads(Long userIdx) {
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
                    WHERE t.user_idx = :userIdx AND t.parent_idx IS NULL AND t.del_check = 0
                    GROUP BY t.thread_idx, u.profile_image, u.user_id, t.content
                    ORDER BY likeCount DESC
                """, "ThreadViewMapping")
                .setParameter("userIdx", userIdx)
                .getResultList();
    }

    @Override
    public List<ThreadViewDTO> findUserReplies(Long userIdx) {
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
                    WHERE t.user_idx = :userIdx AND t.parent_idx IS NOT NULL AND t.del_check = 0
                    GROUP BY t.thread_idx, u.profile_image, u.user_id, t.content
                    ORDER BY likeCount DESC
                """, "ThreadViewMapping")
                .setParameter("userIdx", userIdx)
                .getResultList();
    }

    @Override
    public List<UserSimpleProfileDTO> findFollowerList(Long userIdx) {
        return em
                .createNativeQuery(
                        """
                                    SELECT u.user_idx AS userIdx, u.user_id AS userId, u.profile_image AS profileImage, u.user_name AS userName
                                    FROM USER u
                                    WHERE u.user_idx IN (
                                        SELECT follower_id
                                        FROM follow
                                        WHERE following_id = :userIdx AND del_check = 0
                                    )
                                """,
                        "UserSimpleProfileMapping")
                .setParameter("userIdx", userIdx)
                .getResultList();
    }

    @Override
    public List<UserSimpleProfileDTO> findFollowingList(Long userIdx) {
        return em
                .createNativeQuery(
                        """
                                    SELECT u.user_idx AS userIdx, u.user_id AS userId, u.profile_image AS profileImage, u.user_name AS userName
                                    FROM USER u
                                    WHERE u.user_idx IN (
                                        SELECT following_id
                                        FROM follow
                                        WHERE follower_id = :userIdx AND del_check = 0
                                    )
                                """,
                        "UserSimpleProfileMapping")
                .setParameter("userIdx", userIdx)
                .getResultList();
    }

    @Override
    public int countFollowings(Long userIdx) {
        return ((Number) em.createNativeQuery("""
                    SELECT COUNT(*)
                    FROM follow
                    WHERE follower_id = :userIdx AND del_check = 0
                """)
                .setParameter("userIdx", userIdx)
                .getSingleResult()).intValue();
    }

    @Override
    public int countFollowers(Long userIdx) {
        return ((Number) em.createNativeQuery("""
                    SELECT COUNT(*)
                    FROM follow
                    WHERE following_id = :userIdx AND del_check = 0
                """)
                .setParameter("userIdx", userIdx)
                .getSingleResult()).intValue();
    }

    public List<String> findHashtagsByPrefix(String prefix) {
        return em.createNativeQuery("""
                    SELECT hashtag_name
                    FROM hashtag
                    WHERE hashtag_name LIKE :prefix
                """)
                .setParameter("prefix", prefix + "%")
                .getResultList();
    }

    // 2. 입력값으로 사용자 검색
    public List<UserSimpleProfileDTO> findUsersByUserIdPrefix(String userIdPrefix) {
        return em.createNativeQuery("""
                    SELECT user_idx AS userIdx, user_id AS userId, profile_image AS profileImage, user_name AS userName
                    FROM USER
                    WHERE user_id LIKE :userId AND del_check = 0
                """, "UserSimpleProfileMapping")
                .setParameter("userId", userIdPrefix + "%")
                .getResultList();
    }

    // 3. 해시태그명 기반 관련 사용자 검색
    public List<UserSimpleProfileDTO> findUsersByHashtags(List<String> hashtags) {
        return em.createNativeQuery("""
                    SELECT user_idx AS userIdx, user_id AS userId, profile_image AS profileImage, user_name AS userName
                    FROM USER
                    WHERE user_idx IN (
                        SELECT DISTINCT user_idx
                        FROM user_hashtag
                        WHERE hashtag_idx IN (
                            SELECT hashtag_idx
                            FROM hashtag
                            WHERE hashtag_name IN :hashtags
                        )
                    ) AND del_check = 0
                """, "UserSimpleProfileMapping")
                .setParameter("hashtags", hashtags)
                .getResultList();
    }
}
