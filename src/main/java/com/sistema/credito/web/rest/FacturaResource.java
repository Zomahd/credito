package com.sistema.credito.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sistema.credito.service.FacturaService;
import com.sistema.credito.web.rest.errors.BadRequestAlertException;
import com.sistema.credito.web.rest.util.HeaderUtil;
import com.sistema.credito.web.rest.util.PaginationUtil;
import com.sistema.credito.service.dto.FacturaDTO;
import com.sistema.credito.service.dto.FacturaCriteria;
import com.sistema.credito.service.FacturaQueryService;
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
 * REST controller for managing Factura.
 */
@RestController
@RequestMapping("/api")
public class FacturaResource {

    private final Logger log = LoggerFactory.getLogger(FacturaResource.class);

    private static final String ENTITY_NAME = "factura";

    private final FacturaService facturaService;

    private final FacturaQueryService facturaQueryService;

    public FacturaResource(FacturaService facturaService, FacturaQueryService facturaQueryService) {
        this.facturaService = facturaService;
        this.facturaQueryService = facturaQueryService;
    }

    /**
     * POST  /facturas : Create a new factura.
     *
     * @param facturaDTO the facturaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new facturaDTO, or with status 400 (Bad Request) if the factura has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/facturas")
    @Timed
    public ResponseEntity<FacturaDTO> createFactura(@Valid @RequestBody FacturaDTO facturaDTO) throws URISyntaxException {
        log.debug("REST request to save Factura : {}", facturaDTO);
        if (facturaDTO.getId() != null) {
            throw new BadRequestAlertException("A new factura cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacturaDTO result = facturaService.save(facturaDTO);
        return ResponseEntity.created(new URI("/api/facturas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /facturas : Updates an existing factura.
     *
     * @param facturaDTO the facturaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated facturaDTO,
     * or with status 400 (Bad Request) if the facturaDTO is not valid,
     * or with status 500 (Internal Server Error) if the facturaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/facturas")
    @Timed
    public ResponseEntity<FacturaDTO> updateFactura(@Valid @RequestBody FacturaDTO facturaDTO) throws URISyntaxException {
        log.debug("REST request to update Factura : {}", facturaDTO);
        if (facturaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FacturaDTO result = facturaService.save(facturaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, facturaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /facturas : get all the facturas.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of facturas in body
     */
    @GetMapping("/facturas")
    @Timed
    public ResponseEntity<List<FacturaDTO>> getAllFacturas(FacturaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Facturas by criteria: {}", criteria);
        Page<FacturaDTO> page = facturaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/facturas");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /facturas/count : count all the facturas.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/facturas/count")
    @Timed
    public ResponseEntity<Long> countFacturas(FacturaCriteria criteria) {
        log.debug("REST request to count Facturas by criteria: {}", criteria);
        return ResponseEntity.ok().body(facturaQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /facturas/:id : get the "id" factura.
     *
     * @param id the id of the facturaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the facturaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/facturas/{id}")
    @Timed
    public ResponseEntity<FacturaDTO> getFactura(@PathVariable Long id) {
        log.debug("REST request to get Factura : {}", id);
        Optional<FacturaDTO> facturaDTO = facturaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facturaDTO);
    }

    /**
     * DELETE  /facturas/:id : delete the "id" factura.
     *
     * @param id the id of the facturaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/facturas/{id}")
    @Timed
    public ResponseEntity<Void> deleteFactura(@PathVariable Long id) {
        log.debug("REST request to delete Factura : {}", id);
        facturaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
