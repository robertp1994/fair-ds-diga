package de.expandai.web.rest;

import de.expandai.domain.Symptoms;
import de.expandai.repository.SymptomsRepository;
import de.expandai.service.SymptomsService;
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
 * REST controller for managing {@link de.expandai.domain.Symptoms}.
 */
@RestController
@RequestMapping("/api/symptoms")
public class SymptomsResource {

    private static final Logger log = LoggerFactory.getLogger(SymptomsResource.class);

    private static final String ENTITY_NAME = "symptoms";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SymptomsService symptomsService;

    private final SymptomsRepository symptomsRepository;

    public SymptomsResource(SymptomsService symptomsService, SymptomsRepository symptomsRepository) {
        this.symptomsService = symptomsService;
        this.symptomsRepository = symptomsRepository;
    }

    /**
     * {@code POST  /symptoms} : Create a new symptoms.
     *
     * @param symptoms the symptoms to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new symptoms, or with status {@code 400 (Bad Request)} if the symptoms has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Symptoms> createSymptoms(@RequestBody Symptoms symptoms) throws URISyntaxException {
        log.debug("REST request to save Symptoms : {}", symptoms);
        if (symptoms.getId() != null) {
            throw new BadRequestAlertException("A new symptoms cannot already have an ID", ENTITY_NAME, "idexists");
        }
        symptoms = symptomsService.save(symptoms);
        return ResponseEntity.created(new URI("/api/symptoms/" + symptoms.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, symptoms.getId().toString()))
            .body(symptoms);
    }

    /**
     * {@code PUT  /symptoms/:id} : Updates an existing symptoms.
     *
     * @param id the id of the symptoms to save.
     * @param symptoms the symptoms to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated symptoms,
     * or with status {@code 400 (Bad Request)} if the symptoms is not valid,
     * or with status {@code 500 (Internal Server Error)} if the symptoms couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Symptoms> updateSymptoms(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Symptoms symptoms
    ) throws URISyntaxException {
        log.debug("REST request to update Symptoms : {}, {}", id, symptoms);
        if (symptoms.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, symptoms.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!symptomsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        symptoms = symptomsService.update(symptoms);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, symptoms.getId().toString()))
            .body(symptoms);
    }

    /**
     * {@code PATCH  /symptoms/:id} : Partial updates given fields of an existing symptoms, field will ignore if it is null
     *
     * @param id the id of the symptoms to save.
     * @param symptoms the symptoms to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated symptoms,
     * or with status {@code 400 (Bad Request)} if the symptoms is not valid,
     * or with status {@code 404 (Not Found)} if the symptoms is not found,
     * or with status {@code 500 (Internal Server Error)} if the symptoms couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Symptoms> partialUpdateSymptoms(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Symptoms symptoms
    ) throws URISyntaxException {
        log.debug("REST request to partial update Symptoms partially : {}, {}", id, symptoms);
        if (symptoms.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, symptoms.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!symptomsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Symptoms> result = symptomsService.partialUpdate(symptoms);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, symptoms.getId().toString())
        );
    }

    /**
     * {@code GET  /symptoms} : get all the symptoms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of symptoms in body.
     */
    @GetMapping("")
    public List<Symptoms> getAllSymptoms() {
        log.debug("REST request to get all Symptoms");
        return symptomsService.findAll();
    }

    /**
     * {@code GET  /symptoms/:id} : get the "id" symptoms.
     *
     * @param id the id of the symptoms to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the symptoms, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Symptoms> getSymptoms(@PathVariable("id") Long id) {
        log.debug("REST request to get Symptoms : {}", id);
        Optional<Symptoms> symptoms = symptomsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(symptoms);
    }

    /**
     * {@code DELETE  /symptoms/:id} : delete the "id" symptoms.
     *
     * @param id the id of the symptoms to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSymptoms(@PathVariable("id") Long id) {
        log.debug("REST request to delete Symptoms : {}", id);
        symptomsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
