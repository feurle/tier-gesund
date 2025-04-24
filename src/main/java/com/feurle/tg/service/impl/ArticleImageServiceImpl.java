package com.feurle.tg.service.impl;

import com.feurle.tg.domain.ArticleImage;
import com.feurle.tg.repository.ArticleImageRepository;
import com.feurle.tg.service.ArticleImageService;
import com.feurle.tg.service.dto.ArticleImageDTO;
import com.feurle.tg.service.mapper.ArticleImageMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.feurle.tg.domain.ArticleImage}.
 */
@Service
@Transactional
public class ArticleImageServiceImpl implements ArticleImageService {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleImageServiceImpl.class);

    private final ArticleImageRepository articleImageRepository;

    private final ArticleImageMapper articleImageMapper;

    public ArticleImageServiceImpl(ArticleImageRepository articleImageRepository, ArticleImageMapper articleImageMapper) {
        this.articleImageRepository = articleImageRepository;
        this.articleImageMapper = articleImageMapper;
    }

    @Override
    public ArticleImageDTO save(ArticleImageDTO articleImageDTO) {
        LOG.debug("Request to save ArticleImage : {}", articleImageDTO);
        ArticleImage articleImage = articleImageMapper.toEntity(articleImageDTO);
        articleImage = articleImageRepository.save(articleImage);
        return articleImageMapper.toDto(articleImage);
    }

    @Override
    public ArticleImageDTO update(ArticleImageDTO articleImageDTO) {
        LOG.debug("Request to update ArticleImage : {}", articleImageDTO);
        ArticleImage articleImage = articleImageMapper.toEntity(articleImageDTO);
        articleImage = articleImageRepository.save(articleImage);
        return articleImageMapper.toDto(articleImage);
    }

    @Override
    public Optional<ArticleImageDTO> partialUpdate(ArticleImageDTO articleImageDTO) {
        LOG.debug("Request to partially update ArticleImage : {}", articleImageDTO);

        return articleImageRepository
            .findById(articleImageDTO.getId())
            .map(existingArticleImage -> {
                articleImageMapper.partialUpdate(existingArticleImage, articleImageDTO);

                return existingArticleImage;
            })
            .map(articleImageRepository::save)
            .map(articleImageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleImageDTO> findAll() {
        LOG.debug("Request to get all ArticleImages");
        return articleImageRepository.findAll().stream().map(articleImageMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the articleImages where NewsArticle is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ArticleImageDTO> findAllWhereNewsArticleIsNull() {
        LOG.debug("Request to get all articleImages where NewsArticle is null");
        return StreamSupport.stream(articleImageRepository.findAll().spliterator(), false)
            .filter(articleImage -> articleImage.getNewsArticle() == null)
            .map(articleImageMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArticleImageDTO> findOne(Long id) {
        LOG.debug("Request to get ArticleImage : {}", id);
        return articleImageRepository.findById(id).map(articleImageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ArticleImage : {}", id);
        articleImageRepository.deleteById(id);
    }
}
