package com.feurle.tg.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NewsArticleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NewsArticle getNewsArticleSample1() {
        return new NewsArticle().id(1L).title("title1").author("author1");
    }

    public static NewsArticle getNewsArticleSample2() {
        return new NewsArticle().id(2L).title("title2").author("author2");
    }

    public static NewsArticle getNewsArticleRandomSampleGenerator() {
        return new NewsArticle().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString()).author(UUID.randomUUID().toString());
    }
}
