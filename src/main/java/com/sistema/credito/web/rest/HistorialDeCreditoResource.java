package com.sistema.credito.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sistema.credito.service.HistorialDeCreditoService;
import com.sistema.credito.web.rest.errors.BadRequestAlertException;
import com.sistema.credito.web.rest.util.HeaderUtil;
import com.sistema.credito.web.rest.util.PaginationUtil;
import com.sistema.credito.service.dto.HistorialDeCreditoDTO;
import com.sistema.credito.service.dto.HistorialDeCreditoCriteria;
import com.sistema.credito.service.HistorialDeCreditoQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    private final HistorialDeCreditoQueryService historialDeCreditoQueryService;

    public HistorialDeCreditoResource(HistorialDeCreditoService historialDeCreditoService, HistorialDeCreditoQueryService historialDeCreditoQueryService) {
        this.historialDeCreditoService = historialDeCreditoService;
        this.historialDeCreditoQueryService = historialDeCreditoQueryService;
    }

    /**
     * POST  /historial-de-creditos : Create a new historialDeCredito.
     *
     * @param historialDeCreditoDTO the historialDeCreditoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new historialDeCreditoDTO, or with status 400 (Bad Request) if the historialDeCredito has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/historial-de-creditos")
    @Timed
    public ResponseEntity<HistorialDeCreditoDTO> createHistorialDeCredito(@Valid @RequestBody HistorialDeCreditoDTO historialDeCreditoDTO) throws URISyntaxException {
        log.debug("REST request to save HistorialDeCredito : {}", historialDeCreditoDTO);
        if (historialDeCreditoDTO.getId() != null) {
            throw new BadRequestAlertException("A new historialDeCredito cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistorialDeCreditoDTO result = historialDeCreditoService.save(historialDeCreditoDTO);
        return ResponseEntity.created(new URI("/api/historial-de-creditos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /historial-de-creditos : Updates an existing historialDeCredito.
     *
     * @param historialDeCreditoDTO the historialDeCreditoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated historialDeCreditoDTO,
     * or with status 400 (Bad Request) if the historialDeCreditoDTO is not valid,
     * or with status 500 (Internal Server Error) if the historialDeCreditoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/historial-de-creditos")
    @Timed
    public ResponseEntity<HistorialDeCreditoDTO> updateHistorialDeCredito(@Valid @RequestBody HistorialDeCreditoDTO historialDeCreditoDTO) throws URISyntaxException {
        log.debug("REST request to update HistorialDeCredito : {}", historialDeCreditoDTO);
        if (historialDeCreditoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HistorialDeCreditoDTO result = historialDeCreditoService.save(historialDeCreditoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, historialDeCreditoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /historial-de-creditos : get all the historialDeCreditos.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of historialDeCreditos in body
     */
    @GetMapping("/historial-de-creditos")
    @Timed
    public ResponseEntity<List<HistorialDeCreditoDTO>> getAllHistorialDeCreditos(HistorialDeCreditoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get HistorialDeCreditos by criteria: {}", criteria);
        Page<HistorialDeCreditoDTO> page = historialDeCreditoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/historial-de-creditos");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /historial-de-creditos/count : count all the historialDeCreditos.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/historial-de-creditos/count")
    @Timed
    public ResponseEntity<Long> countHistorialDeCreditos(HistorialDeCreditoCriteria criteria) {
        log.debug("REST request to count HistorialDeCreditos by criteria: {}", criteria);
        return ResponseEntity.ok().body(historialDeCreditoQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /historial-de-creditos/:id : get the "id" historialDeCredito.
     *
     * @param id the id of the historialDeCreditoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the historialDeCreditoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/historial-de-creditos/{id}")
    @Timed
    public ResponseEntity<HistorialDeCreditoDTO> getHistorialDeCredito(@PathVariable Long id) {
        log.debug("REST request to get HistorialDeCredito : {}", id);
        Optional<HistorialDeCreditoDTO> historialDeCreditoDTO = historialDeCreditoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historialDeCreditoDTO);
    }

    /**
     * DELETE  /historial-de-creditos/:id : delete the "id" historialDeCredito.
     *
     * @param id the id of the historialDeCreditoDTO to delete
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
