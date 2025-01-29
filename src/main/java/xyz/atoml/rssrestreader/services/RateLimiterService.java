package xyz.atoml.rssrestreader.services;

import lombok.NonNull;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.atoml.rssrestreader.config.SecurityConfig;
import xyz.atoml.rssrestreader.core.BaseService;
import xyz.atoml.rssrestreader.core.logging.AppLogger;
import xyz.atoml.rssrestreader.security.RateLimitType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@EnableScheduling
public class RateLimiterService extends BaseService
{
    private final int rateLimitAction;
    private final int rateLimitWrongApiKey;
    private final Map<String, AtomicInteger> actionRequests;
    private final Map<String, AtomicInteger> wrongApiKeyRequests;

    public RateLimiterService(SecurityConfig config)
    {
        super(config.getRateLimitAction() > 0 || config.getRateLimitWrongApiKey() > 0);

        this.rateLimitAction = config.getRateLimitAction();
        this.rateLimitWrongApiKey = config.getRateLimitWrongApiKey();
        this.actionRequests = new ConcurrentHashMap<>();
        this.wrongApiKeyRequests = new ConcurrentHashMap<>();
    }

    public boolean hasReachedRateLimit(@NonNull String address, RateLimitType type)
    {
        return switch (type)
        {
            case ACTION -> checkRateLimit(address, rateLimitAction, actionRequests);
            case WRONG_API_KEY -> checkRateLimit(address, rateLimitWrongApiKey, wrongApiKeyRequests);
        };
    }

    public void signHit(@NonNull String address, RateLimitType type)
    {
        switch (type)
        {
            case ACTION -> incrEntry(address, actionRequests);
            case WRONG_API_KEY -> incrEntry(address, wrongApiKeyRequests);
        }
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void resetCounts()
    {
        actionRequests.clear();
        wrongApiKeyRequests.clear();
    }

    private void incrEntry(@NonNull String address, Map<String, AtomicInteger> map)
    {
        map.putIfAbsent(address, new AtomicInteger(0));
        map.get(address).getAndIncrement();
    }

    private boolean checkRateLimit(@NonNull String address, int limit, Map<String, AtomicInteger> map)
    {
        map.putIfAbsent(address, new AtomicInteger(1));
        return map.get(address).get() > limit;
    }

    @Override
    protected void logConfiguration()
    {
        if (!enabled) {
            AppLogger.warn("Rate limiting is completely disabled!");
            return;
        }

        AppLogger.info(String.format("Rate limits: %d actions/min, %d wrong API keys/min",
                rateLimitAction, rateLimitWrongApiKey));
    }
}
