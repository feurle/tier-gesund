package com.feurle.tg.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.feurle.tg.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArticleImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticleImageDTO.class);
        ArticleImageDTO articleImageDTO1 = new ArticleImageDTO();
        articleImageDTO1.setId(1L);
        ArticleImageDTO articleImageDTO2 = new ArticleImageDTO();
        assertThat(articleImageDTO1).isNotEqualTo(articleImageDTO2);
        articleImageDTO2.setId(articleImageDTO1.getId());
        assertThat(articleImageDTO1).isEqualTo(articleImageDTO2);
        articleImageDTO2.setId(2L);
        assertThat(articleImageDTO1).isNotEqualTo(articleImageDTO2);
        articleImageDTO1.setId(null);
        assertThat(articleImageDTO1).isNotEqualTo(articleImageDTO2);
    }
}
