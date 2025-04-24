package com.feurle.tg.domain;

import static com.feurle.tg.domain.ArticleImageTestSamples.*;
import static com.feurle.tg.domain.NewsArticleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.feurle.tg.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NewsArticleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NewsArticle.class);
        NewsArticle newsArticle1 = getNewsArticleSample1();
        NewsArticle newsArticle2 = new NewsArticle();
        assertThat(newsArticle1).isNotEqualTo(newsArticle2);

        newsArticle2.setId(newsArticle1.getId());
        assertThat(newsArticle1).isEqualTo(newsArticle2);

        newsArticle2 = getNewsArticleSample2();
        assertThat(newsArticle1).isNotEqualTo(newsArticle2);
    }

    @Test
    void articleImageTest() {
        NewsArticle newsArticle = getNewsArticleRandomSampleGenerator();
        ArticleImage articleImageBack = getArticleImageRandomSampleGenerator();

        newsArticle.setArticleImage(articleImageBack);
        assertThat(newsArticle.getArticleImage()).isEqualTo(articleImageBack);

        newsArticle.articleImage(null);
        assertThat(newsArticle.getArticleImage()).isNull();
    }
}
