package kr.soft.autofeed.thread.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.soft.autofeed.domain.User;
import kr.soft.autofeed.hashtag.dao.HashtagRepository;
import kr.soft.autofeed.ThreadHashtag.dao.ThreadHashtagRepository;
import kr.soft.autofeed.domain.Hashtag;
import kr.soft.autofeed.domain.Media;
import kr.soft.autofeed.domain.Thread;
import kr.soft.autofeed.domain.ThreadHashtag;
import kr.soft.autofeed.domain.ThreadHashtagId;
import kr.soft.autofeed.thread.dao.ThreadRepository;
import kr.soft.autofeed.media.dao.MediaRepository;
import kr.soft.autofeed.thread.dto.ThreadRegistDTO;
import kr.soft.autofeed.thread.dto.ThreadUpdateDTO;
import kr.soft.autofeed.threadLike.dao.ThreadLikeRepository;
import kr.soft.autofeed.user.dao.UserRepository;
import kr.soft.autofeed.util.FileUploadUtil;
import kr.soft.autofeed.util.ResponseData;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ThreadService {

    final private ThreadRepository threadRepository;
    final private UserRepository userRepository;
    final private MediaRepository mediaRepository;
    final private HashtagRepository hashtagRepository;
    final private ThreadHashtagRepository threadHashtagRepository;
    final private ThreadLikeRepository threadLikeRepository;

    @Transactional
    public ResponseData threadRestore(Long threadIdx) {
        Thread thread = threadRepository.findById(threadIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        thread.setDelCheck(false);
        thread.setDeletedBy(null);
        thread.setDeletedAt(null);

        return ResponseData.success();
    }

    @Transactional
    public ResponseData threadDelete(Long threadIdx) {
        Thread thread = threadRepository.findById(threadIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        thread.setDelCheck(true);
        thread.setDeletedBy(thread.getUser());
        thread.setDeletedAt(LocalDateTime.now());

        return ResponseData.success();
    }

    // 매시간 정각에 실행
    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void threadCleanInsert(){
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusMinutes(2);
        List<Thread> threads = threadRepository.findAllForCleanup(twoHoursAgo);

        for(Thread thread : threads){
            thread.setContent("삭제된 게시글");
            threadHashtagRepository.findAllByThreadThreadIdx(thread.getThreadIdx())
                .forEach(threadHashtag -> threadHashtag.setDelCheck(true));

            mediaRepository.findAllByThreadThreadIdx(thread.getThreadIdx())
                .forEach(media -> media.setDelCheck(true));

            threadLikeRepository.findAllByThreadThreadIdx(thread.getThreadIdx())
                .forEach(like -> like.setDelCheck(true));
        }

    }

    @Transactional
    public ResponseData threadRegist(ThreadRegistDTO threadRegistDTO) throws IOException {

        // 1. thread 테이블 객체 생성 및 반환.
        Thread savedThread = threadRepository.save(threadInsert(threadRegistDTO));

        List<String> savedImageUrls = FileUploadUtil.saveImages(threadRegistDTO.getPostImages(),
                threadRegistDTO.getUserIdx());

        for (String url : savedImageUrls) {
            // 2. media 테이블 객체 생성 및 반환
            mediaRepository.save(mediaInsert(savedThread, url));
        }

        for (String hashtagName : threadRegistDTO.getHashtagName()) {
            // 3. thread_hastag 테이블 객체 생성 및 반환
            threadHashtagRepository.save(threadHashtagInsert(savedThread, hashtagName));
        }

        return ResponseData.success();

    }

    @Transactional
    private Thread threadInsert(ThreadRegistDTO threadRegistDTO) {
        User user = userRepository.findById(threadRegistDTO.getUserIdx())
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));

        Thread parentThread = null;
        if (threadRegistDTO.getParentIdx() != null) {
            parentThread = threadRepository.findById(threadRegistDTO.getParentIdx())
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        }

        Thread thread = Thread.builder()
                .user(user)
                .content(threadRegistDTO.getContent())
                .parentThread(parentThread)
                .build();

        return thread;
    }

    @Transactional
    private Media mediaInsert(Thread savedThread, String url) {
        String extension = url.substring(url.lastIndexOf(".") + 1);

        Media media = Media.builder()
                .thread(savedThread)
                .fileUrl(url)
                .fileType(extension)
                .build();

        return media;
    }

    @Transactional
    private ThreadHashtag threadHashtagInsert(Thread savedThread, String hashtagName) {

        Hashtag hashtag = hashtagRepository.findByHashtagName(hashtagName)
                .orElseThrow(() -> new IllegalArgumentException("해쉬태그가 없습니다."));

        ThreadHashtagId threadHashtagId = new ThreadHashtagId(
                savedThread.getThreadIdx(), hashtag.getHashtagIdx());

        ThreadHashtag threadHashtag = ThreadHashtag.builder()
                .id(threadHashtagId)
                .thread(savedThread)
                .hashtag(hashtag)
                .build();

        return threadHashtag;
    }

}
