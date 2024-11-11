package xyz.atoml.rssrestreader.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.atoml.rssrestreader.services.struct.AppService;
import xyz.atoml.rssrestreader.utils.AppLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@EnableScheduling
public class RateLimiterService extends AppService
{
    private final int maxRequestsPerMinute;
    private final Map<String, AtomicInteger> requestCounts;

    public RateLimiterService(@Value("${rss.api.rate-limit:#{-1}}") int maxRequestsPerMinute)
    {
        super(maxRequestsPerMinute > 0);
        this.maxRequestsPerMinute = maxRequestsPerMinute;
        this.requestCounts = new ConcurrentHashMap<>();

        if (!isEnabled())
        {
            AppLogger.warn("API Rate limit is disabled.");
            AppLogger.warn("To enable it, change the config: `rss.api.rate-limit`");
        }
    }

    public boolean isAllowed(@NonNull String ipAddress)
    {
        if (!isEnabled())
        {
            return true;
        }

        requestCounts.putIfAbsent(ipAddress, new AtomicInteger(0));

        int currentCount = requestCounts.get(ipAddress)
                                        .incrementAndGet();

        return currentCount <= maxRequestsPerMinute;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void resetCounts()
    {
        requestCounts.clear();
    }
}
