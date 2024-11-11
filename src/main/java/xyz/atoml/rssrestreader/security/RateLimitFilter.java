package xyz.atoml.rssrestreader.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.atoml.rssrestreader.services.RateLimiterService;

import java.io.IOException;

@Component
@Order(1)
public class RateLimitFilter extends OncePerRequestFilter
{
    private final RateLimiterService rateLimiterService;

    public RateLimitFilter(RateLimiterService rateLimiterService)
    {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        if (!rateLimiterService.isEnabled())
        {
            filterChain.doFilter(request, response);
            return;
        }

        String clientIp = request.getRemoteAddr();
        boolean rateLimited = !rateLimiterService.isAllowed(clientIp);

        if (rateLimited)
        {
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Rate limit exceeded. Try again later.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
