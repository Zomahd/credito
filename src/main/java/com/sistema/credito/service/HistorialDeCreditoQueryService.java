package com.sistema.credito.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.sistema.credito.domain.HistorialDeCredito;
import com.sistema.credito.domain.*; // for static metamodels
import com.sistema.credito.repository.HistorialDeCreditoRepository;
import com.sistema.credito.service.dto.HistorialDeCreditoCriteria;
import com.sistema.credito.service.dto.HistorialDeCreditoDTO;
import com.sistema.credito.service.mapper.HistorialDeCreditoMapper;

/**
 * Service for executing complex queries for HistorialDeCredito entities in the database.
 * The main input is a {@link HistorialDeCreditoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HistorialDeCreditoDTO} or a {@link Page} of {@link HistorialDeCreditoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HistorialDeCreditoQueryService extends QueryService<HistorialDeCredito> {

    private final Logger log = LoggerFactory.getLogger(HistorialDeCreditoQueryService.class);

    private final HistorialDeCreditoRepository historialDeCreditoRepository;

    private final HistorialDeCreditoMapper historialDeCreditoMapper;

    public HistorialDeCreditoQueryService(HistorialDeCreditoRepository historialDeCreditoRepository, HistorialDeCreditoMapper historialDeCreditoMapper) {
        this.historialDeCreditoRepository = historialDeCreditoRepository;
        this.historialDeCreditoMapper = historialDeCreditoMapper;
    }

    /**
     * Return a {@link List} of {@link HistorialDeCreditoDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HistorialDeCreditoDTO> findByCriteria(HistorialDeCreditoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HistorialDeCredito> specification = createSpecification(criteria);
        return historialDeCreditoMapper.toDto(historialDeCreditoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HistorialDeCreditoDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HistorialDeCreditoDTO> findByCriteria(HistorialDeCreditoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HistorialDeCredito> specification = createSpecification(criteria);
        return historialDeCreditoRepository.findAll(specification, page)
            .map(historialDeCreditoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HistorialDeCreditoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HistorialDeCredito> specification = createSpecification(criteria);
        return historialDeCreditoRepository.count(specification);
    }

    /**
     * Function to convert HistorialDeCreditoCriteria to a {@link Specification}
     */
    private Specification<HistorialDeCredito> createSpecification(HistorialDeCreditoCriteria criteria) {
        Specification<HistorialDeCredito> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), HistorialDeCredito_.id));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), HistorialDeCredito_.fecha));
            }
            if (criteria.getClienteId() != null) {
                specification = specification.and(buildSpecification(criteria.getClienteId(),
                    root -> root.join(HistorialDeCredito_.cliente, JoinType.LEFT).get(Cliente_.id)));
            }
            if (criteria.getFacturaId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacturaId(),
                    root -> root.join(HistorialDeCredito_.factura, JoinType.LEFT).get(Factura_.id)));
            }
            if (criteria.getAbonoId() != null) {
                specification = specification.and(buildSpecification(criteria.getAbonoId(),
                    root -> root.join(HistorialDeCredito_.abono, JoinType.LEFT).get(Abono_.id)));
            }
        }
        return specification;
    }
}
