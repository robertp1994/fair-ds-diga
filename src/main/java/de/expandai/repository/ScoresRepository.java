package de.expandai.repository;

import de.expandai.domain.Scores;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Scores entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScoresRepository extends JpaRepository<Scores, Long> {}
