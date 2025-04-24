package com.feurle.tg.service;

import com.feurle.tg.service.dto.NewsArticleDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.feurle.tg.domain.NewsArticle}.
 */
public interface NewsArticleService {
    /**
     * Save a newsArticle.
     *
     * @param newsArticleDTO the entity to save.
     * @return the persisted entity.
     */
    NewsArticleDTO save(NewsArticleDTO newsArticleDTO);

    /**
     * Updates a newsArticle.
     *
     * @param newsArticleDTO the entity to update.
     * @return the persisted entity.
     */
    NewsArticleDTO update(NewsArticleDTO newsArticleDTO);

    /**
     * Partially updates a newsArticle.
     *
     * @param newsArticleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NewsArticleDTO> partialUpdate(NewsArticleDTO newsArticleDTO);

    /**
     * Get all the newsArticles.
     *
     * @return the list of entities.
     */
    List<NewsArticleDTO> findAll();

    /**
     * Get the "id" newsArticle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NewsArticleDTO> findOne(Long id);

    /**
     * Delete the "id" newsArticle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
