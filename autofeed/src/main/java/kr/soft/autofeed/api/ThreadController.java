package kr.soft.autofeed.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.soft.autofeed.thread.dto.ThreadRegistDTO;
import kr.soft.autofeed.thread.service.ThreadService;
import kr.soft.autofeed.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/thread")
@RequiredArgsConstructor
public class ThreadController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    final private ThreadService threadService;

    // 게시글 작성
    @PostMapping(value = "/regist")
    public void postRegist(MultipartHttpServletRequest request) throws IOException{
        Long userIdx = Long.parseLong(request.getParameter("userIdx"));
        String content = request.getParameter("content");
        if (content == null) content = "";

        String[] hashtagName = request.getParameterValues("hashtagName");
        List<String> hashtagList = (hashtagName != null) ? Arrays.asList(hashtagName) : List.of();

        List<MultipartFile> threadImages = request.getFiles("images");

        ThreadRegistDTO threadRegistDTO = new ThreadRegistDTO(userIdx, content, hashtagList, threadImages);

        threadService.threadRegist(threadRegistDTO);
        
    }
}
