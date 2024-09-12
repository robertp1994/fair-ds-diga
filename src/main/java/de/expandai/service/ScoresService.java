package de.expandai.service;

import de.expandai.domain.Scores;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.expandai.domain.Scores}.
 */
public interface ScoresService {
    /**
     * Save a scores.
     *
     * @param scores the entity to save.
     * @return the persisted entity.
     */
    Scores save(Scores scores);

    /**
     * Updates a scores.
     *
     * @param scores the entity to update.
     * @return the persisted entity.
     */
    Scores update(Scores scores);

    /**
     * Partially updates a scores.
     *
     * @param scores the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Scores> partialUpdate(Scores scores);

    /**
     * Get all the scores.
     *
     * @return the list of entities.
     */
    List<Scores> findAll();

    /**
     * Get the "id" scores.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Scores> findOne(Long id);

    /**
     * Delete the "id" scores.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
