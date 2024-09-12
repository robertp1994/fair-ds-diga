package de.expandai.service.impl;

import de.expandai.domain.Symptoms;
import de.expandai.repository.SymptomsRepository;
import de.expandai.service.SymptomsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link de.expandai.domain.Symptoms}.
 */
@Service
@Transactional
public class SymptomsServiceImpl implements SymptomsService {

    private static final Logger log = LoggerFactory.getLogger(SymptomsServiceImpl.class);

    private final SymptomsRepository symptomsRepository;

    public SymptomsServiceImpl(SymptomsRepository symptomsRepository) {
        this.symptomsRepository = symptomsRepository;
    }

    @Override
    public Symptoms save(Symptoms symptoms) {
        log.debug("Request to save Symptoms : {}", symptoms);
        return symptomsRepository.save(symptoms);
    }

    @Override
    public Symptoms update(Symptoms symptoms) {
        log.debug("Request to update Symptoms : {}", symptoms);
        return symptomsRepository.save(symptoms);
    }

    @Override
    public Optional<Symptoms> partialUpdate(Symptoms symptoms) {
        log.debug("Request to partially update Symptoms : {}", symptoms);

        return symptomsRepository
            .findById(symptoms.getId())
            .map(existingSymptoms -> {
                if (symptoms.getTime() != null) {
                    existingSymptoms.setTime(symptoms.getTime());
                }
                if (symptoms.getStatus() != null) {
                    existingSymptoms.setStatus(symptoms.getStatus());
                }

                return existingSymptoms;
            })
            .map(symptomsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Symptoms> findAll() {
        log.debug("Request to get all Symptoms");
        return symptomsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Symptoms> findOne(Long id) {
        log.debug("Request to get Symptoms : {}", id);
        return symptomsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Symptoms : {}", id);
        symptomsRepository.deleteById(id);
    }
}
