package de.expandai.web.rest;

import static de.expandai.domain.SymptomsAsserts.*;
import static de.expandai.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.expandai.IntegrationTest;
import de.expandai.domain.Symptoms;
import de.expandai.domain.enumeration.STATUS;
import de.expandai.repository.SymptomsRepository;
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
 * Integration tests for the {@link SymptomsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SymptomsResourceIT {

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final STATUS DEFAULT_STATUS = STATUS.RED;
    private static final STATUS UPDATED_STATUS = STATUS.YELLOW;

    private static final String ENTITY_API_URL = "/api/symptoms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SymptomsRepository symptomsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSymptomsMockMvc;

    private Symptoms symptoms;

    private Symptoms insertedSymptoms;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Symptoms createEntity(EntityManager em) {
        Symptoms symptoms = new Symptoms().time(DEFAULT_TIME).status(DEFAULT_STATUS);
        return symptoms;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Symptoms createUpdatedEntity(EntityManager em) {
        Symptoms symptoms = new Symptoms().time(UPDATED_TIME).status(UPDATED_STATUS);
        return symptoms;
    }

    @BeforeEach
    public void initTest() {
        symptoms = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSymptoms != null) {
            symptomsRepository.delete(insertedSymptoms);
            insertedSymptoms = null;
        }
    }

    @Test
    @Transactional
    void createSymptoms() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Symptoms
        var returnedSymptoms = om.readValue(
            restSymptomsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(symptoms)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Symptoms.class
        );

        // Validate the Symptoms in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSymptomsUpdatableFieldsEquals(returnedSymptoms, getPersistedSymptoms(returnedSymptoms));

        insertedSymptoms = returnedSymptoms;
    }

    @Test
    @Transactional
    void createSymptomsWithExistingId() throws Exception {
        // Create the Symptoms with an existing ID
        symptoms.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSymptomsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(symptoms)))
            .andExpect(status().isBadRequest());

        // Validate the Symptoms in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSymptoms() throws Exception {
        // Initialize the database
        insertedSymptoms = symptomsRepository.saveAndFlush(symptoms);

        // Get all the symptomsList
        restSymptomsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(symptoms.getId().intValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getSymptoms() throws Exception {
        // Initialize the database
        insertedSymptoms = symptomsRepository.saveAndFlush(symptoms);

        // Get the symptoms
        restSymptomsMockMvc
            .perform(get(ENTITY_API_URL_ID, symptoms.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(symptoms.getId().intValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSymptoms() throws Exception {
        // Get the symptoms
        restSymptomsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSymptoms() throws Exception {
        // Initialize the database
        insertedSymptoms = symptomsRepository.saveAndFlush(symptoms);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the symptoms
        Symptoms updatedSymptoms = symptomsRepository.findById(symptoms.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSymptoms are not directly saved in db
        em.detach(updatedSymptoms);
        updatedSymptoms.time(UPDATED_TIME).status(UPDATED_STATUS);

        restSymptomsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSymptoms.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSymptoms))
            )
            .andExpect(status().isOk());

        // Validate the Symptoms in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSymptomsToMatchAllProperties(updatedSymptoms);
    }

    @Test
    @Transactional
    void putNonExistingSymptoms() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        symptoms.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSymptomsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, symptoms.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(symptoms))
            )
            .andExpect(status().isBadRequest());

        // Validate the Symptoms in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSymptoms() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        symptoms.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSymptomsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(symptoms))
            )
            .andExpect(status().isBadRequest());

        // Validate the Symptoms in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSymptoms() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        symptoms.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSymptomsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(symptoms)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Symptoms in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSymptomsWithPatch() throws Exception {
        // Initialize the database
        insertedSymptoms = symptomsRepository.saveAndFlush(symptoms);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the symptoms using partial update
        Symptoms partialUpdatedSymptoms = new Symptoms();
        partialUpdatedSymptoms.setId(symptoms.getId());

        partialUpdatedSymptoms.time(UPDATED_TIME).status(UPDATED_STATUS);

        restSymptomsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSymptoms.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSymptoms))
            )
            .andExpect(status().isOk());

        // Validate the Symptoms in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSymptomsUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSymptoms, symptoms), getPersistedSymptoms(symptoms));
    }

    @Test
    @Transactional
    void fullUpdateSymptomsWithPatch() throws Exception {
        // Initialize the database
        insertedSymptoms = symptomsRepository.saveAndFlush(symptoms);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the symptoms using partial update
        Symptoms partialUpdatedSymptoms = new Symptoms();
        partialUpdatedSymptoms.setId(symptoms.getId());

        partialUpdatedSymptoms.time(UPDATED_TIME).status(UPDATED_STATUS);

        restSymptomsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSymptoms.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSymptoms))
            )
            .andExpect(status().isOk());

        // Validate the Symptoms in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSymptomsUpdatableFieldsEquals(partialUpdatedSymptoms, getPersistedSymptoms(partialUpdatedSymptoms));
    }

    @Test
    @Transactional
    void patchNonExistingSymptoms() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        symptoms.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSymptomsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, symptoms.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(symptoms))
            )
            .andExpect(status().isBadRequest());

        // Validate the Symptoms in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSymptoms() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        symptoms.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSymptomsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(symptoms))
            )
            .andExpect(status().isBadRequest());

        // Validate the Symptoms in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSymptoms() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        symptoms.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSymptomsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(symptoms)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Symptoms in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSymptoms() throws Exception {
        // Initialize the database
        insertedSymptoms = symptomsRepository.saveAndFlush(symptoms);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the symptoms
        restSymptomsMockMvc
            .perform(delete(ENTITY_API_URL_ID, symptoms.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return symptomsRepository.count();
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

    protected Symptoms getPersistedSymptoms(Symptoms symptoms) {
        return symptomsRepository.findById(symptoms.getId()).orElseThrow();
    }

    protected void assertPersistedSymptomsToMatchAllProperties(Symptoms expectedSymptoms) {
        assertSymptomsAllPropertiesEquals(expectedSymptoms, getPersistedSymptoms(expectedSymptoms));
    }

    protected void assertPersistedSymptomsToMatchUpdatableProperties(Symptoms expectedSymptoms) {
        assertSymptomsAllUpdatablePropertiesEquals(expectedSymptoms, getPersistedSymptoms(expectedSymptoms));
    }
}
