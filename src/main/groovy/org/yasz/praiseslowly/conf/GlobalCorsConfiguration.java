package org.yasz.praiseslowly.conf;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class GlobalCorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", HttpHeaders.CONTENT_DISPOSITION);
    }

}