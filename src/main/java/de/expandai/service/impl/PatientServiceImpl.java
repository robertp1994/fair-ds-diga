package de.expandai.service.impl;

import de.expandai.domain.Patient;
import de.expandai.repository.PatientRepository;
import de.expandai.service.PatientService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link de.expandai.domain.Patient}.
 */
@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private static final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient save(Patient patient) {
        log.debug("Request to save Patient : {}", patient);
        return patientRepository.save(patient);
    }

    @Override
    public Patient update(Patient patient) {
        log.debug("Request to update Patient : {}", patient);
        return patientRepository.save(patient);
    }

    @Override
    public Optional<Patient> partialUpdate(Patient patient) {
        log.debug("Request to partially update Patient : {}", patient);

        return patientRepository
            .findById(patient.getId())
            .map(existingPatient -> {
                if (patient.getGender() != null) {
                    existingPatient.setGender(patient.getGender());
                }
                if (patient.getBirthDate() != null) {
                    existingPatient.setBirthDate(patient.getBirthDate());
                }
                if (patient.getCreatedAt() != null) {
                    existingPatient.setCreatedAt(patient.getCreatedAt());
                }
                if (patient.getYearOfDiagnosis() != null) {
                    existingPatient.setYearOfDiagnosis(patient.getYearOfDiagnosis());
                }

                return existingPatient;
            })
            .map(patientRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        log.debug("Request to get all Patients");
        return patientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Patient> findOne(String id) {
        log.debug("Request to get Patient : {}", id);
        return patientRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Patient : {}", id);
        patientRepository.deleteById(id);
    }
}
