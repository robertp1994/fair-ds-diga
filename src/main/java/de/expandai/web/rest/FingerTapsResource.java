package de.expandai.web.rest;

import de.expandai.domain.FingerTaps;
import de.expandai.repository.FingerTapsRepository;
import de.expandai.service.FingerTapsService;
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
 * REST controller for managing {@link de.expandai.domain.FingerTaps}.
 */
@RestController
@RequestMapping("/api/finger-taps")
public class FingerTapsResource {

    private static final Logger log = LoggerFactory.getLogger(FingerTapsResource.class);

    private static final String ENTITY_NAME = "fingerTaps";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FingerTapsService fingerTapsService;

    private final FingerTapsRepository fingerTapsRepository;

    public FingerTapsResource(FingerTapsService fingerTapsService, FingerTapsRepository fingerTapsRepository) {
        this.fingerTapsService = fingerTapsService;
        this.fingerTapsRepository = fingerTapsRepository;
    }

    /**
     * {@code POST  /finger-taps} : Create a new fingerTaps.
     *
     * @param fingerTaps the fingerTaps to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fingerTaps, or with status {@code 400 (Bad Request)} if the fingerTaps has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FingerTaps> createFingerTaps(@RequestBody FingerTaps fingerTaps) throws URISyntaxException {
        log.debug("REST request to save FingerTaps : {}", fingerTaps);
        if (fingerTaps.getId() != null) {
            throw new BadRequestAlertException("A new fingerTaps cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fingerTaps = fingerTapsService.save(fingerTaps);
        return ResponseEntity.created(new URI("/api/finger-taps/" + fingerTaps.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, fingerTaps.getId().toString()))
            .body(fingerTaps);
    }

    /**
     * {@code PUT  /finger-taps/:id} : Updates an existing fingerTaps.
     *
     * @param id the id of the fingerTaps to save.
     * @param fingerTaps the fingerTaps to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fingerTaps,
     * or with status {@code 400 (Bad Request)} if the fingerTaps is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fingerTaps couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FingerTaps> updateFingerTaps(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FingerTaps fingerTaps
    ) throws URISyntaxException {
        log.debug("REST request to update FingerTaps : {}, {}", id, fingerTaps);
        if (fingerTaps.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fingerTaps.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fingerTapsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fingerTaps = fingerTapsService.update(fingerTaps);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fingerTaps.getId().toString()))
            .body(fingerTaps);
    }

    /**
     * {@code PATCH  /finger-taps/:id} : Partial updates given fields of an existing fingerTaps, field will ignore if it is null
     *
     * @param id the id of the fingerTaps to save.
     * @param fingerTaps the fingerTaps to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fingerTaps,
     * or with status {@code 400 (Bad Request)} if the fingerTaps is not valid,
     * or with status {@code 404 (Not Found)} if the fingerTaps is not found,
     * or with status {@code 500 (Internal Server Error)} if the fingerTaps couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FingerTaps> partialUpdateFingerTaps(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FingerTaps fingerTaps
    ) throws URISyntaxException {
        log.debug("REST request to partial update FingerTaps partially : {}, {}", id, fingerTaps);
        if (fingerTaps.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fingerTaps.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fingerTapsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FingerTaps> result = fingerTapsService.partialUpdate(fingerTaps);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fingerTaps.getId().toString())
        );
    }

    /**
     * {@code GET  /finger-taps} : get all the fingerTaps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fingerTaps in body.
     */
    @GetMapping("")
    public List<FingerTaps> getAllFingerTaps() {
        log.debug("REST request to get all FingerTaps");
        return fingerTapsService.findAll();
    }

    // Custom API to get all FingerTaps by patientId
    @GetMapping("/patients/{patientId}")
    public List<FingerTaps> getFingerTapsByPatient(@PathVariable String patientId) {
        return fingerTapsService.findByPatientId(patientId);
    }

    /**
     * {@code GET  /finger-taps/:id} : get the "id" fingerTaps.
     *
     * @param id the id of the fingerTaps to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fingerTaps, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FingerTaps> getFingerTaps(@PathVariable("id") Long id) {
        log.debug("REST request to get FingerTaps : {}", id);
        Optional<FingerTaps> fingerTaps = fingerTapsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fingerTaps);
    }

    /**
     * {@code DELETE  /finger-taps/:id} : delete the "id" fingerTaps.
     *
     * @param id the id of the fingerTaps to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFingerTaps(@PathVariable("id") Long id) {
        log.debug("REST request to delete FingerTaps : {}", id);
        fingerTapsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
