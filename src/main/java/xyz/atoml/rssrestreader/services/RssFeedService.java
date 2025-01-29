package xyz.atoml.rssrestreader.services;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import xyz.atoml.rssrestreader.exception.RssFeedException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RssFeedService
{
    private final RssReader rssReader = new RssReader();

    public List<Item> fetchRssFeed(@NonNull String rssUrl) throws RssFeedException
    {
        try {
            Stream<Item> rssItems = rssReader.read(rssUrl);
            return rssItems.collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new RssFeedException("Failed to fetch RSS feed from the provided URL", e);
        }
    }
}
