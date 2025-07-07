package kr.soft.autofeed.config;

import org.springframework.context.annotation.Configuration;
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
}

