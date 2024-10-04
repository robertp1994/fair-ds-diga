package de.expandai.service;

import de.expandai.domain.FingerTaps;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link de.expandai.domain.FingerTaps}.
 */
public interface FingerTapsService {
    /**
     * Save a fingerTaps.
     *
     * @param fingerTaps the entity to save.
     * @return the persisted entity.
     */
    FingerTaps save(FingerTaps fingerTaps);

    /**
     * Updates a fingerTaps.
     *
     * @param fingerTaps the entity to update.
     * @return the persisted entity.
     */
    FingerTaps update(FingerTaps fingerTaps);

    /**
     * Partially updates a fingerTaps.
     *
     * @param fingerTaps the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FingerTaps> partialUpdate(FingerTaps fingerTaps);

    /**
     * Get all the fingerTaps.
     *
     * @return the list of entities.
     */
    List<FingerTaps> findAll();

    /**
     * Get the "id" fingerTaps.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FingerTaps> findOne(Long id);

    @Transactional(readOnly = true)
    List<FingerTaps> findByPatientId(String patientId);

    /**
     * Delete the "id" fingerTaps.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
