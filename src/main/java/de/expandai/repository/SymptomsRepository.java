package de.expandai.repository;

import de.expandai.domain.Symptoms;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Symptoms entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SymptomsRepository extends JpaRepository<Symptoms, Long> {}
