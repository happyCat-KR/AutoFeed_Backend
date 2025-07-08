package kr.soft.autofeed.threadLike.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.soft.autofeed.domain.User;
import kr.soft.autofeed.domain.Thread;
import kr.soft.autofeed.domain.ThreadLike;
import kr.soft.autofeed.domain.ThreadLikeId;
import kr.soft.autofeed.thread.dao.ThreadRepository;
import kr.soft.autofeed.threadLike.dao.ThreadLikeRepository;
import kr.soft.autofeed.threadLike.dto.LikeDTO;
import kr.soft.autofeed.user.dao.UserRepository;
import kr.soft.autofeed.util.ResponseData;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ThreadLikeService {

    private final ThreadLikeRepository threadLikeRepository;
    private final UserRepository userRepository;
    private final ThreadRepository threadRepository;

    @Transactional
    public ResponseData LikeRegist(LikeDTO likeRegistDTO) {
        User user = userRepository.findById(likeRegistDTO.getUserIdx())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Thread thread = threadRepository.findById(likeRegistDTO.getThreadIdx())
                .orElseThrow(() -> new IllegalArgumentException("해당 스레드가 존재하지 않습니다."));

        ThreadLikeId threadLikeId = new ThreadLikeId(thread.getThreadIdx(), user.getUserIdx());

        // 중복체크
        ThreadLike threadLike = threadLikeRepository.findById(threadLikeId).map(existing -> {
            if (!existing.isDelCheck()) {
                throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
            }
            existing.setDelCheck(false);

            return existing;
        }).orElseGet(() -> 
        ThreadLike.builder()
                .threadLikeId(threadLikeId)
                .thread(thread)
                .user(user)
                .build());

        threadLikeRepository.save(threadLike);

        return ResponseData.success();
    }

    @Transactional
    public ResponseData LikeCancel(LikeDTO likeCancelDTO) {

        User user = userRepository.findById(likeCancelDTO.getUserIdx())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Thread thread = threadRepository.findById(likeCancelDTO.getThreadIdx())
                .orElseThrow(() -> new IllegalArgumentException("해당 스레드가 존재하지 않습니다."));

        ThreadLikeId threadLikeId = new ThreadLikeId(thread.getThreadIdx(), user.getUserIdx());

        ThreadLike threadLike = threadLikeRepository.findById(threadLikeId)
                .orElseThrow(() -> new IllegalArgumentException("좋아요 기록이 존재하지 않습니다."));

        if (threadLike.isDelCheck()) {
            throw new IllegalArgumentException("이미 취소된 좋아요 입니다.");
        }

        threadLike.setDelCheck(true);

        return ResponseData.success();

    }

}
