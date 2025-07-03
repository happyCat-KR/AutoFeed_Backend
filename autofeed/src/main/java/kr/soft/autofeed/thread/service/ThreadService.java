package kr.soft.autofeed.thread.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.soft.autofeed.thread.dao.ThreadRepository;
import kr.soft.autofeed.thread.dto.ThreadRegistDTO;
import kr.soft.autofeed.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ThreadService {

    final private ThreadRepository threadRepository;

    public void threadRegist(ThreadRegistDTO threadRegistDTO){
        Thread thread = new Thread();
        
        
        threadRepository.save(null);
        
    }
    
    public void getImagesUrl(ThreadRegistDTO postRegistDTO) throws IOException{
        List<String> savedImageUrls = FileUploadUtil.saveImages(postRegistDTO.getPostImages(), postRegistDTO.getUserIdx());

        System.out.println("저장된 이미지들:");
        for (String url : savedImageUrls) {
            System.out.println(url);
        }

        System.out.println("게시글 내용: " + postRegistDTO.getContent());
    }
}
