package xyz.atoml.rssrestreader.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ApiError
{
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;

    public ApiError(int status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.path = path;
    }
}