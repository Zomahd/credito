package com.sistema.credito.service;

import com.sistema.credito.service.dto.AbonoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Abono.
 */
public interface AbonoService {

    /**
     * Save a abono.
     *
     * @param abonoDTO the entity to save
     * @return the persisted entity
     */
    AbonoDTO save(AbonoDTO abonoDTO);

    /**
     * Get all the abonos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AbonoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" abono.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AbonoDTO> findOne(Long id);

    /**
     * Delete the "id" abono.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
