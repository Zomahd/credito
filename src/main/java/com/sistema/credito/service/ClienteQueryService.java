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

import com.sistema.credito.domain.Cliente;
import com.sistema.credito.domain.*; // for static metamodels
import com.sistema.credito.repository.ClienteRepository;
import com.sistema.credito.service.dto.ClienteCriteria;
import com.sistema.credito.service.dto.ClienteDTO;
import com.sistema.credito.service.mapper.ClienteMapper;

/**
 * Service for executing complex queries for Cliente entities in the database.
 * The main input is a {@link ClienteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClienteDTO} or a {@link Page} of {@link ClienteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClienteQueryService extends QueryService<Cliente> {

    private final Logger log = LoggerFactory.getLogger(ClienteQueryService.class);

    private final ClienteRepository clienteRepository;

    private final ClienteMapper clienteMapper;

    public ClienteQueryService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    /**
     * Return a {@link List} of {@link ClienteDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClienteDTO> findByCriteria(ClienteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cliente> specification = createSpecification(criteria);
        return clienteMapper.toDto(clienteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ClienteDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClienteDTO> findByCriteria(ClienteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cliente> specification = createSpecification(criteria);
        return clienteRepository.findAll(specification, page)
            .map(clienteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClienteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cliente> specification = createSpecification(criteria);
        return clienteRepository.count(specification);
    }

    /**
     * Function to convert ClienteCriteria to a {@link Specification}
     */
    private Specification<Cliente> createSpecification(ClienteCriteria criteria) {
        Specification<Cliente> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Cliente_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Cliente_.nombre));
            }
            if (criteria.getPrimerApellido() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrimerApellido(), Cliente_.primerApellido));
            }
            if (criteria.getSegundoApellido() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSegundoApellido(), Cliente_.segundoApellido));
            }
            if (criteria.getCorreo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCorreo(), Cliente_.correo));
            }
            if (criteria.getTelefono() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefono(), Cliente_.telefono));
            }
            if (criteria.getFacturaId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacturaId(),
                    root -> root.join(Cliente_.facturas, JoinType.LEFT).get(Factura_.id)));
            }
        }
        return specification;
    }
}
