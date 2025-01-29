package xyz.atoml.rssrestreader.config;

import jakarta.servlet.DispatcherType;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.ForwardedHeaderFilter;
import xyz.atoml.rssrestreader.security.ApiKeyFilter;
import xyz.atoml.rssrestreader.security.RateLimitFilter;

@Configuration
public class FiltersConfig
{
    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilterRegistration(RateLimitFilter rateLimitFilter)
    {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(rateLimitFilter);
        registrationBean.addUrlPatterns("/rss/*");

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistration(ApiKeyFilter apiKeyFilter)
    {
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(apiKeyFilter);
        registrationBean.addUrlPatterns("/rss/*");

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter()
    {
        ForwardedHeaderFilter filter = new ForwardedHeaderFilter();
        FilterRegistrationBean<ForwardedHeaderFilter> registration = new FilterRegistrationBean<>(filter);
        
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.addUrlPatterns("*");
        
        return registration;
    }
}
