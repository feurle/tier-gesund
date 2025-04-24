package com.feurle.tg.service.mapper;

import static com.feurle.tg.domain.ArticleImageAsserts.*;
import static com.feurle.tg.domain.ArticleImageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArticleImageMapperTest {

    private ArticleImageMapper articleImageMapper;

    @BeforeEach
    void setUp() {
        articleImageMapper = new ArticleImageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getArticleImageSample1();
        var actual = articleImageMapper.toEntity(articleImageMapper.toDto(expected));
        assertArticleImageAllPropertiesEquals(expected, actual);
    }
}
