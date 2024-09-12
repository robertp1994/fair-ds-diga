package de.expandai.web.rest;

import de.expandai.domain.Scores;
import de.expandai.repository.ScoresRepository;
import de.expandai.service.ScoresService;
import de.expandai.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link de.expandai.domain.Scores}.
 */
@RestController
@RequestMapping("/api/scores")
public class ScoresResource {

    private static final Logger log = LoggerFactory.getLogger(ScoresResource.class);

    private static final String ENTITY_NAME = "scores";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScoresService scoresService;

    private final ScoresRepository scoresRepository;

    public ScoresResource(ScoresService scoresService, ScoresRepository scoresRepository) {
        this.scoresService = scoresService;
        this.scoresRepository = scoresRepository;
    }

    /**
     * {@code POST  /scores} : Create a new scores.
     *
     * @param scores the scores to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scores, or with status {@code 400 (Bad Request)} if the scores has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Scores> createScores(@RequestBody Scores scores) throws URISyntaxException {
        log.debug("REST request to save Scores : {}", scores);
        if (scores.getId() != null) {
            throw new BadRequestAlertException("A new scores cannot already have an ID", ENTITY_NAME, "idexists");
        }
        scores = scoresService.save(scores);
        return ResponseEntity.created(new URI("/api/scores/" + scores.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, scores.getId().toString()))
            .body(scores);
    }

    /**
     * {@code PUT  /scores/:id} : Updates an existing scores.
     *
     * @param id the id of the scores to save.
     * @param scores the scores to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scores,
     * or with status {@code 400 (Bad Request)} if the scores is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scores couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Scores> updateScores(@PathVariable(value = "id", required = false) final Long id, @RequestBody Scores scores)
        throws URISyntaxException {
        log.debug("REST request to update Scores : {}, {}", id, scores);
        if (scores.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scores.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scoresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        scores = scoresService.update(scores);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scores.getId().toString()))
            .body(scores);
    }

    /**
     * {@code PATCH  /scores/:id} : Partial updates given fields of an existing scores, field will ignore if it is null
     *
     * @param id the id of the scores to save.
     * @param scores the scores to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scores,
     * or with status {@code 400 (Bad Request)} if the scores is not valid,
     * or with status {@code 404 (Not Found)} if the scores is not found,
     * or with status {@code 500 (Internal Server Error)} if the scores couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Scores> partialUpdateScores(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Scores scores
    ) throws URISyntaxException {
        log.debug("REST request to partial update Scores partially : {}, {}", id, scores);
        if (scores.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scores.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scoresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Scores> result = scoresService.partialUpdate(scores);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scores.getId().toString())
        );
    }

    /**
     * {@code GET  /scores} : get all the scores.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scores in body.
     */
    @GetMapping("")
    public List<Scores> getAllScores() {
        log.debug("REST request to get all Scores");
        return scoresService.findAll();
    }

    /**
     * {@code GET  /scores/:id} : get the "id" scores.
     *
     * @param id the id of the scores to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scores, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Scores> getScores(@PathVariable("id") Long id) {
        log.debug("REST request to get Scores : {}", id);
        Optional<Scores> scores = scoresService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scores);
    }

    /**
     * {@code DELETE  /scores/:id} : delete the "id" scores.
     *
     * @param id the id of the scores to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScores(@PathVariable("id") Long id) {
        log.debug("REST request to delete Scores : {}", id);
        scoresService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
