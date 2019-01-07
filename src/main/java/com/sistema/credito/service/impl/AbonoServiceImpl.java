package com.sistema.credito.service.impl;

import com.sistema.credito.service.AbonoService;
import com.sistema.credito.domain.Abono;
import com.sistema.credito.repository.AbonoRepository;
import com.sistema.credito.service.dto.AbonoDTO;
import com.sistema.credito.service.mapper.AbonoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Abono.
 */
@Service
@Transactional
public class AbonoServiceImpl implements AbonoService {

    private final Logger log = LoggerFactory.getLogger(AbonoServiceImpl.class);

    private final AbonoRepository abonoRepository;

    private final AbonoMapper abonoMapper;

    public AbonoServiceImpl(AbonoRepository abonoRepository, AbonoMapper abonoMapper) {
        this.abonoRepository = abonoRepository;
        this.abonoMapper = abonoMapper;
    }

    /**
     * Save a abono.
     *
     * @param abonoDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AbonoDTO save(AbonoDTO abonoDTO) {
        log.debug("Request to save Abono : {}", abonoDTO);

        Abono abono = abonoMapper.toEntity(abonoDTO);
        abono = abonoRepository.save(abono);
        return abonoMapper.toDto(abono);
    }

    /**
     * Get all the abonos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AbonoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Abonos");
        return abonoRepository.findAll(pageable)
            .map(abonoMapper::toDto);
    }


    /**
     * Get one abono by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AbonoDTO> findOne(Long id) {
        log.debug("Request to get Abono : {}", id);
        return abonoRepository.findById(id)
            .map(abonoMapper::toDto);
    }

    /**
     * Delete the abono by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Abono : {}", id);
        abonoRepository.deleteById(id);
    }
}
