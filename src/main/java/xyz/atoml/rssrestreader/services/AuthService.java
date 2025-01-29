package xyz.atoml.rssrestreader.services;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xyz.atoml.rssrestreader.config.SecurityConfig;
import xyz.atoml.rssrestreader.core.BaseService;
import xyz.atoml.rssrestreader.core.logging.AppLogger;

import java.security.MessageDigest;

@Service
public class AuthService extends BaseService
{
    private final String apiKey;

    public AuthService(SecurityConfig config)
    {
        super(StringUtils.hasText(config.getApiKey()));

        this.apiKey = config.getApiKey();
    }

    public boolean validateApiKey(@NonNull String requestApiKey)
    {
        if (!enabled) {
            return true;
        }

        return MessageDigest.isEqual(apiKey.getBytes(), requestApiKey.getBytes());
    }

    @Override
    protected void logConfiguration()
    {
        if (enabled && !"changeme".equals(apiKey)) {
            return;
        }

        AppLogger.warn("API Key is missing or has default value!");
        AppLogger.warn("For security reasons set a unique API key in the config: `app.security.api-key`");
    }
}