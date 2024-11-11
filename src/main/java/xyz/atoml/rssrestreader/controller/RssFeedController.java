package xyz.atoml.rssrestreader.controller;

import com.apptasticsoftware.rssreader.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.atoml.rssrestreader.services.RssFeedService;
import xyz.atoml.rssrestreader.utils.AppLogger;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@RestController
public class RssFeedController
{
    private static final Pattern URL_PATTERN = Pattern.compile("^(http|https)://[\\w.-]+(:\\d+)?(/.*)?$");

    private final RssFeedService rssFeedService;

    @Autowired
    public RssFeedController(RssFeedService rssFeedService)
    {
        this.rssFeedService = rssFeedService;
    }

    @GetMapping("/rss/read")
    public ResponseEntity<?> getRssFeed(@RequestParam String url)
    {
        if (!URL_PATTERN.matcher(url).matches())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid URL format.");
        }

        try
        {
            List<Item> items = rssFeedService.fetchRssFeed(url);
            AppLogger.info("Processed RSS URL: " + url);

            return ResponseEntity.ok(items);
        }
        catch (IOException e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to fetch RSS feed from the provided URL.");
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Invalid URL format.");
        }
    }

}
