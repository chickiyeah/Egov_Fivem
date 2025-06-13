package egovframework.com.fivemlist.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 가상 경로 "/uploads/**" → 실제 경로 "D:/myproject/uploads/"
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///E:/userupload/");
    }
}