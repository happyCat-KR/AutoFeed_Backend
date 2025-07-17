package kr.soft.autofeed.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserProfileUtil {

    private static final Path UPLOAD_DIR = Paths.get("C:", "autofeed_image_folder", "user_images");

    // 웹에서 접근할 때 사용할 prefix (이 경로로 요청 시 위의 폴더에서 파일 찾게 매핑할 예정)
    private static final String URL_PREFIX = "/images/user/";

    public static String saveImage(MultipartFile file, Long userIdx) throws IOException {
        List<String> allowedExtensions = List.of(".jpg", ".jpeg", ".png", "jfif");

        File saveDir = UPLOAD_DIR.toFile();
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        if (file != null && !file.isEmpty()) {

            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();

            if (!allowedExtensions.contains(extension)) {
                System.out.println("허용되지 않은 파일 형식: " + extension);
                throw new IllegalArgumentException("허용되지 않은 파일 확장자입니다."); // 이 파일은 저장하지 않음
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String timestamp = LocalDateTime.now().format(formatter);
            String savedFileName = userIdx + "_" + timestamp + extension;

            Path savedFilePath = UPLOAD_DIR.resolve(savedFileName);
            File savedFile = savedFilePath.toFile();

            file.transferTo(savedFile);

            return URL_PREFIX + savedFileName;
        }

        return "";
    }
}
