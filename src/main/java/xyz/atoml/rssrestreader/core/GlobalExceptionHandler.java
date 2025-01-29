package xyz.atoml.rssrestreader.core;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import xyz.atoml.rssrestreader.exception.RssFeedException;
import xyz.atoml.rssrestreader.model.ApiError;

@ControllerAdvice
public class GlobalExceptionHandler
{

    @ExceptionHandler(RssFeedException.class)
    public ResponseEntity<ApiError> handleRssFeedException(RssFeedException e, HttpServletRequest request)
    {
        ApiError error = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception e, HttpServletRequest request)
    {
        ApiError error = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred",
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}