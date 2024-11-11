package xyz.atoml.rssrestreader.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import xyz.atoml.rssrestreader.security.RateLimitFilter;
import xyz.atoml.rssrestreader.services.AuthService;
import xyz.atoml.rssrestreader.security.ApiKeyFilter;
import xyz.atoml.rssrestreader.services.RateLimiterService;

@Configuration
public class WebSecurityConfig
{
    private final AuthService authServiceHelper;
    private final RateLimiterService rateLimiterService;

    public WebSecurityConfig(AuthService authServiceHelper,
                             RateLimiterService rateLimiterService)
    {
        this.authServiceHelper = authServiceHelper;
        this.rateLimiterService = rateLimiterService;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception
    {
        http.addFilterBefore(new ApiKeyFilter(authServiceHelper), AnonymousAuthenticationFilter.class);
        http.addFilterBefore(new RateLimitFilter(rateLimiterService), ApiKeyFilter.class);

        http.authorizeHttpRequests(requests -> requests.requestMatchers("/error").permitAll()
                                                       .anyRequest().authenticated()
        );

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
