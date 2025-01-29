package xyz.atoml.rssrestreader.exception;

public class RssFeedException extends RuntimeException
{
    public RssFeedException(String message) {
        super(message);
    }

    public RssFeedException(String message, Throwable cause) {
        super(message, cause);
    }
}