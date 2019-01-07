package com.sistema.credito.service;

import com.sistema.credito.domain.HistorialDeCredito;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing HistorialDeCredito.
 */
public interface HistorialDeCreditoService {

    /**
     * Save a historialDeCredito.
     *
     * @param historialDeCredito the entity to save
     * @return the persisted entity
     */
    HistorialDeCredito save(HistorialDeCredito historialDeCredito);

    /**
     * Get all the historialDeCreditos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<HistorialDeCredito> findAll(Pageable pageable);


    /**
     * Get the "id" historialDeCredito.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<HistorialDeCredito> findOne(Long id);

    /**
     * Delete the "id" historialDeCredito.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
