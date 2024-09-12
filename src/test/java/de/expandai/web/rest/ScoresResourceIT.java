package de.expandai.web.rest;

import static de.expandai.domain.ScoresAsserts.*;
import static de.expandai.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.expandai.IntegrationTest;
import de.expandai.domain.Scores;
import de.expandai.repository.ScoresRepository;
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
 * Integration tests for the {@link ScoresResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScoresResourceIT {

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_QUESTIONNAIRE = "AAAAAAAAAA";
    private static final String UPDATED_QUESTIONNAIRE = "BBBBBBBBBB";

    private static final Long DEFAULT_SCORE = 1L;
    private static final Long UPDATED_SCORE = 2L;

    private static final String ENTITY_API_URL = "/api/scores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ScoresRepository scoresRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScoresMockMvc;

    private Scores scores;

    private Scores insertedScores;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scores createEntity(EntityManager em) {
        Scores scores = new Scores().time(DEFAULT_TIME).questionnaire(DEFAULT_QUESTIONNAIRE).score(DEFAULT_SCORE);
        return scores;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scores createUpdatedEntity(EntityManager em) {
        Scores scores = new Scores().time(UPDATED_TIME).questionnaire(UPDATED_QUESTIONNAIRE).score(UPDATED_SCORE);
        return scores;
    }

    @BeforeEach
    public void initTest() {
        scores = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedScores != null) {
            scoresRepository.delete(insertedScores);
            insertedScores = null;
        }
    }

    @Test
    @Transactional
    void createScores() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Scores
        var returnedScores = om.readValue(
            restScoresMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scores)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Scores.class
        );

        // Validate the Scores in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertScoresUpdatableFieldsEquals(returnedScores, getPersistedScores(returnedScores));

        insertedScores = returnedScores;
    }

    @Test
    @Transactional
    void createScoresWithExistingId() throws Exception {
        // Create the Scores with an existing ID
        scores.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScoresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scores)))
            .andExpect(status().isBadRequest());

        // Validate the Scores in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllScores() throws Exception {
        // Initialize the database
        insertedScores = scoresRepository.saveAndFlush(scores);

        // Get all the scoresList
        restScoresMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scores.getId().intValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].questionnaire").value(hasItem(DEFAULT_QUESTIONNAIRE)))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.intValue())));
    }

    @Test
    @Transactional
    void getScores() throws Exception {
        // Initialize the database
        insertedScores = scoresRepository.saveAndFlush(scores);

        // Get the scores
        restScoresMockMvc
            .perform(get(ENTITY_API_URL_ID, scores.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scores.getId().intValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.questionnaire").value(DEFAULT_QUESTIONNAIRE))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingScores() throws Exception {
        // Get the scores
        restScoresMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingScores() throws Exception {
        // Initialize the database
        insertedScores = scoresRepository.saveAndFlush(scores);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scores
        Scores updatedScores = scoresRepository.findById(scores.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedScores are not directly saved in db
        em.detach(updatedScores);
        updatedScores.time(UPDATED_TIME).questionnaire(UPDATED_QUESTIONNAIRE).score(UPDATED_SCORE);

        restScoresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScores.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedScores))
            )
            .andExpect(status().isOk());

        // Validate the Scores in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedScoresToMatchAllProperties(updatedScores);
    }

    @Test
    @Transactional
    void putNonExistingScores() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scores.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScoresMockMvc
            .perform(put(ENTITY_API_URL_ID, scores.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scores)))
            .andExpect(status().isBadRequest());

        // Validate the Scores in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScores() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scores.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scores))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scores in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScores() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scores.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoresMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scores)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Scores in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScoresWithPatch() throws Exception {
        // Initialize the database
        insertedScores = scoresRepository.saveAndFlush(scores);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scores using partial update
        Scores partialUpdatedScores = new Scores();
        partialUpdatedScores.setId(scores.getId());

        partialUpdatedScores.time(UPDATED_TIME).score(UPDATED_SCORE);

        restScoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScores.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScores))
            )
            .andExpect(status().isOk());

        // Validate the Scores in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScoresUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedScores, scores), getPersistedScores(scores));
    }

    @Test
    @Transactional
    void fullUpdateScoresWithPatch() throws Exception {
        // Initialize the database
        insertedScores = scoresRepository.saveAndFlush(scores);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scores using partial update
        Scores partialUpdatedScores = new Scores();
        partialUpdatedScores.setId(scores.getId());

        partialUpdatedScores.time(UPDATED_TIME).questionnaire(UPDATED_QUESTIONNAIRE).score(UPDATED_SCORE);

        restScoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScores.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScores))
            )
            .andExpect(status().isOk());

        // Validate the Scores in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScoresUpdatableFieldsEquals(partialUpdatedScores, getPersistedScores(partialUpdatedScores));
    }

    @Test
    @Transactional
    void patchNonExistingScores() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scores.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scores.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(scores))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scores in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScores() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scores.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scores))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scores in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScores() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scores.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoresMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(scores)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Scores in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScores() throws Exception {
        // Initialize the database
        insertedScores = scoresRepository.saveAndFlush(scores);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the scores
        restScoresMockMvc
            .perform(delete(ENTITY_API_URL_ID, scores.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return scoresRepository.count();
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

    protected Scores getPersistedScores(Scores scores) {
        return scoresRepository.findById(scores.getId()).orElseThrow();
    }

    protected void assertPersistedScoresToMatchAllProperties(Scores expectedScores) {
        assertScoresAllPropertiesEquals(expectedScores, getPersistedScores(expectedScores));
    }

    protected void assertPersistedScoresToMatchUpdatableProperties(Scores expectedScores) {
        assertScoresAllUpdatablePropertiesEquals(expectedScores, getPersistedScores(expectedScores));
    }
}
