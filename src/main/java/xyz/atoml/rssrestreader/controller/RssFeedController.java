package xyz.atoml.rssrestreader.controller;

import com.apptasticsoftware.rssreader.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.atoml.rssrestreader.core.logging.AppLogger;
import xyz.atoml.rssrestreader.exception.RssFeedException;
import xyz.atoml.rssrestreader.services.RssFeedService;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/rss")
public class RssFeedController
{
    private static final Pattern URL_PATTERN = Pattern.compile("^(http|https)://[\\w.-]+(:\\d+)?(/.*)?$");

    private final RssFeedService rssFeedService;

    @Autowired
    public RssFeedController(RssFeedService rssFeedService)
    {
        this.rssFeedService = rssFeedService;
    }

    @GetMapping("/read")
    public ResponseEntity<List<Item>> getRssFeed(@RequestParam String url)
    {
        if (!URL_PATTERN.matcher(url).matches()) {
            throw new RssFeedException("Invalid URL format");
        }

        List<Item> items = rssFeedService.fetchRssFeed(url);
        AppLogger.info("Processed RSS feed with " + items.size() +  " items from URL: " + url);

        return ResponseEntity.ok(items);
    }
}