package xyz.atoml.rssrestreader.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import xyz.atoml.rssrestreader.services.RateLimiterService;

import java.io.IOException;

@Component
@Order(0)
public class RateLimitFilter extends HttpFilter
{
    private final RateLimiterService rateLimiterService;

    public RateLimitFilter(RateLimiterService rateLimiterService)
    {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    protected void doFilter(@NonNull HttpServletRequest request,
                            @NonNull HttpServletResponse response,
                            @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        if (!rateLimiterService.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        String address = request.getRemoteAddr();

        boolean actionLimitExceeded = rateLimiterService.hasReachedRateLimit(address, RateLimitType.ACTION);
        boolean wrongApiKeyLimitExceeded = rateLimiterService.hasReachedRateLimit(address, RateLimitType.WRONG_API_KEY);

        if (!actionLimitExceeded && !wrongApiKeyLimitExceeded) {
            rateLimiterService.signHit(address, RateLimitType.ACTION);
            filterChain.doFilter(request, response);
            return;
        }

        response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Rate limit exceeded. Try again later.");
    }
}
