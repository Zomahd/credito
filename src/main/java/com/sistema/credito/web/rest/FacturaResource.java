package com.sistema.credito.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sistema.credito.domain.Factura;
import com.sistema.credito.repository.FacturaRepository;
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

    private final FacturaRepository facturaRepository;

    public FacturaResource(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    /**
     * POST  /facturas : Create a new factura.
     *
     * @param factura the factura to create
     * @return the ResponseEntity with status 201 (Created) and with body the new factura, or with status 400 (Bad Request) if the factura has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/facturas")
    @Timed
    public ResponseEntity<Factura> createFactura(@Valid @RequestBody Factura factura) throws URISyntaxException {
        log.debug("REST request to save Factura : {}", factura);
        if (factura.getId() != null) {
            throw new BadRequestAlertException("A new factura cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Factura result = facturaRepository.save(factura);
        return ResponseEntity.created(new URI("/api/facturas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /facturas : Updates an existing factura.
     *
     * @param factura the factura to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated factura,
     * or with status 400 (Bad Request) if the factura is not valid,
     * or with status 500 (Internal Server Error) if the factura couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/facturas")
    @Timed
    public ResponseEntity<Factura> updateFactura(@Valid @RequestBody Factura factura) throws URISyntaxException {
        log.debug("REST request to update Factura : {}", factura);
        if (factura.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Factura result = facturaRepository.save(factura);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, factura.getId().toString()))
            .body(result);
    }

    /**
     * GET  /facturas : get all the facturas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of facturas in body
     */
    @GetMapping("/facturas")
    @Timed
    public ResponseEntity<List<Factura>> getAllFacturas(Pageable pageable) {
        log.debug("REST request to get a page of Facturas");
        Page<Factura> page = facturaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/facturas");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /facturas/:id : get the "id" factura.
     *
     * @param id the id of the factura to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the factura, or with status 404 (Not Found)
     */
    @GetMapping("/facturas/{id}")
    @Timed
    public ResponseEntity<Factura> getFactura(@PathVariable Long id) {
        log.debug("REST request to get Factura : {}", id);
        Optional<Factura> factura = facturaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(factura);
    }

    /**
     * DELETE  /facturas/:id : delete the "id" factura.
     *
     * @param id the id of the factura to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/facturas/{id}")
    @Timed
    public ResponseEntity<Void> deleteFactura(@PathVariable Long id) {
        log.debug("REST request to delete Factura : {}", id);

        facturaRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
