package com.feurle.tg.domain;

import static com.feurle.tg.domain.ArticleImageTestSamples.*;
import static com.feurle.tg.domain.NewsArticleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.feurle.tg.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArticleImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticleImage.class);
        ArticleImage articleImage1 = getArticleImageSample1();
        ArticleImage articleImage2 = new ArticleImage();
        assertThat(articleImage1).isNotEqualTo(articleImage2);

        articleImage2.setId(articleImage1.getId());
        assertThat(articleImage1).isEqualTo(articleImage2);

        articleImage2 = getArticleImageSample2();
        assertThat(articleImage1).isNotEqualTo(articleImage2);
    }

    @Test
    void newsArticleTest() {
        ArticleImage articleImage = getArticleImageRandomSampleGenerator();
        NewsArticle newsArticleBack = getNewsArticleRandomSampleGenerator();

        articleImage.setNewsArticle(newsArticleBack);
        assertThat(articleImage.getNewsArticle()).isEqualTo(newsArticleBack);
        assertThat(newsArticleBack.getArticleImage()).isEqualTo(articleImage);

        articleImage.newsArticle(null);
        assertThat(articleImage.getNewsArticle()).isNull();
        assertThat(newsArticleBack.getArticleImage()).isNull();
    }
}
