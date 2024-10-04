package de.expandai.repository;

import de.expandai.domain.FingerTaps;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FingerTaps entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FingerTapsRepository extends JpaRepository<FingerTaps, Long> {
    List<FingerTaps> findAllByPatientId(String patientId);
}
