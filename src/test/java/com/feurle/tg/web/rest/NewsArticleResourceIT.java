package com.feurle.tg.web.rest;

import static com.feurle.tg.domain.NewsArticleAsserts.*;
import static com.feurle.tg.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feurle.tg.IntegrationTest;
import com.feurle.tg.domain.NewsArticle;
import com.feurle.tg.domain.enumeration.Language;
import com.feurle.tg.domain.enumeration.Location;
import com.feurle.tg.domain.enumeration.State;
import com.feurle.tg.repository.NewsArticleRepository;
import com.feurle.tg.repository.UserRepository;
import com.feurle.tg.service.dto.NewsArticleDTO;
import com.feurle.tg.service.mapper.NewsArticleMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NewsArticleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NewsArticleResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final State DEFAULT_STATE = State.PUBLISHED;
    private static final State UPDATED_STATE = State.CLOSED;

    private static final Instant DEFAULT_PUBLISHED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUBLISHED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final Language DEFAULT_LANGUAGE = Language.GERMAN;
    private static final Language UPDATED_LANGUAGE = Language.ENGLISH;

    private static final Location DEFAULT_LOCATION = Location.TEASER;
    private static final Location UPDATED_LOCATION = Location.HOME;

    private static final String ENTITY_API_URL = "/api/news-articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NewsArticleRepository newsArticleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NewsArticleMapper newsArticleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNewsArticleMockMvc;

    private NewsArticle newsArticle;

    private NewsArticle insertedNewsArticle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NewsArticle createEntity() {
        return new NewsArticle()
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .state(DEFAULT_STATE)
            .publishedDate(DEFAULT_PUBLISHED_DATE)
            .author(DEFAULT_AUTHOR)
            .language(DEFAULT_LANGUAGE)
            .location(DEFAULT_LOCATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NewsArticle createUpdatedEntity() {
        return new NewsArticle()
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .state(UPDATED_STATE)
            .publishedDate(UPDATED_PUBLISHED_DATE)
            .author(UPDATED_AUTHOR)
            .language(UPDATED_LANGUAGE)
            .location(UPDATED_LOCATION);
    }

    @BeforeEach
    void initTest() {
        newsArticle = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedNewsArticle != null) {
            newsArticleRepository.delete(insertedNewsArticle);
            insertedNewsArticle = null;
        }
    }

    @Test
    @Transactional
    void createNewsArticle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NewsArticle
        NewsArticleDTO newsArticleDTO = newsArticleMapper.toDto(newsArticle);
        var returnedNewsArticleDTO = om.readValue(
            restNewsArticleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(newsArticleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NewsArticleDTO.class
        );

        // Validate the NewsArticle in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNewsArticle = newsArticleMapper.toEntity(returnedNewsArticleDTO);
        assertNewsArticleUpdatableFieldsEquals(returnedNewsArticle, getPersistedNewsArticle(returnedNewsArticle));

        insertedNewsArticle = returnedNewsArticle;
    }

    @Test
    @Transactional
    void createNewsArticleWithExistingId() throws Exception {
        // Create the NewsArticle with an existing ID
        newsArticle.setId(1L);
        NewsArticleDTO newsArticleDTO = newsArticleMapper.toDto(newsArticle);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNewsArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(newsArticleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NewsArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        newsArticle.setTitle(null);

        // Create the NewsArticle, which fails.
        NewsArticleDTO newsArticleDTO = newsArticleMapper.toDto(newsArticle);

        restNewsArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(newsArticleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNewsArticles() throws Exception {
        // Initialize the database
        insertedNewsArticle = newsArticleRepository.saveAndFlush(newsArticle);

        // Get all the newsArticleList
        restNewsArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(newsArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(DEFAULT_PUBLISHED_DATE.toString())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())));
    }

    @Test
    @Transactional
    void getNewsArticle() throws Exception {
        // Initialize the database
        insertedNewsArticle = newsArticleRepository.saveAndFlush(newsArticle);

        // Get the newsArticle
        restNewsArticleMockMvc
            .perform(get(ENTITY_API_URL_ID, newsArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(newsArticle.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.publishedDate").value(DEFAULT_PUBLISHED_DATE.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNewsArticle() throws Exception {
        // Get the newsArticle
        restNewsArticleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNewsArticle() throws Exception {
        // Initialize the database
        insertedNewsArticle = newsArticleRepository.saveAndFlush(newsArticle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the newsArticle
        NewsArticle updatedNewsArticle = newsArticleRepository.findById(newsArticle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNewsArticle are not directly saved in db
        em.detach(updatedNewsArticle);
        updatedNewsArticle
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .state(UPDATED_STATE)
            .publishedDate(UPDATED_PUBLISHED_DATE)
            .author(UPDATED_AUTHOR)
            .language(UPDATED_LANGUAGE)
            .location(UPDATED_LOCATION);
        NewsArticleDTO newsArticleDTO = newsArticleMapper.toDto(updatedNewsArticle);

        restNewsArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, newsArticleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(newsArticleDTO))
            )
            .andExpect(status().isOk());

        // Validate the NewsArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNewsArticleToMatchAllProperties(updatedNewsArticle);
    }

    @Test
    @Transactional
    void putNonExistingNewsArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        newsArticle.setId(longCount.incrementAndGet());

        // Create the NewsArticle
        NewsArticleDTO newsArticleDTO = newsArticleMapper.toDto(newsArticle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNewsArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, newsArticleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(newsArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NewsArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNewsArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        newsArticle.setId(longCount.incrementAndGet());

        // Create the NewsArticle
        NewsArticleDTO newsArticleDTO = newsArticleMapper.toDto(newsArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(newsArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NewsArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNewsArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        newsArticle.setId(longCount.incrementAndGet());

        // Create the NewsArticle
        NewsArticleDTO newsArticleDTO = newsArticleMapper.toDto(newsArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsArticleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(newsArticleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NewsArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNewsArticleWithPatch() throws Exception {
        // Initialize the database
        insertedNewsArticle = newsArticleRepository.saveAndFlush(newsArticle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the newsArticle using partial update
        NewsArticle partialUpdatedNewsArticle = new NewsArticle();
        partialUpdatedNewsArticle.setId(newsArticle.getId());

        partialUpdatedNewsArticle.title(UPDATED_TITLE).content(UPDATED_CONTENT).location(UPDATED_LOCATION);

        restNewsArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNewsArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNewsArticle))
            )
            .andExpect(status().isOk());

        // Validate the NewsArticle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNewsArticleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNewsArticle, newsArticle),
            getPersistedNewsArticle(newsArticle)
        );
    }

    @Test
    @Transactional
    void fullUpdateNewsArticleWithPatch() throws Exception {
        // Initialize the database
        insertedNewsArticle = newsArticleRepository.saveAndFlush(newsArticle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the newsArticle using partial update
        NewsArticle partialUpdatedNewsArticle = new NewsArticle();
        partialUpdatedNewsArticle.setId(newsArticle.getId());

        partialUpdatedNewsArticle
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .state(UPDATED_STATE)
            .publishedDate(UPDATED_PUBLISHED_DATE)
            .author(UPDATED_AUTHOR)
            .language(UPDATED_LANGUAGE)
            .location(UPDATED_LOCATION);

        restNewsArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNewsArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNewsArticle))
            )
            .andExpect(status().isOk());

        // Validate the NewsArticle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNewsArticleUpdatableFieldsEquals(partialUpdatedNewsArticle, getPersistedNewsArticle(partialUpdatedNewsArticle));
    }

    @Test
    @Transactional
    void patchNonExistingNewsArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        newsArticle.setId(longCount.incrementAndGet());

        // Create the NewsArticle
        NewsArticleDTO newsArticleDTO = newsArticleMapper.toDto(newsArticle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNewsArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, newsArticleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(newsArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NewsArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNewsArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        newsArticle.setId(longCount.incrementAndGet());

        // Create the NewsArticle
        NewsArticleDTO newsArticleDTO = newsArticleMapper.toDto(newsArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(newsArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NewsArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNewsArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        newsArticle.setId(longCount.incrementAndGet());

        // Create the NewsArticle
        NewsArticleDTO newsArticleDTO = newsArticleMapper.toDto(newsArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsArticleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(newsArticleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NewsArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNewsArticle() throws Exception {
        // Initialize the database
        insertedNewsArticle = newsArticleRepository.saveAndFlush(newsArticle);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the newsArticle
        restNewsArticleMockMvc
            .perform(delete(ENTITY_API_URL_ID, newsArticle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return newsArticleRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected NewsArticle getPersistedNewsArticle(NewsArticle newsArticle) {
        return newsArticleRepository.findById(newsArticle.getId()).orElseThrow();
    }

    protected void assertPersistedNewsArticleToMatchAllProperties(NewsArticle expectedNewsArticle) {
        assertNewsArticleAllPropertiesEquals(expectedNewsArticle, getPersistedNewsArticle(expectedNewsArticle));
    }

    protected void assertPersistedNewsArticleToMatchUpdatableProperties(NewsArticle expectedNewsArticle) {
        assertNewsArticleAllUpdatablePropertiesEquals(expectedNewsArticle, getPersistedNewsArticle(expectedNewsArticle));
    }
}
