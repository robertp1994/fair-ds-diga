package de.expandai.web.rest;

import static de.expandai.domain.FingerTapsAsserts.*;
import static de.expandai.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.expandai.IntegrationTest;
import de.expandai.domain.FingerTaps;
import de.expandai.domain.enumeration.SIDE;
import de.expandai.repository.FingerTapsRepository;
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
 * Integration tests for the {@link FingerTapsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FingerTapsResourceIT {

    private static final String DEFAULT_PATIENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PATIENT_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final SIDE DEFAULT_SIDE = SIDE.LEFT;
    private static final SIDE UPDATED_SIDE = SIDE.RIGHT;

    private static final String DEFAULT_THUMB_X = "AAAAAAAAAA";
    private static final String UPDATED_THUMB_X = "BBBBBBBBBB";

    private static final String DEFAULT_THUMB_Y = "AAAAAAAAAA";
    private static final String UPDATED_THUMB_Y = "BBBBBBBBBB";

    private static final String DEFAULT_DIGIT_X = "AAAAAAAAAA";
    private static final String UPDATED_DIGIT_X = "BBBBBBBBBB";

    private static final String DEFAULT_DIGIT_Y = "AAAAAAAAAA";
    private static final String UPDATED_DIGIT_Y = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/finger-taps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FingerTapsRepository fingerTapsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFingerTapsMockMvc;

    private FingerTaps fingerTaps;

    private FingerTaps insertedFingerTaps;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FingerTaps createEntity(EntityManager em) {
        FingerTaps fingerTaps = new FingerTaps()
            .patientId(DEFAULT_PATIENT_ID)
            .date(DEFAULT_DATE)
            .side(DEFAULT_SIDE)
            .thumbX(DEFAULT_THUMB_X)
            .thumbY(DEFAULT_THUMB_Y)
            .digitX(DEFAULT_DIGIT_X)
            .digitY(DEFAULT_DIGIT_Y);
        return fingerTaps;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FingerTaps createUpdatedEntity(EntityManager em) {
        FingerTaps fingerTaps = new FingerTaps()
            .patientId(UPDATED_PATIENT_ID)
            .date(UPDATED_DATE)
            .side(UPDATED_SIDE)
            .thumbX(UPDATED_THUMB_X)
            .thumbY(UPDATED_THUMB_Y)
            .digitX(UPDATED_DIGIT_X)
            .digitY(UPDATED_DIGIT_Y);
        return fingerTaps;
    }

    @BeforeEach
    public void initTest() {
        fingerTaps = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFingerTaps != null) {
            fingerTapsRepository.delete(insertedFingerTaps);
            insertedFingerTaps = null;
        }
    }

    @Test
    @Transactional
    void createFingerTaps() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FingerTaps
        var returnedFingerTaps = om.readValue(
            restFingerTapsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fingerTaps)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FingerTaps.class
        );

        // Validate the FingerTaps in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFingerTapsUpdatableFieldsEquals(returnedFingerTaps, getPersistedFingerTaps(returnedFingerTaps));

        insertedFingerTaps = returnedFingerTaps;
    }

    @Test
    @Transactional
    void createFingerTapsWithExistingId() throws Exception {
        // Create the FingerTaps with an existing ID
        fingerTaps.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFingerTapsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fingerTaps)))
            .andExpect(status().isBadRequest());

        // Validate the FingerTaps in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFingerTaps() throws Exception {
        // Initialize the database
        insertedFingerTaps = fingerTapsRepository.saveAndFlush(fingerTaps);

        // Get all the fingerTapsList
        restFingerTapsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fingerTaps.getId().intValue())))
            .andExpect(jsonPath("$.[*].patientId").value(hasItem(DEFAULT_PATIENT_ID)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].side").value(hasItem(DEFAULT_SIDE.toString())))
            .andExpect(jsonPath("$.[*].thumbX").value(hasItem(DEFAULT_THUMB_X)))
            .andExpect(jsonPath("$.[*].thumbY").value(hasItem(DEFAULT_THUMB_Y)))
            .andExpect(jsonPath("$.[*].digitX").value(hasItem(DEFAULT_DIGIT_X)))
            .andExpect(jsonPath("$.[*].digitY").value(hasItem(DEFAULT_DIGIT_Y)));
    }

    @Test
    @Transactional
    void getFingerTaps() throws Exception {
        // Initialize the database
        insertedFingerTaps = fingerTapsRepository.saveAndFlush(fingerTaps);

        // Get the fingerTaps
        restFingerTapsMockMvc
            .perform(get(ENTITY_API_URL_ID, fingerTaps.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fingerTaps.getId().intValue()))
            .andExpect(jsonPath("$.patientId").value(DEFAULT_PATIENT_ID))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.side").value(DEFAULT_SIDE.toString()))
            .andExpect(jsonPath("$.thumbX").value(DEFAULT_THUMB_X))
            .andExpect(jsonPath("$.thumbY").value(DEFAULT_THUMB_Y))
            .andExpect(jsonPath("$.digitX").value(DEFAULT_DIGIT_X))
            .andExpect(jsonPath("$.digitY").value(DEFAULT_DIGIT_Y));
    }

    @Test
    @Transactional
    void getNonExistingFingerTaps() throws Exception {
        // Get the fingerTaps
        restFingerTapsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFingerTaps() throws Exception {
        // Initialize the database
        insertedFingerTaps = fingerTapsRepository.saveAndFlush(fingerTaps);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fingerTaps
        FingerTaps updatedFingerTaps = fingerTapsRepository.findById(fingerTaps.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFingerTaps are not directly saved in db
        em.detach(updatedFingerTaps);
        updatedFingerTaps
            .patientId(UPDATED_PATIENT_ID)
            .date(UPDATED_DATE)
            .side(UPDATED_SIDE)
            .thumbX(UPDATED_THUMB_X)
            .thumbY(UPDATED_THUMB_Y)
            .digitX(UPDATED_DIGIT_X)
            .digitY(UPDATED_DIGIT_Y);

        restFingerTapsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFingerTaps.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFingerTaps))
            )
            .andExpect(status().isOk());

        // Validate the FingerTaps in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFingerTapsToMatchAllProperties(updatedFingerTaps);
    }

    @Test
    @Transactional
    void putNonExistingFingerTaps() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fingerTaps.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFingerTapsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fingerTaps.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fingerTaps))
            )
            .andExpect(status().isBadRequest());

        // Validate the FingerTaps in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFingerTaps() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fingerTaps.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFingerTapsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fingerTaps))
            )
            .andExpect(status().isBadRequest());

        // Validate the FingerTaps in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFingerTaps() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fingerTaps.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFingerTapsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fingerTaps)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FingerTaps in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFingerTapsWithPatch() throws Exception {
        // Initialize the database
        insertedFingerTaps = fingerTapsRepository.saveAndFlush(fingerTaps);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fingerTaps using partial update
        FingerTaps partialUpdatedFingerTaps = new FingerTaps();
        partialUpdatedFingerTaps.setId(fingerTaps.getId());

        partialUpdatedFingerTaps.thumbY(UPDATED_THUMB_Y).digitX(UPDATED_DIGIT_X).digitY(UPDATED_DIGIT_Y);

        restFingerTapsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFingerTaps.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFingerTaps))
            )
            .andExpect(status().isOk());

        // Validate the FingerTaps in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFingerTapsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFingerTaps, fingerTaps),
            getPersistedFingerTaps(fingerTaps)
        );
    }

    @Test
    @Transactional
    void fullUpdateFingerTapsWithPatch() throws Exception {
        // Initialize the database
        insertedFingerTaps = fingerTapsRepository.saveAndFlush(fingerTaps);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fingerTaps using partial update
        FingerTaps partialUpdatedFingerTaps = new FingerTaps();
        partialUpdatedFingerTaps.setId(fingerTaps.getId());

        partialUpdatedFingerTaps
            .patientId(UPDATED_PATIENT_ID)
            .date(UPDATED_DATE)
            .side(UPDATED_SIDE)
            .thumbX(UPDATED_THUMB_X)
            .thumbY(UPDATED_THUMB_Y)
            .digitX(UPDATED_DIGIT_X)
            .digitY(UPDATED_DIGIT_Y);

        restFingerTapsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFingerTaps.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFingerTaps))
            )
            .andExpect(status().isOk());

        // Validate the FingerTaps in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFingerTapsUpdatableFieldsEquals(partialUpdatedFingerTaps, getPersistedFingerTaps(partialUpdatedFingerTaps));
    }

    @Test
    @Transactional
    void patchNonExistingFingerTaps() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fingerTaps.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFingerTapsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fingerTaps.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fingerTaps))
            )
            .andExpect(status().isBadRequest());

        // Validate the FingerTaps in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFingerTaps() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fingerTaps.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFingerTapsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fingerTaps))
            )
            .andExpect(status().isBadRequest());

        // Validate the FingerTaps in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFingerTaps() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fingerTaps.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFingerTapsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fingerTaps)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FingerTaps in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFingerTaps() throws Exception {
        // Initialize the database
        insertedFingerTaps = fingerTapsRepository.saveAndFlush(fingerTaps);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fingerTaps
        restFingerTapsMockMvc
            .perform(delete(ENTITY_API_URL_ID, fingerTaps.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fingerTapsRepository.count();
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

    protected FingerTaps getPersistedFingerTaps(FingerTaps fingerTaps) {
        return fingerTapsRepository.findById(fingerTaps.getId()).orElseThrow();
    }

    protected void assertPersistedFingerTapsToMatchAllProperties(FingerTaps expectedFingerTaps) {
        assertFingerTapsAllPropertiesEquals(expectedFingerTaps, getPersistedFingerTaps(expectedFingerTaps));
    }

    protected void assertPersistedFingerTapsToMatchUpdatableProperties(FingerTaps expectedFingerTaps) {
        assertFingerTapsAllUpdatablePropertiesEquals(expectedFingerTaps, getPersistedFingerTaps(expectedFingerTaps));
    }
}
