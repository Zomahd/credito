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

import com.sistema.credito.domain.Abono;
import com.sistema.credito.domain.*; // for static metamodels
import com.sistema.credito.repository.AbonoRepository;
import com.sistema.credito.service.dto.AbonoCriteria;
import com.sistema.credito.service.dto.AbonoDTO;
import com.sistema.credito.service.mapper.AbonoMapper;

/**
 * Service for executing complex queries for Abono entities in the database.
 * The main input is a {@link AbonoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AbonoDTO} or a {@link Page} of {@link AbonoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AbonoQueryService extends QueryService<Abono> {

    private final Logger log = LoggerFactory.getLogger(AbonoQueryService.class);

    private final AbonoRepository abonoRepository;

    private final AbonoMapper abonoMapper;

    public AbonoQueryService(AbonoRepository abonoRepository, AbonoMapper abonoMapper) {
        this.abonoRepository = abonoRepository;
        this.abonoMapper = abonoMapper;
    }

    /**
     * Return a {@link List} of {@link AbonoDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AbonoDTO> findByCriteria(AbonoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Abono> specification = createSpecification(criteria);
        return abonoMapper.toDto(abonoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AbonoDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AbonoDTO> findByCriteria(AbonoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Abono> specification = createSpecification(criteria);
        return abonoRepository.findAll(specification, page)
            .map(abonoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AbonoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Abono> specification = createSpecification(criteria);
        return abonoRepository.count(specification);
    }

    /**
     * Function to convert AbonoCriteria to a {@link Specification}
     */
    private Specification<Abono> createSpecification(AbonoCriteria criteria) {
        Specification<Abono> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Abono_.id));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), Abono_.fecha));
            }
            if (criteria.getAbono() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAbono(), Abono_.abono));
            }
            if (criteria.getFacturaId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacturaId(),
                    root -> root.join(Abono_.factura, JoinType.LEFT).get(Factura_.id)));
            }
        }
        return specification;
    }
}
