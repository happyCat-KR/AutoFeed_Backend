package kr.soft.autofeed.user.dao;

import java.util.List;

import kr.soft.autofeed.thread.dto.ThreadViewDTO;
import kr.soft.autofeed.user.dto.UserSimpleProfileDTO;
import kr.soft.autofeed.user.dto.UserProfileViewDTO;

public interface UserCostomRepository {
    UserProfileViewDTO findUserProfile(Long userIdx);
    List<ThreadViewDTO> findUserThreads(Long userIdx);
    List<ThreadViewDTO> findUserReplies(Long userIdx);
    List<UserSimpleProfileDTO> findFollowerList(Long userIdx);
    List<UserSimpleProfileDTO> findFollowingList(Long userIdx);
    int countFollowings(Long userIdx);
    int countFollowers(Long userIdx);
    List<String> findHashtagsByPrefix(String prefix);
    List<UserSimpleProfileDTO> findUsersByUserIdPrefix(String userIdPrefix);
    List<UserSimpleProfileDTO> findUsersByHashtags(List<String> hashtags);
    List<ThreadViewDTO> findUserMediaThread(Long userIdx);
}
