package kr.soft.autofeed.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.soft.autofeed.thread.dto.ThreadRegistDTO;
import kr.soft.autofeed.thread.dto.ThreadUpdateDTO;
import kr.soft.autofeed.thread.service.ThreadService;
import kr.soft.autofeed.util.FileUploadUtil;
import kr.soft.autofeed.util.HangulFilterUtil;
import kr.soft.autofeed.util.ResponseData;
import kr.soft.autofeed.util.TextClassifier;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/thread")
@RequiredArgsConstructor
public class ThreadController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    final private ThreadService threadService;

    @PostMapping("/category")
    public String getCategory(@RequestParam("sentence") String sentence) {
        // 카테고리 분류 기능 호출
        return TextClassifier.classifyText(sentence);
    }

    @PostMapping("/write/profile")
    public ResponseEntity<ResponseData> threadWriteUserProfile(@RequestParam("userIdx") Long userIdx) {
        ResponseData responseData = threadService.threadWriteUserProfile(userIdx);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("page/replies")
    public ResponseEntity<ResponseData> getRepliesView(@RequestParam("parentIdx") Long parentIdx) {
        ResponseData responseData = threadService.getRepliesView(parentIdx);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("page/detail")
    public ResponseEntity<ResponseData> getDetailThread(@RequestParam("threadIdx") Long threadIdx) {
        ResponseData responseData = threadService.getDetailThread(threadIdx);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("search/nomal")
    public ResponseEntity<ResponseData> getSearchNomal(@RequestParam("inputStr") String inputStr) {
        inputStr = HangulFilterUtil.removeLastIfHangul(inputStr);
        ResponseData responseData = threadService.getSearchThreadResult(inputStr);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("search/hashtag")
    public ResponseEntity<ResponseData> getSearchHashtag(@RequestParam("inputStr") String inputStr) {
        ResponseData responseData = threadService.getSearchThreadResult(inputStr);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("page/feed/follow")
    public ResponseEntity<ResponseData> getFollowingThreads(@RequestParam("userIdx") Long userIdx) {
        ResponseData responseData = threadService.getFollowingThreads(userIdx);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/page/feed/recommend")
    public ResponseEntity<ResponseData> getTopThreads(@RequestParam("userIdx") Long userIdx) {
        ResponseData responseData = threadService.getTopThreads(userIdx);
        return ResponseEntity.ok(responseData);
    }

    // 게시글 작성
    @PostMapping(value = "/regist")
    public ResponseEntity<ResponseData> postRegist(MultipartHttpServletRequest request) throws IOException {
        Long userIdx = Long.parseLong(request.getParameter("userIdx"));

        String parentIdxStr = request.getParameter("parentIdx");
        Long parentIdx = (parentIdxStr != null && !parentIdxStr.isBlank()) ? Long.parseLong(parentIdxStr) : null;

        String content = request.getParameter("content");
        if (content == null)
            content = "";

        String[] hashtagName = request.getParameterValues("hashtagName");
        List<String> hashtagList = (hashtagName != null) ? Arrays.asList(hashtagName) : List.of();

        List<MultipartFile> threadImages = request.getFiles("images");

        ThreadRegistDTO threadRegistDTO = new ThreadRegistDTO(userIdx, parentIdx, content, hashtagList, threadImages);

        ResponseData responseData = threadService.threadRegist(threadRegistDTO);

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseData> threadDelete(@RequestParam("threadIdx") Long threadIdx) {
        ResponseData responseData = threadService.threadDelete(threadIdx);

        return ResponseEntity.ok(responseData);
    }

}
