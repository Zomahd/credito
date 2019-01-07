package com.sistema.credito.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sistema.credito.domain.HistorialDeCredito;
import com.sistema.credito.service.HistorialDeCreditoService;
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
 * REST controller for managing HistorialDeCredito.
 */
@RestController
@RequestMapping("/api")
public class HistorialDeCreditoResource {

    private final Logger log = LoggerFactory.getLogger(HistorialDeCreditoResource.class);

    private static final String ENTITY_NAME = "historialDeCredito";

    private final HistorialDeCreditoService historialDeCreditoService;

    public HistorialDeCreditoResource(HistorialDeCreditoService historialDeCreditoService) {
        this.historialDeCreditoService = historialDeCreditoService;
    }

    /**
     * POST  /historial-de-creditos : Create a new historialDeCredito.
     *
     * @param historialDeCredito the historialDeCredito to create
     * @return the ResponseEntity with status 201 (Created) and with body the new historialDeCredito, or with status 400 (Bad Request) if the historialDeCredito has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/historial-de-creditos")
    @Timed
    public ResponseEntity<HistorialDeCredito> createHistorialDeCredito(@RequestBody HistorialDeCredito historialDeCredito) throws URISyntaxException {
        log.debug("REST request to save HistorialDeCredito : {}", historialDeCredito);
        if (historialDeCredito.getId() != null) {
            throw new BadRequestAlertException("A new historialDeCredito cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistorialDeCredito result = historialDeCreditoService.save(historialDeCredito);
        return ResponseEntity.created(new URI("/api/historial-de-creditos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /historial-de-creditos : Updates an existing historialDeCredito.
     *
     * @param historialDeCredito the historialDeCredito to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated historialDeCredito,
     * or with status 400 (Bad Request) if the historialDeCredito is not valid,
     * or with status 500 (Internal Server Error) if the historialDeCredito couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/historial-de-creditos")
    @Timed
    public ResponseEntity<HistorialDeCredito> updateHistorialDeCredito(@RequestBody HistorialDeCredito historialDeCredito) throws URISyntaxException {
        log.debug("REST request to update HistorialDeCredito : {}", historialDeCredito);
        if (historialDeCredito.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HistorialDeCredito result = historialDeCreditoService.save(historialDeCredito);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, historialDeCredito.getId().toString()))
            .body(result);
    }

    /**
     * GET  /historial-de-creditos : get all the historialDeCreditos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of historialDeCreditos in body
     */
    @GetMapping("/historial-de-creditos")
    @Timed
    public ResponseEntity<List<HistorialDeCredito>> getAllHistorialDeCreditos(Pageable pageable) {
        log.debug("REST request to get a page of HistorialDeCreditos");
        Page<HistorialDeCredito> page = historialDeCreditoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/historial-de-creditos");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /historial-de-creditos/:id : get the "id" historialDeCredito.
     *
     * @param id the id of the historialDeCredito to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the historialDeCredito, or with status 404 (Not Found)
     */
    @GetMapping("/historial-de-creditos/{id}")
    @Timed
    public ResponseEntity<HistorialDeCredito> getHistorialDeCredito(@PathVariable Long id) {
        log.debug("REST request to get HistorialDeCredito : {}", id);
        Optional<HistorialDeCredito> historialDeCredito = historialDeCreditoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historialDeCredito);
    }

    /**
     * DELETE  /historial-de-creditos/:id : delete the "id" historialDeCredito.
     *
     * @param id the id of the historialDeCredito to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/historial-de-creditos/{id}")
    @Timed
    public ResponseEntity<Void> deleteHistorialDeCredito(@PathVariable Long id) {
        log.debug("REST request to delete HistorialDeCredito : {}", id);
        historialDeCreditoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
