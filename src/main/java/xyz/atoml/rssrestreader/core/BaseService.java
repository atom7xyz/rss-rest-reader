package xyz.atoml.rssrestreader.core;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class BaseService
{
    protected boolean enabled;

    protected abstract void logConfiguration();

    @PostConstruct
    protected void init() {
        logConfiguration();
    }
}
