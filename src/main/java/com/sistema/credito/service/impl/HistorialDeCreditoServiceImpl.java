package com.sistema.credito.service.impl;

import com.sistema.credito.service.HistorialDeCreditoService;
import com.sistema.credito.domain.HistorialDeCredito;
import com.sistema.credito.repository.HistorialDeCreditoRepository;
import com.sistema.credito.service.dto.HistorialDeCreditoDTO;
import com.sistema.credito.service.mapper.HistorialDeCreditoMapper;
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

    private final HistorialDeCreditoMapper historialDeCreditoMapper;

    public HistorialDeCreditoServiceImpl(HistorialDeCreditoRepository historialDeCreditoRepository, HistorialDeCreditoMapper historialDeCreditoMapper) {
        this.historialDeCreditoRepository = historialDeCreditoRepository;
        this.historialDeCreditoMapper = historialDeCreditoMapper;
    }

    /**
     * Save a historialDeCredito.
     *
     * @param historialDeCreditoDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public HistorialDeCreditoDTO save(HistorialDeCreditoDTO historialDeCreditoDTO) {
        log.debug("Request to save HistorialDeCredito : {}", historialDeCreditoDTO);

        HistorialDeCredito historialDeCredito = historialDeCreditoMapper.toEntity(historialDeCreditoDTO);
        historialDeCredito = historialDeCreditoRepository.save(historialDeCredito);
        return historialDeCreditoMapper.toDto(historialDeCredito);
    }

    /**
     * Get all the historialDeCreditos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HistorialDeCreditoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HistorialDeCreditos");
        return historialDeCreditoRepository.findAll(pageable)
            .map(historialDeCreditoMapper::toDto);
    }


    /**
     * Get one historialDeCredito by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HistorialDeCreditoDTO> findOne(Long id) {
        log.debug("Request to get HistorialDeCredito : {}", id);
        return historialDeCreditoRepository.findById(id)
            .map(historialDeCreditoMapper::toDto);
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
