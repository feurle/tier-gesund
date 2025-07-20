package com.feurle.tg.repository;

import com.feurle.tg.domain.NewsArticle;
import com.feurle.tg.domain.enumeration.Location;
import com.feurle.tg.domain.enumeration.State;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NewsArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    @Query("select newsArticle from NewsArticle newsArticle where newsArticle.user.login = ?#{authentication.name}")
    List<NewsArticle> findByUserIsCurrentUser();

    List<NewsArticle> findAllByLocation(Location location);

    List<NewsArticle> findAllByState(State state);
}
