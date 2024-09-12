package de.expandai.service;

import de.expandai.domain.Symptoms;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.expandai.domain.Symptoms}.
 */
public interface SymptomsService {
    /**
     * Save a symptoms.
     *
     * @param symptoms the entity to save.
     * @return the persisted entity.
     */
    Symptoms save(Symptoms symptoms);

    /**
     * Updates a symptoms.
     *
     * @param symptoms the entity to update.
     * @return the persisted entity.
     */
    Symptoms update(Symptoms symptoms);

    /**
     * Partially updates a symptoms.
     *
     * @param symptoms the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Symptoms> partialUpdate(Symptoms symptoms);

    /**
     * Get all the symptoms.
     *
     * @return the list of entities.
     */
    List<Symptoms> findAll();

    /**
     * Get the "id" symptoms.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Symptoms> findOne(Long id);

    /**
     * Delete the "id" symptoms.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
