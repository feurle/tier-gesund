package com.feurle.tg.web.rest;

import com.feurle.tg.repository.NewsArticleRepository;
import com.feurle.tg.service.NewsArticleService;
import com.feurle.tg.service.dto.NewsArticleDTO;
import com.feurle.tg.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.feurle.tg.domain.NewsArticle}.
 */
@RestController
@RequestMapping("/api/news-articles")
public class NewsArticleResource {

    private static final Logger LOG = LoggerFactory.getLogger(NewsArticleResource.class);

    private static final String ENTITY_NAME = "newsArticle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NewsArticleService newsArticleService;

    private final NewsArticleRepository newsArticleRepository;

    public NewsArticleResource(NewsArticleService newsArticleService, NewsArticleRepository newsArticleRepository) {
        this.newsArticleService = newsArticleService;
        this.newsArticleRepository = newsArticleRepository;
    }

    /**
     * {@code POST  /news-articles} : Create a new newsArticle.
     *
     * @param newsArticleDTO the newsArticleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new newsArticleDTO, or with status {@code 400 (Bad Request)} if the newsArticle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NewsArticleDTO> createNewsArticle(@Valid @RequestBody NewsArticleDTO newsArticleDTO) throws URISyntaxException {
        LOG.debug("REST request to save NewsArticle : {}", newsArticleDTO);
        if (newsArticleDTO.getId() != null) {
            throw new BadRequestAlertException("A new newsArticle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        newsArticleDTO = newsArticleService.save(newsArticleDTO);
        return ResponseEntity.created(new URI("/api/news-articles/" + newsArticleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, newsArticleDTO.getId().toString()))
            .body(newsArticleDTO);
    }

    /**
     * {@code PUT  /news-articles/:id} : Updates an existing newsArticle.
     *
     * @param id the id of the newsArticleDTO to save.
     * @param newsArticleDTO the newsArticleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated newsArticleDTO,
     * or with status {@code 400 (Bad Request)} if the newsArticleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the newsArticleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NewsArticleDTO> updateNewsArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NewsArticleDTO newsArticleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NewsArticle : {}, {}", id, newsArticleDTO);
        if (newsArticleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, newsArticleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!newsArticleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        newsArticleDTO = newsArticleService.update(newsArticleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, newsArticleDTO.getId().toString()))
            .body(newsArticleDTO);
    }

    /**
     * {@code PATCH  /news-articles/:id} : Partial updates given fields of an existing newsArticle, field will ignore if it is null
     *
     * @param id the id of the newsArticleDTO to save.
     * @param newsArticleDTO the newsArticleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated newsArticleDTO,
     * or with status {@code 400 (Bad Request)} if the newsArticleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the newsArticleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the newsArticleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NewsArticleDTO> partialUpdateNewsArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NewsArticleDTO newsArticleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NewsArticle partially : {}, {}", id, newsArticleDTO);
        if (newsArticleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, newsArticleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!newsArticleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NewsArticleDTO> result = newsArticleService.partialUpdate(newsArticleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, newsArticleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /news-articles} : get all the newsArticles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of newsArticles in body.
     */
    @GetMapping("")
    public List<NewsArticleDTO> getAllNewsArticles() {
        LOG.debug("REST request to get all NewsArticles");
        return newsArticleService.findAll();
    }

    /**
     * {@code GET  /news-articles/:id} : get the "id" newsArticle.
     *
     * @param id the id of the newsArticleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the newsArticleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NewsArticleDTO> getNewsArticle(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NewsArticle : {}", id);
        Optional<NewsArticleDTO> newsArticleDTO = newsArticleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(newsArticleDTO);
    }

    /**
     * {@code DELETE  /news-articles/:id} : delete the "id" newsArticle.
     *
     * @param id the id of the newsArticleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewsArticle(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NewsArticle : {}", id);
        newsArticleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
