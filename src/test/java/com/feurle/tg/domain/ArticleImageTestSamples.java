package com.feurle.tg.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ArticleImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ArticleImage getArticleImageSample1() {
        return new ArticleImage().id(1L).title("title1");
    }

    public static ArticleImage getArticleImageSample2() {
        return new ArticleImage().id(2L).title("title2");
    }

    public static ArticleImage getArticleImageRandomSampleGenerator() {
        return new ArticleImage().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString());
    }
}
