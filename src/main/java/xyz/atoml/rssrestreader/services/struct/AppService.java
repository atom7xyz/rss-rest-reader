package xyz.atoml.rssrestreader.services.struct;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppService
{
    protected boolean enabled;

    public AppService(boolean value)
    {
        this.enabled = value;
    }
}
