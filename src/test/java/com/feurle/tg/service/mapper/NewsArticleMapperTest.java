package com.feurle.tg.service.mapper;

import static com.feurle.tg.domain.NewsArticleAsserts.*;
import static com.feurle.tg.domain.NewsArticleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NewsArticleMapperTest {

    private NewsArticleMapper newsArticleMapper;

    @BeforeEach
    void setUp() {
        newsArticleMapper = new NewsArticleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNewsArticleSample1();
        var actual = newsArticleMapper.toEntity(newsArticleMapper.toDto(expected));
        assertNewsArticleAllPropertiesEquals(expected, actual);
    }
}
