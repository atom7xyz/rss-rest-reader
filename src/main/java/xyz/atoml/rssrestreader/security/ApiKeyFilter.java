package xyz.atoml.rssrestreader.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.atoml.rssrestreader.services.AuthService;

import java.io.IOException;
import java.util.Collections;

@Component
@Order(2)
public class ApiKeyFilter extends OncePerRequestFilter
{
    private static final String HEADER_API = "X-API-KEY";

    private final AuthService authServiceHelper;

    public ApiKeyFilter(AuthService authServiceHelper)
    {
        this.authServiceHelper = authServiceHelper;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        if (!authServiceHelper.isEnabled())
        {
            filterChain.doFilter(request, response);
            return;
        }

        String reqApiKey = request.getHeader(HEADER_API);
        boolean apiKeyInvalid = !authServiceHelper.validateApiKey(reqApiKey);

        if (apiKeyInvalid)
        {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid API Key");
            return;
        }

        var authToken = new UsernamePasswordAuthenticationToken(reqApiKey, reqApiKey, Collections.emptyList());

        SecurityContextHolder.getContext()
                             .setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
