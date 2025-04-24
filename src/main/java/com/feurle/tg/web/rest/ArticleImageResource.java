package com.feurle.tg.web.rest;

import com.feurle.tg.repository.ArticleImageRepository;
import com.feurle.tg.service.ArticleImageService;
import com.feurle.tg.service.dto.ArticleImageDTO;
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
 * REST controller for managing {@link com.feurle.tg.domain.ArticleImage}.
 */
@RestController
@RequestMapping("/api/article-images")
public class ArticleImageResource {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleImageResource.class);

    private static final String ENTITY_NAME = "articleImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArticleImageService articleImageService;

    private final ArticleImageRepository articleImageRepository;

    public ArticleImageResource(ArticleImageService articleImageService, ArticleImageRepository articleImageRepository) {
        this.articleImageService = articleImageService;
        this.articleImageRepository = articleImageRepository;
    }

    /**
     * {@code POST  /article-images} : Create a new articleImage.
     *
     * @param articleImageDTO the articleImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new articleImageDTO, or with status {@code 400 (Bad Request)} if the articleImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArticleImageDTO> createArticleImage(@Valid @RequestBody ArticleImageDTO articleImageDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ArticleImage : {}", articleImageDTO);
        if (articleImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new articleImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        articleImageDTO = articleImageService.save(articleImageDTO);
        return ResponseEntity.created(new URI("/api/article-images/" + articleImageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, articleImageDTO.getId().toString()))
            .body(articleImageDTO);
    }

    /**
     * {@code PUT  /article-images/:id} : Updates an existing articleImage.
     *
     * @param id the id of the articleImageDTO to save.
     * @param articleImageDTO the articleImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated articleImageDTO,
     * or with status {@code 400 (Bad Request)} if the articleImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the articleImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArticleImageDTO> updateArticleImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArticleImageDTO articleImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ArticleImage : {}, {}", id, articleImageDTO);
        if (articleImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, articleImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articleImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        articleImageDTO = articleImageService.update(articleImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, articleImageDTO.getId().toString()))
            .body(articleImageDTO);
    }

    /**
     * {@code PATCH  /article-images/:id} : Partial updates given fields of an existing articleImage, field will ignore if it is null
     *
     * @param id the id of the articleImageDTO to save.
     * @param articleImageDTO the articleImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated articleImageDTO,
     * or with status {@code 400 (Bad Request)} if the articleImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the articleImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the articleImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArticleImageDTO> partialUpdateArticleImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArticleImageDTO articleImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ArticleImage partially : {}, {}", id, articleImageDTO);
        if (articleImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, articleImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articleImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArticleImageDTO> result = articleImageService.partialUpdate(articleImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, articleImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /article-images} : get all the articleImages.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of articleImages in body.
     */
    @GetMapping("")
    public List<ArticleImageDTO> getAllArticleImages(@RequestParam(name = "filter", required = false) String filter) {
        if ("newsarticle-is-null".equals(filter)) {
            LOG.debug("REST request to get all ArticleImages where newsArticle is null");
            return articleImageService.findAllWhereNewsArticleIsNull();
        }
        LOG.debug("REST request to get all ArticleImages");
        return articleImageService.findAll();
    }

    /**
     * {@code GET  /article-images/:id} : get the "id" articleImage.
     *
     * @param id the id of the articleImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the articleImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleImageDTO> getArticleImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ArticleImage : {}", id);
        Optional<ArticleImageDTO> articleImageDTO = articleImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(articleImageDTO);
    }

    /**
     * {@code DELETE  /article-images/:id} : delete the "id" articleImage.
     *
     * @param id the id of the articleImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticleImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ArticleImage : {}", id);
        articleImageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
