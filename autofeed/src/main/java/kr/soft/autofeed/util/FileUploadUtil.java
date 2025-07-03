package kr.soft.autofeed.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUploadUtil {

    private static final Path UPLOAD_DIR = Paths.get("C:", "autofeed_image_folder", "thread_images");

    public static List<String> saveImages(List<MultipartFile> files) throws IOException {
        List<String> savedUrls = new ArrayList<>();

        File saveDir = UPLOAD_DIR.toFile();
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String originalFileName = file.getOriginalFilename();
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String savedFileName = UUID.randomUUID().toString() + extension;

                Path savedFilePath = UPLOAD_DIR.resolve(savedFileName);
                File savedFile = savedFilePath.toFile();

                file.transferTo(savedFile);

                savedUrls.add("/static/images/" + savedFileName); // 접근용 URL
            }
        }

        return savedUrls;
    }
}
