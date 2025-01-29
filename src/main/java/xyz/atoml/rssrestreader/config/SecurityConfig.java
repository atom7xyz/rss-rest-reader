package xyz.atoml.rssrestreader.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.security")
public class SecurityConfig
{
    private int rateLimitAction;
    private int rateLimitWrongApiKey;
    private String apiKey;
}
