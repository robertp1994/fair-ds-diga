package de.expandai.service.impl;

import de.expandai.domain.FingerTaps;
import de.expandai.repository.FingerTapsRepository;
import de.expandai.service.FingerTapsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link de.expandai.domain.FingerTaps}.
 */
@Service
@Transactional
public class FingerTapsServiceImpl implements FingerTapsService {

    private static final Logger log = LoggerFactory.getLogger(FingerTapsServiceImpl.class);

    private final FingerTapsRepository fingerTapsRepository;

    public FingerTapsServiceImpl(FingerTapsRepository fingerTapsRepository) {
        this.fingerTapsRepository = fingerTapsRepository;
    }

    @Override
    public FingerTaps save(FingerTaps fingerTaps) {
        log.debug("Request to save FingerTaps : {}", fingerTaps);
        return fingerTapsRepository.save(fingerTaps);
    }

    @Override
    public FingerTaps update(FingerTaps fingerTaps) {
        log.debug("Request to update FingerTaps : {}", fingerTaps);
        return fingerTapsRepository.save(fingerTaps);
    }

    @Override
    public Optional<FingerTaps> partialUpdate(FingerTaps fingerTaps) {
        log.debug("Request to partially update FingerTaps : {}", fingerTaps);

        return fingerTapsRepository
            .findById(fingerTaps.getId())
            .map(existingFingerTaps -> {
                if (fingerTaps.getPatientId() != null) {
                    existingFingerTaps.setPatientId(fingerTaps.getPatientId());
                }
                if (fingerTaps.getDate() != null) {
                    existingFingerTaps.setDate(fingerTaps.getDate());
                }
                if (fingerTaps.getSide() != null) {
                    existingFingerTaps.setSide(fingerTaps.getSide());
                }
                if (fingerTaps.getThumbX() != null) {
                    existingFingerTaps.setThumbX(fingerTaps.getThumbX());
                }
                if (fingerTaps.getThumbY() != null) {
                    existingFingerTaps.setThumbY(fingerTaps.getThumbY());
                }
                if (fingerTaps.getDigitX() != null) {
                    existingFingerTaps.setDigitX(fingerTaps.getDigitX());
                }
                if (fingerTaps.getDigitY() != null) {
                    existingFingerTaps.setDigitY(fingerTaps.getDigitY());
                }

                return existingFingerTaps;
            })
            .map(fingerTapsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FingerTaps> findAll() {
        log.debug("Request to get all FingerTaps");
        return fingerTapsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FingerTaps> findOne(Long id) {
        log.debug("Request to get FingerTaps : {}", id);
        return fingerTapsRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FingerTaps> findByPatient(String id) {
        log.debug("Request to get FingerTaps by patient : {}", id);
        return fingerTapsRepository.findAllByPatientId(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FingerTaps : {}", id);
        fingerTapsRepository.deleteById(id);
    }
}
