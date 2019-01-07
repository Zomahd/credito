package com.sistema.credito.service.impl;

import com.sistema.credito.service.FacturaService;
import com.sistema.credito.domain.Factura;
import com.sistema.credito.repository.FacturaRepository;
import com.sistema.credito.service.dto.FacturaDTO;
import com.sistema.credito.service.mapper.FacturaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Factura.
 */
@Service
@Transactional
public class FacturaServiceImpl implements FacturaService {

    private final Logger log = LoggerFactory.getLogger(FacturaServiceImpl.class);

    private final FacturaRepository facturaRepository;

    private final FacturaMapper facturaMapper;

    public FacturaServiceImpl(FacturaRepository facturaRepository, FacturaMapper facturaMapper) {
        this.facturaRepository = facturaRepository;
        this.facturaMapper = facturaMapper;
    }

    /**
     * Save a factura.
     *
     * @param facturaDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FacturaDTO save(FacturaDTO facturaDTO) {
        log.debug("Request to save Factura : {}", facturaDTO);

        Factura factura = facturaMapper.toEntity(facturaDTO);
        factura = facturaRepository.save(factura);
        return facturaMapper.toDto(factura);
    }

    /**
     * Get all the facturas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FacturaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Facturas");
        return facturaRepository.findAll(pageable)
            .map(facturaMapper::toDto);
    }


    /**
     * Get one factura by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FacturaDTO> findOne(Long id) {
        log.debug("Request to get Factura : {}", id);
        return facturaRepository.findById(id)
            .map(facturaMapper::toDto);
    }

    /**
     * Delete the factura by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Factura : {}", id);
        facturaRepository.deleteById(id);
    }
}
