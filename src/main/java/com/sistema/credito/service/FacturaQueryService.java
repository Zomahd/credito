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

import com.sistema.credito.domain.Factura;
import com.sistema.credito.domain.*; // for static metamodels
import com.sistema.credito.repository.FacturaRepository;
import com.sistema.credito.service.dto.FacturaCriteria;
import com.sistema.credito.service.dto.FacturaDTO;
import com.sistema.credito.service.mapper.FacturaMapper;

/**
 * Service for executing complex queries for Factura entities in the database.
 * The main input is a {@link FacturaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FacturaDTO} or a {@link Page} of {@link FacturaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacturaQueryService extends QueryService<Factura> {

    private final Logger log = LoggerFactory.getLogger(FacturaQueryService.class);

    private final FacturaRepository facturaRepository;

    private final FacturaMapper facturaMapper;

    public FacturaQueryService(FacturaRepository facturaRepository, FacturaMapper facturaMapper) {
        this.facturaRepository = facturaRepository;
        this.facturaMapper = facturaMapper;
    }

    /**
     * Return a {@link List} of {@link FacturaDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FacturaDTO> findByCriteria(FacturaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Factura> specification = createSpecification(criteria);
        return facturaMapper.toDto(facturaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FacturaDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacturaDTO> findByCriteria(FacturaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Factura> specification = createSpecification(criteria);
        return facturaRepository.findAll(specification, page)
            .map(facturaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacturaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Factura> specification = createSpecification(criteria);
        return facturaRepository.count(specification);
    }

    /**
     * Function to convert FacturaCriteria to a {@link Specification}
     */
    private Specification<Factura> createSpecification(FacturaCriteria criteria) {
        Specification<Factura> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Factura_.id));
            }
            if (criteria.getNumeroDeFactura() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumeroDeFactura(), Factura_.numeroDeFactura));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), Factura_.fecha));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), Factura_.total));
            }
            if (criteria.getAbonado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAbonado(), Factura_.abonado));
            }
            if (criteria.getEstadoDeFactura() != null) {
                specification = specification.and(buildSpecification(criteria.getEstadoDeFactura(), Factura_.estadoDeFactura));
            }
            if (criteria.getClienteId() != null) {
                specification = specification.and(buildSpecification(criteria.getClienteId(),
                    root -> root.join(Factura_.cliente, JoinType.LEFT).get(Cliente_.id)));
            }
            if (criteria.getAbonoId() != null) {
                specification = specification.and(buildSpecification(criteria.getAbonoId(),
                    root -> root.join(Factura_.abonos, JoinType.LEFT).get(Abono_.id)));
            }
        }
        return specification;
    }
}
