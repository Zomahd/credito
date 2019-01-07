package com.sistema.credito.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sistema.credito.service.AbonoService;
import com.sistema.credito.web.rest.errors.BadRequestAlertException;
import com.sistema.credito.web.rest.util.HeaderUtil;
import com.sistema.credito.web.rest.util.PaginationUtil;
import com.sistema.credito.service.dto.AbonoDTO;
import com.sistema.credito.service.dto.AbonoCriteria;
import com.sistema.credito.service.AbonoQueryService;
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
 * REST controller for managing Abono.
 */
@RestController
@RequestMapping("/api")
public class AbonoResource {

    private final Logger log = LoggerFactory.getLogger(AbonoResource.class);

    private static final String ENTITY_NAME = "abono";

    private final AbonoService abonoService;

    private final AbonoQueryService abonoQueryService;

    public AbonoResource(AbonoService abonoService, AbonoQueryService abonoQueryService) {
        this.abonoService = abonoService;
        this.abonoQueryService = abonoQueryService;
    }

    /**
     * POST  /abonos : Create a new abono.
     *
     * @param abonoDTO the abonoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new abonoDTO, or with status 400 (Bad Request) if the abono has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/abonos")
    @Timed
    public ResponseEntity<AbonoDTO> createAbono(@Valid @RequestBody AbonoDTO abonoDTO) throws URISyntaxException {
        log.debug("REST request to save Abono : {}", abonoDTO);
        if (abonoDTO.getId() != null) {
            throw new BadRequestAlertException("A new abono cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AbonoDTO result = abonoService.save(abonoDTO);
        return ResponseEntity.created(new URI("/api/abonos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /abonos : Updates an existing abono.
     *
     * @param abonoDTO the abonoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated abonoDTO,
     * or with status 400 (Bad Request) if the abonoDTO is not valid,
     * or with status 500 (Internal Server Error) if the abonoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/abonos")
    @Timed
    public ResponseEntity<AbonoDTO> updateAbono(@Valid @RequestBody AbonoDTO abonoDTO) throws URISyntaxException {
        log.debug("REST request to update Abono : {}", abonoDTO);
        if (abonoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AbonoDTO result = abonoService.save(abonoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, abonoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /abonos : get all the abonos.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of abonos in body
     */
    @GetMapping("/abonos")
    @Timed
    public ResponseEntity<List<AbonoDTO>> getAllAbonos(AbonoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Abonos by criteria: {}", criteria);
        Page<AbonoDTO> page = abonoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/abonos");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /abonos/count : count all the abonos.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/abonos/count")
    @Timed
    public ResponseEntity<Long> countAbonos(AbonoCriteria criteria) {
        log.debug("REST request to count Abonos by criteria: {}", criteria);
        return ResponseEntity.ok().body(abonoQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /abonos/:id : get the "id" abono.
     *
     * @param id the id of the abonoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the abonoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/abonos/{id}")
    @Timed
    public ResponseEntity<AbonoDTO> getAbono(@PathVariable Long id) {
        log.debug("REST request to get Abono : {}", id);
        Optional<AbonoDTO> abonoDTO = abonoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(abonoDTO);
    }

    /**
     * DELETE  /abonos/:id : delete the "id" abono.
     *
     * @param id the id of the abonoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/abonos/{id}")
    @Timed
    public ResponseEntity<Void> deleteAbono(@PathVariable Long id) {
        log.debug("REST request to delete Abono : {}", id);
        abonoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
