package kr.soft.autofeed.thread.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.websocket.Decoder.Text;
import kr.soft.autofeed.domain.User;
import kr.soft.autofeed.domain.UserAction;
import kr.soft.autofeed.hashtag.dao.HashtagRepository;
import kr.soft.autofeed.ActionType.dao.ActionTypeRepository;
import kr.soft.autofeed.ThreadHashtag.dao.ThreadHashtagRepository;
import kr.soft.autofeed.UserAction.dao.UserActionRepository;
import kr.soft.autofeed.UserAction.service.UserActionService;
import kr.soft.autofeed.domain.ActionType;
import kr.soft.autofeed.domain.Hashtag;
import kr.soft.autofeed.domain.Media;
import kr.soft.autofeed.domain.Thread;
import kr.soft.autofeed.domain.ThreadHashtag;
import kr.soft.autofeed.domain.ThreadHashtagId;
import kr.soft.autofeed.thread.dao.ThreadRepository;
import kr.soft.autofeed.thread.dao.ThreadCustomRepository;
import kr.soft.autofeed.media.dao.MediaRepository;
import kr.soft.autofeed.thread.dto.ThreadDetailViewDTO;
import kr.soft.autofeed.thread.dto.ThreadRegistDTO;
import kr.soft.autofeed.thread.dto.ThreadViewDTO;
import kr.soft.autofeed.thread.dto.WriteUserProfileDTO;
import kr.soft.autofeed.thread.dto.ThreadUpdateDTO;
import kr.soft.autofeed.threadLike.dao.ThreadLikeRepository;
import kr.soft.autofeed.user.dao.UserRepository;
import kr.soft.autofeed.util.FileUploadUtil;
import kr.soft.autofeed.util.HangulFilterUtil;
import kr.soft.autofeed.util.ResponseData;
import kr.soft.autofeed.util.TextClassifier;
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
    final private UserActionRepository userActionRepository;
    final private UserActionService userActionService;
    private final ThreadCustomRepository threadCustomRepository;

    @Transactional
    public ResponseData threadWriteUserProfile(Long userIdx) {
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        WriteUserProfileDTO writeUserProfileDTO = new WriteUserProfileDTO(
                user.getUserIdx(),
                user.getUserId(),
                user.getProfileImage()
        );
        return ResponseData.success(writeUserProfileDTO);
    }

    @Transactional
    public ResponseData getRepliesView(Long parentIdx) {
        List<ThreadViewDTO> replies = threadCustomRepository.findRepliesOfThread(parentIdx);
        return ResponseData.success(replies);
    }

    @Transactional
    public ResponseData getDetailThread(Long threadIdx) {
        ThreadViewDTO thread = threadCustomRepository.findDetailThread(threadIdx);
        List<ThreadViewDTO> replies = threadCustomRepository.findRepliesOfThread(threadIdx);
        ThreadDetailViewDTO detailView = new ThreadDetailViewDTO(thread, replies);
        return ResponseData.success(detailView);
    }

    @Transactional
    public ResponseData getSearchThreadResult(String inputStr) {
        System.out.println("Search input: " + inputStr);
        List<ThreadViewDTO> hashtagResults = threadCustomRepository.findThreadsByHashtag(inputStr);
        List<ThreadViewDTO> contentResults = threadCustomRepository.findThreadsByContentKeyword(inputStr);

        // threadIdx 기준 중복 제거를 위해 Map 사용
        Map<Long, ThreadViewDTO> mergedMap = new LinkedHashMap<>();

        // hashtag 결과 먼저 넣기
        for (ThreadViewDTO t : hashtagResults) {
            mergedMap.put(t.getThreadIdx(), t);
        }

        // content 결과 중복 없이 추가
        for (ThreadViewDTO t : contentResults) {
            mergedMap.putIfAbsent(t.getThreadIdx(), t);
        }

        List<ThreadViewDTO> mergedList = new ArrayList<>(mergedMap.values());

        return ResponseData.success(mergedList);
    }

    @Transactional
    public ResponseData getFollowingThreads(Long userIdx) {
        List<ThreadViewDTO> result = threadCustomRepository.findFollowingThreads(userIdx);
        return ResponseData.success(result);
    }

    @Transactional
    public ResponseData getTopThreads(Long userIdx) {
        List<ThreadViewDTO> result = threadCustomRepository.findTopThreadsByUser(userIdx);

        return ResponseData.success(result);
    }

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
    public void threadCleanInsert() {
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusMinutes(2);
        List<Thread> threads = threadRepository.findAllForCleanup(twoHoursAgo);

        for (Thread thread : threads) {
            if (thread.getContent().equals("삭제된 게시글"))
                continue;

            thread.setContent("삭제된 게시글");

            threadHashtagRepository.findAllByThreadThreadIdx(thread.getThreadIdx())
                    .forEach(threadHashtag -> {
                        // 활동 기록 저장(삭제)
                        userActionService.regist(thread.getUser(), threadHashtag.getHashtag(), "post_delete");
                        // 해시태그 삭제
                        threadHashtag.setDelCheck(true);
                    });

            mediaRepository.findAllByThreadThreadIdx(thread.getThreadIdx())
                    .forEach(media -> media.setDelCheck(true));

            threadLikeRepository.findAllByThreadThreadIdx(thread.getThreadIdx())
                    .forEach(like -> like.setDelCheck(true));
        }

    }

    @Transactional
    public ResponseData threadRegist(ThreadRegistDTO threadRegistDTO) throws IOException {

        // 작성 내용 카테고리분류
        String category = TextClassifier.classifyText(threadRegistDTO.getContent());

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

            // 4. 활동 기록 저장
            Hashtag hashtag = hashtagRepository.findByHashtagName(hashtagName)
                    .orElseThrow(() -> new IllegalArgumentException("해쉬태그가 없습니다."));
            userActionService.regist(savedThread.getUser(), hashtag, "post_create");

            if(hashtagName.equals(category)) {
                category = "중복";
            }
        }
        System.out.println("카테고리 분류 결과: " + category);
        // 5. 카테고리 활동 기록 저장(게시글작성시 카테고리 분류가 되면 해당 카테고리로 활동 기록 저장 *이미 해시태그로 저장된 경우는 제외)
        if(!category.equals("분류불가") && !category.equals("중복")) {
            Hashtag hashtag = hashtagRepository.findByHashtagName(category)
                    .orElseThrow(() -> new IllegalArgumentException("해쉬태그가 없습니다."));
            userActionService.regist(savedThread.getUser(), hashtag, "post_create");
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
