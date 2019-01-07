package com.sistema.credito.service;

import com.sistema.credito.service.dto.HistorialDeCreditoDTO;

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
     * @param historialDeCreditoDTO the entity to save
     * @return the persisted entity
     */
    HistorialDeCreditoDTO save(HistorialDeCreditoDTO historialDeCreditoDTO);

    /**
     * Get all the historialDeCreditos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<HistorialDeCreditoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" historialDeCredito.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<HistorialDeCreditoDTO> findOne(Long id);

    /**
     * Delete the "id" historialDeCredito.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
