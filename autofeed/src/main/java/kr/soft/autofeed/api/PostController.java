package kr.soft.autofeed.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.soft.autofeed.post.dto.PostRegistDTO;
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
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping(value = "/regist")
    public void postRegist(MultipartHttpServletRequest request) throws IOException{
        Long userIdx = Long.parseLong(request.getParameter("userIdx"));
        String content = request.getParameter("content");

        String[] hashtagName = request.getParameterValues("hashtagName");
        List<String> hashtagList = Arrays.asList(hashtagName);

        List<MultipartFile> file = request.getFiles("images");

        PostRegistDTO postRegistDTO = new PostRegistDTO(userIdx, content, hashtagList);

        List<String> savedImageUrls = FileUploadUtil.saveImages(file);

        System.out.println("저장된 이미지들:");
        for (String url : savedImageUrls) {
            System.out.println(url);
        }

        System.out.println("게시글 내용: " + postRegistDTO.getContent());
    }
}
