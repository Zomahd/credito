package com.sistema.credito.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sistema.credito.domain.Abono;
import com.sistema.credito.repository.AbonoRepository;
import com.sistema.credito.web.rest.errors.BadRequestAlertException;
import com.sistema.credito.web.rest.util.HeaderUtil;
import com.sistema.credito.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Abono.
 */
@RestController
@RequestMapping("/api")
public class AbonoResource {

    private final Logger log = LoggerFactory.getLogger(AbonoResource.class);

    private static final String ENTITY_NAME = "abono";

    private final AbonoRepository abonoRepository;

    public AbonoResource(AbonoRepository abonoRepository) {
        this.abonoRepository = abonoRepository;
    }

    /**
     * POST  /abonos : Create a new abono.
     *
     * @param abono the abono to create
     * @return the ResponseEntity with status 201 (Created) and with body the new abono, or with status 400 (Bad Request) if the abono has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/abonos")
    @Timed
    public ResponseEntity<Abono> createAbono(@RequestBody Abono abono) throws URISyntaxException {
        log.debug("REST request to save Abono : {}", abono);
        if (abono.getId() != null) {
            throw new BadRequestAlertException("A new abono cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Abono result = abonoRepository.save(abono);
        return ResponseEntity.created(new URI("/api/abonos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /abonos : Updates an existing abono.
     *
     * @param abono the abono to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated abono,
     * or with status 400 (Bad Request) if the abono is not valid,
     * or with status 500 (Internal Server Error) if the abono couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/abonos")
    @Timed
    public ResponseEntity<Abono> updateAbono(@RequestBody Abono abono) throws URISyntaxException {
        log.debug("REST request to update Abono : {}", abono);
        if (abono.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Abono result = abonoRepository.save(abono);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, abono.getId().toString()))
            .body(result);
    }

    /**
     * GET  /abonos : get all the abonos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of abonos in body
     */
    @GetMapping("/abonos")
    @Timed
    public ResponseEntity<List<Abono>> getAllAbonos(Pageable pageable) {
        log.debug("REST request to get a page of Abonos");
        Page<Abono> page = abonoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/abonos");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /abonos/:id : get the "id" abono.
     *
     * @param id the id of the abono to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the abono, or with status 404 (Not Found)
     */
    @GetMapping("/abonos/{id}")
    @Timed
    public ResponseEntity<Abono> getAbono(@PathVariable Long id) {
        log.debug("REST request to get Abono : {}", id);
        Optional<Abono> abono = abonoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(abono);
    }

    /**
     * DELETE  /abonos/:id : delete the "id" abono.
     *
     * @param id the id of the abono to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/abonos/{id}")
    @Timed
    public ResponseEntity<Void> deleteAbono(@PathVariable Long id) {
        log.debug("REST request to delete Abono : {}", id);

        abonoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
