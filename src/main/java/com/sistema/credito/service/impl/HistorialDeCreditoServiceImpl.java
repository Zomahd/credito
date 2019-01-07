package com.sistema.credito.service.impl;

import com.sistema.credito.service.HistorialDeCreditoService;
import com.sistema.credito.domain.HistorialDeCredito;
import com.sistema.credito.repository.HistorialDeCreditoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing HistorialDeCredito.
 */
@Service
@Transactional
public class HistorialDeCreditoServiceImpl implements HistorialDeCreditoService {

    private final Logger log = LoggerFactory.getLogger(HistorialDeCreditoServiceImpl.class);

    private final HistorialDeCreditoRepository historialDeCreditoRepository;

    public HistorialDeCreditoServiceImpl(HistorialDeCreditoRepository historialDeCreditoRepository) {
        this.historialDeCreditoRepository = historialDeCreditoRepository;
    }

    /**
     * Save a historialDeCredito.
     *
     * @param historialDeCredito the entity to save
     * @return the persisted entity
     */
    @Override
    public HistorialDeCredito save(HistorialDeCredito historialDeCredito) {
        log.debug("Request to save HistorialDeCredito : {}", historialDeCredito);
        return historialDeCreditoRepository.save(historialDeCredito);
    }

    /**
     * Get all the historialDeCreditos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HistorialDeCredito> findAll(Pageable pageable) {
        log.debug("Request to get all HistorialDeCreditos");
        return historialDeCreditoRepository.findAll(pageable);
    }


    /**
     * Get one historialDeCredito by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HistorialDeCredito> findOne(Long id) {
        log.debug("Request to get HistorialDeCredito : {}", id);
        return historialDeCreditoRepository.findById(id);
    }

    /**
     * Delete the historialDeCredito by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete HistorialDeCredito : {}", id);
        historialDeCreditoRepository.deleteById(id);
    }
}
