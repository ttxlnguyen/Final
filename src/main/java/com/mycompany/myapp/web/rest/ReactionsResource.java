package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Reactions;
import com.mycompany.myapp.repository.ReactionsRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Reactions}.
 */
@RestController
@RequestMapping("/api/reactions")
@Transactional
public class ReactionsResource {

    private static final Logger log = LoggerFactory.getLogger(ReactionsResource.class);

    private static final String ENTITY_NAME = "reactions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReactionsRepository reactionsRepository;

    public ReactionsResource(ReactionsRepository reactionsRepository) {
        this.reactionsRepository = reactionsRepository;
    }

    /**
     * {@code POST  /reactions} : Create a new reactions.
     *
     * @param reactions the reactions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reactions, or with status {@code 400 (Bad Request)} if the reactions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Reactions> createReactions(@RequestBody Reactions reactions) throws URISyntaxException {
        log.debug("REST request to save Reactions : {}", reactions);
        if (reactions.getId() != null) {
            throw new BadRequestAlertException("A new reactions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reactions = reactionsRepository.save(reactions);
        return ResponseEntity.created(new URI("/api/reactions/" + reactions.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reactions.getId().toString()))
            .body(reactions);
    }

    /**
     * {@code PUT  /reactions/:id} : Updates an existing reactions.
     *
     * @param id the id of the reactions to save.
     * @param reactions the reactions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reactions,
     * or with status {@code 400 (Bad Request)} if the reactions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reactions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Reactions> updateReactions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Reactions reactions
    ) throws URISyntaxException {
        log.debug("REST request to update Reactions : {}, {}", id, reactions);
        if (reactions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reactions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reactionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reactions = reactionsRepository.save(reactions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reactions.getId().toString()))
            .body(reactions);
    }

    /**
     * {@code PATCH  /reactions/:id} : Partial updates given fields of an existing reactions, field will ignore if it is null
     *
     * @param id the id of the reactions to save.
     * @param reactions the reactions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reactions,
     * or with status {@code 400 (Bad Request)} if the reactions is not valid,
     * or with status {@code 404 (Not Found)} if the reactions is not found,
     * or with status {@code 500 (Internal Server Error)} if the reactions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Reactions> partialUpdateReactions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Reactions reactions
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reactions partially : {}, {}", id, reactions);
        if (reactions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reactions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reactionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Reactions> result = reactionsRepository
            .findById(reactions.getId())
            .map(existingReactions -> {
                if (reactions.getReaction() != null) {
                    existingReactions.setReaction(reactions.getReaction());
                }

                return existingReactions;
            })
            .map(reactionsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reactions.getId().toString())
        );
    }

    /**
     * {@code GET  /reactions} : get all the reactions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reactions in body.
     */
    @GetMapping("")
    public List<Reactions> getAllReactions() {
        log.debug("REST request to get all Reactions");
        return reactionsRepository.findAll();
    }

    /**
     * {@code GET  /reactions/:id} : get the "id" reactions.
     *
     * @param id the id of the reactions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reactions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reactions> getReactions(@PathVariable("id") Long id) {
        log.debug("REST request to get Reactions : {}", id);
        Optional<Reactions> reactions = reactionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(reactions);
    }

    /**
     * {@code DELETE  /reactions/:id} : delete the "id" reactions.
     *
     * @param id the id of the reactions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReactions(@PathVariable("id") Long id) {
        log.debug("REST request to delete Reactions : {}", id);
        reactionsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
