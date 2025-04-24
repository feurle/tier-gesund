package com.feurle.tg.repository;

import com.feurle.tg.domain.NewsArticle;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NewsArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    @Query("select newsArticle from NewsArticle newsArticle where newsArticle.user.login = ?#{authentication.name}")
    List<NewsArticle> findByUserIsCurrentUser();
}
