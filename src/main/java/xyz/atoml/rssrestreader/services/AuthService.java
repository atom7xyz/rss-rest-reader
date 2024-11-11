package xyz.atoml.rssrestreader.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xyz.atoml.rssrestreader.services.struct.AppService;
import xyz.atoml.rssrestreader.utils.AppLogger;

@Service
public class AuthService extends AppService
{
    private final String apiKey;

    public AuthService(@Value("${rss.api.key:#{null}}") String apiKey)
    {
        super(StringUtils.hasText(apiKey));
        this.apiKey = apiKey;

        if (!isEnabled())
        {
            AppLogger.warn("API Key is missing!");
            AppLogger.warn("For security reasons, it is recommended to set a unique API key in the config: `rss.api.key`");
        }

        if ("change_me".equals(apiKey))
        {
            AppLogger.warn("API Key has default value!");
            AppLogger.warn("For security reasons, it is recommended to set a unique API key in the config: `rss.api.key`");
        }
    }

    public boolean validateApiKey(@Nullable String requestApiKey)
    {
        if (!isEnabled())
        {
            return true;
        }

        return apiKey.equals(requestApiKey);
    }
}
