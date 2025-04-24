package com.feurle.tg.service;

import com.feurle.tg.service.dto.ArticleImageDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.feurle.tg.domain.ArticleImage}.
 */
public interface ArticleImageService {
    /**
     * Save a articleImage.
     *
     * @param articleImageDTO the entity to save.
     * @return the persisted entity.
     */
    ArticleImageDTO save(ArticleImageDTO articleImageDTO);

    /**
     * Updates a articleImage.
     *
     * @param articleImageDTO the entity to update.
     * @return the persisted entity.
     */
    ArticleImageDTO update(ArticleImageDTO articleImageDTO);

    /**
     * Partially updates a articleImage.
     *
     * @param articleImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ArticleImageDTO> partialUpdate(ArticleImageDTO articleImageDTO);

    /**
     * Get all the articleImages.
     *
     * @return the list of entities.
     */
    List<ArticleImageDTO> findAll();

    /**
     * Get all the ArticleImageDTO where NewsArticle is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ArticleImageDTO> findAllWhereNewsArticleIsNull();

    /**
     * Get the "id" articleImage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArticleImageDTO> findOne(Long id);

    /**
     * Delete the "id" articleImage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
