package com.feurle.tg.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.feurle.tg.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NewsArticleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NewsArticleDTO.class);
        NewsArticleDTO newsArticleDTO1 = new NewsArticleDTO();
        newsArticleDTO1.setId(1L);
        NewsArticleDTO newsArticleDTO2 = new NewsArticleDTO();
        assertThat(newsArticleDTO1).isNotEqualTo(newsArticleDTO2);
        newsArticleDTO2.setId(newsArticleDTO1.getId());
        assertThat(newsArticleDTO1).isEqualTo(newsArticleDTO2);
        newsArticleDTO2.setId(2L);
        assertThat(newsArticleDTO1).isNotEqualTo(newsArticleDTO2);
        newsArticleDTO1.setId(null);
        assertThat(newsArticleDTO1).isNotEqualTo(newsArticleDTO2);
    }
}
