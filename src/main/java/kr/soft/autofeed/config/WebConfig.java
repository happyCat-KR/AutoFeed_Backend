package kr.soft.autofeed.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 유저 프로필 이미지 경로 매핓
        registry.addResourceHandler("/images/user/**")
                .addResourceLocations("file:///C:/autofeed_image_folder/user_images/");

        registry.addResourceHandler("images/thread/**")
                .addResourceLocations("file:///C:/autofeed_image_folder/thread_images/");
    }

    // 🔹 CORS 설정 추가
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // React 개발 서버 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true);
    }
}
