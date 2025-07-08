package kr.soft.autofeed.thread.service;

import java.io.IOException;
import java.util.List;

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

    @Transactional
    public ResponseData threadUpdate(ThreadUpdateDTO threadUpdateDTO) throws IOException{
        Thread thread = threadRepository.findById(threadUpdateDTO.getThreadIdx())
                .orElseThrow(() -> new IllegalArgumentException("해당 스레드가 존재하지 않습니다."));
        
        thread.setContent(threadUpdateDTO.getContent());

        threadHashtagRepository.findAllByThreadThreadIdx(thread.getThreadIdx())
                .forEach(relation -> relation.setDelCheck(true));
        
        if(threadUpdateDTO.getHashtagName() != null){
            for(String hashtagName : threadUpdateDTO.getHashtagName()){
                Hashtag hashtag = hashtagRepository.findByHashtagName(hashtagName)
                        .orElseThrow(() -> new IllegalArgumentException("해당 해쉬태그가 없습니다."));

                ThreadHashtagId threadHashtagId = new ThreadHashtagId(thread.getThreadIdx(), hashtag.getHashtagIdx());

                ThreadHashtag threadHashtag = threadHashtagRepository.findById(threadHashtagId)
                        .map(existing -> {
                            existing.setDelCheck(false);
                            return existing;
                        })
                        .orElseGet(() -> ThreadHashtag.builder()
                                .id(threadHashtagId)
                                .thread(thread)
                                .hashtag(hashtag)
                                .build());

                threadHashtagRepository.save(threadHashtag);
            }
        }

        mediaRepository.findAllByThreadThreadIdx(thread.getThreadIdx())
                .forEach(media -> media.setDelCheck(true));
        
        List<String> savedUrls = FileUploadUtil.saveImages(threadUpdateDTO.getThreadImages(), thread.getUser().getUserIdx());

        for(String url : savedUrls){
            mediaRepository.save(mediaInsert(thread, url));
        }


        return ResponseData.success();
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
            //3. thread_hastag 테이블 객체 생성 및 반환
            threadHashtagRepository.save(threadHashtagInsert(savedThread, hashtagName));
        }

        return ResponseData.success();

    }


    @Transactional    
    private Thread threadInsert(ThreadRegistDTO threadRegistDTO) {
        User user = userRepository.findById(threadRegistDTO.getUserIdx())
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));

        Thread parentThread = null;
        if(threadRegistDTO.getParentIdx() != null){
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
    private ThreadHashtag threadHashtagInsert(Thread savedThread, String hashtagName){

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
