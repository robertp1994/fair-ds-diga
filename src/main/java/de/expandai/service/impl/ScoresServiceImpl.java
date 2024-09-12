package de.expandai.service.impl;

import de.expandai.domain.Scores;
import de.expandai.repository.ScoresRepository;
import de.expandai.service.ScoresService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link de.expandai.domain.Scores}.
 */
@Service
@Transactional
public class ScoresServiceImpl implements ScoresService {

    private static final Logger log = LoggerFactory.getLogger(ScoresServiceImpl.class);

    private final ScoresRepository scoresRepository;

    public ScoresServiceImpl(ScoresRepository scoresRepository) {
        this.scoresRepository = scoresRepository;
    }

    @Override
    public Scores save(Scores scores) {
        log.debug("Request to save Scores : {}", scores);
        return scoresRepository.save(scores);
    }

    @Override
    public Scores update(Scores scores) {
        log.debug("Request to update Scores : {}", scores);
        return scoresRepository.save(scores);
    }

    @Override
    public Optional<Scores> partialUpdate(Scores scores) {
        log.debug("Request to partially update Scores : {}", scores);

        return scoresRepository
            .findById(scores.getId())
            .map(existingScores -> {
                if (scores.getTime() != null) {
                    existingScores.setTime(scores.getTime());
                }
                if (scores.getQuestionnaire() != null) {
                    existingScores.setQuestionnaire(scores.getQuestionnaire());
                }
                if (scores.getScore() != null) {
                    existingScores.setScore(scores.getScore());
                }

                return existingScores;
            })
            .map(scoresRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scores> findAll() {
        log.debug("Request to get all Scores");
        return scoresRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Scores> findOne(Long id) {
        log.debug("Request to get Scores : {}", id);
        return scoresRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Scores : {}", id);
        scoresRepository.deleteById(id);
    }
}
