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
import xyz.atoml.rssrestreader.services.AuthService;
import xyz.atoml.rssrestreader.services.RateLimiterService;

import java.io.IOException;

@Component
@Order(1)
public class ApiKeyFilter extends HttpFilter
{
    private static final String HEADER_API = "X-API-KEY";

    private final AuthService authService;
    private final RateLimiterService rateLimiterService;

    public ApiKeyFilter(AuthService authService, RateLimiterService rateLimiterService)
    {
        this.authService = authService;
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    protected void doFilter(@NonNull HttpServletRequest request,
                            @NonNull HttpServletResponse response,
                            @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        if (!authService.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        String address = request.getRemoteAddr();
        String apiKey = request.getHeader(HEADER_API);

        if (apiKey == null || apiKey.isEmpty()) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing API key");
            return;
        }

        if (!authService.validateApiKey(apiKey)) {
            rateLimiterService.signHit(address, RateLimitType.WRONG_API_KEY);
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid API Key");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
