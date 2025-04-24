package com.feurle.tg.repository;

import com.feurle.tg.domain.ArticleImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArticleImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {}
