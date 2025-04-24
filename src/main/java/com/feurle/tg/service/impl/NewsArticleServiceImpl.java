package com.feurle.tg.service.impl;

import com.feurle.tg.domain.NewsArticle;
import com.feurle.tg.repository.NewsArticleRepository;
import com.feurle.tg.service.NewsArticleService;
import com.feurle.tg.service.dto.NewsArticleDTO;
import com.feurle.tg.service.mapper.NewsArticleMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.feurle.tg.domain.NewsArticle}.
 */
@Service
@Transactional
public class NewsArticleServiceImpl implements NewsArticleService {

    private static final Logger LOG = LoggerFactory.getLogger(NewsArticleServiceImpl.class);

    private final NewsArticleRepository newsArticleRepository;

    private final NewsArticleMapper newsArticleMapper;

    public NewsArticleServiceImpl(NewsArticleRepository newsArticleRepository, NewsArticleMapper newsArticleMapper) {
        this.newsArticleRepository = newsArticleRepository;
        this.newsArticleMapper = newsArticleMapper;
    }

    @Override
    public NewsArticleDTO save(NewsArticleDTO newsArticleDTO) {
        LOG.debug("Request to save NewsArticle : {}", newsArticleDTO);
        NewsArticle newsArticle = newsArticleMapper.toEntity(newsArticleDTO);
        newsArticle = newsArticleRepository.save(newsArticle);
        return newsArticleMapper.toDto(newsArticle);
    }

    @Override
    public NewsArticleDTO update(NewsArticleDTO newsArticleDTO) {
        LOG.debug("Request to update NewsArticle : {}", newsArticleDTO);
        NewsArticle newsArticle = newsArticleMapper.toEntity(newsArticleDTO);
        newsArticle = newsArticleRepository.save(newsArticle);
        return newsArticleMapper.toDto(newsArticle);
    }

    @Override
    public Optional<NewsArticleDTO> partialUpdate(NewsArticleDTO newsArticleDTO) {
        LOG.debug("Request to partially update NewsArticle : {}", newsArticleDTO);

        return newsArticleRepository
            .findById(newsArticleDTO.getId())
            .map(existingNewsArticle -> {
                newsArticleMapper.partialUpdate(existingNewsArticle, newsArticleDTO);

                return existingNewsArticle;
            })
            .map(newsArticleRepository::save)
            .map(newsArticleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsArticleDTO> findAll() {
        LOG.debug("Request to get all NewsArticles");
        return newsArticleRepository.findAll().stream().map(newsArticleMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NewsArticleDTO> findOne(Long id) {
        LOG.debug("Request to get NewsArticle : {}", id);
        return newsArticleRepository.findById(id).map(newsArticleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete NewsArticle : {}", id);
        newsArticleRepository.deleteById(id);
    }
}
