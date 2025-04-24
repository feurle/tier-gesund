package com.feurle.tg.web.rest;

import static com.feurle.tg.domain.ArticleImageAsserts.*;
import static com.feurle.tg.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feurle.tg.IntegrationTest;
import com.feurle.tg.domain.ArticleImage;
import com.feurle.tg.repository.ArticleImageRepository;
import com.feurle.tg.service.dto.ArticleImageDTO;
import com.feurle.tg.service.mapper.ArticleImageMapper;
import jakarta.persistence.EntityManager;
import java.util.Base64;
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
 * Integration tests for the {@link ArticleImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArticleImageResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/article-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArticleImageRepository articleImageRepository;

    @Autowired
    private ArticleImageMapper articleImageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleImageMockMvc;

    private ArticleImage articleImage;

    private ArticleImage insertedArticleImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleImage createEntity() {
        return new ArticleImage().title(DEFAULT_TITLE).image(DEFAULT_IMAGE).imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleImage createUpdatedEntity() {
        return new ArticleImage().title(UPDATED_TITLE).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @BeforeEach
    void initTest() {
        articleImage = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedArticleImage != null) {
            articleImageRepository.delete(insertedArticleImage);
            insertedArticleImage = null;
        }
    }

    @Test
    @Transactional
    void createArticleImage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ArticleImage
        ArticleImageDTO articleImageDTO = articleImageMapper.toDto(articleImage);
        var returnedArticleImageDTO = om.readValue(
            restArticleImageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleImageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArticleImageDTO.class
        );

        // Validate the ArticleImage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedArticleImage = articleImageMapper.toEntity(returnedArticleImageDTO);
        assertArticleImageUpdatableFieldsEquals(returnedArticleImage, getPersistedArticleImage(returnedArticleImage));

        insertedArticleImage = returnedArticleImage;
    }

    @Test
    @Transactional
    void createArticleImageWithExistingId() throws Exception {
        // Create the ArticleImage with an existing ID
        articleImage.setId(1L);
        ArticleImageDTO articleImageDTO = articleImageMapper.toDto(articleImage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleImageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArticleImage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        articleImage.setTitle(null);

        // Create the ArticleImage, which fails.
        ArticleImageDTO articleImageDTO = articleImageMapper.toDto(articleImage);

        restArticleImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleImageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArticleImages() throws Exception {
        // Initialize the database
        insertedArticleImage = articleImageRepository.saveAndFlush(articleImage);

        // Get all the articleImageList
        restArticleImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(articleImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getArticleImage() throws Exception {
        // Initialize the database
        insertedArticleImage = articleImageRepository.saveAndFlush(articleImage);

        // Get the articleImage
        restArticleImageMockMvc
            .perform(get(ENTITY_API_URL_ID, articleImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(articleImage.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64.getEncoder().encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingArticleImage() throws Exception {
        // Get the articleImage
        restArticleImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArticleImage() throws Exception {
        // Initialize the database
        insertedArticleImage = articleImageRepository.saveAndFlush(articleImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articleImage
        ArticleImage updatedArticleImage = articleImageRepository.findById(articleImage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArticleImage are not directly saved in db
        em.detach(updatedArticleImage);
        updatedArticleImage.title(UPDATED_TITLE).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        ArticleImageDTO articleImageDTO = articleImageMapper.toDto(updatedArticleImage);

        restArticleImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArticleImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArticleImageToMatchAllProperties(updatedArticleImage);
    }

    @Test
    @Transactional
    void putNonExistingArticleImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleImage.setId(longCount.incrementAndGet());

        // Create the ArticleImage
        ArticleImageDTO articleImageDTO = articleImageMapper.toDto(articleImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArticleImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleImage.setId(longCount.incrementAndGet());

        // Create the ArticleImage
        ArticleImageDTO articleImageDTO = articleImageMapper.toDto(articleImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArticleImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleImage.setId(longCount.incrementAndGet());

        // Create the ArticleImage
        ArticleImageDTO articleImageDTO = articleImageMapper.toDto(articleImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArticleImageWithPatch() throws Exception {
        // Initialize the database
        insertedArticleImage = articleImageRepository.saveAndFlush(articleImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articleImage using partial update
        ArticleImage partialUpdatedArticleImage = new ArticleImage();
        partialUpdatedArticleImage.setId(articleImage.getId());

        partialUpdatedArticleImage.image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restArticleImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticleImage))
            )
            .andExpect(status().isOk());

        // Validate the ArticleImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticleImageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedArticleImage, articleImage),
            getPersistedArticleImage(articleImage)
        );
    }

    @Test
    @Transactional
    void fullUpdateArticleImageWithPatch() throws Exception {
        // Initialize the database
        insertedArticleImage = articleImageRepository.saveAndFlush(articleImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articleImage using partial update
        ArticleImage partialUpdatedArticleImage = new ArticleImage();
        partialUpdatedArticleImage.setId(articleImage.getId());

        partialUpdatedArticleImage.title(UPDATED_TITLE).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restArticleImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticleImage))
            )
            .andExpect(status().isOk());

        // Validate the ArticleImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticleImageUpdatableFieldsEquals(partialUpdatedArticleImage, getPersistedArticleImage(partialUpdatedArticleImage));
    }

    @Test
    @Transactional
    void patchNonExistingArticleImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleImage.setId(longCount.incrementAndGet());

        // Create the ArticleImage
        ArticleImageDTO articleImageDTO = articleImageMapper.toDto(articleImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, articleImageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(articleImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArticleImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleImage.setId(longCount.incrementAndGet());

        // Create the ArticleImage
        ArticleImageDTO articleImageDTO = articleImageMapper.toDto(articleImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(articleImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArticleImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleImage.setId(longCount.incrementAndGet());

        // Create the ArticleImage
        ArticleImageDTO articleImageDTO = articleImageMapper.toDto(articleImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleImageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(articleImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArticleImage() throws Exception {
        // Initialize the database
        insertedArticleImage = articleImageRepository.saveAndFlush(articleImage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the articleImage
        restArticleImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, articleImage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return articleImageRepository.count();
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

    protected ArticleImage getPersistedArticleImage(ArticleImage articleImage) {
        return articleImageRepository.findById(articleImage.getId()).orElseThrow();
    }

    protected void assertPersistedArticleImageToMatchAllProperties(ArticleImage expectedArticleImage) {
        assertArticleImageAllPropertiesEquals(expectedArticleImage, getPersistedArticleImage(expectedArticleImage));
    }

    protected void assertPersistedArticleImageToMatchUpdatableProperties(ArticleImage expectedArticleImage) {
        assertArticleImageAllUpdatablePropertiesEquals(expectedArticleImage, getPersistedArticleImage(expectedArticleImage));
    }
}
