package com.sistema.credito.service.mapper;

import com.sistema.credito.domain.*;
import com.sistema.credito.service.dto.FacturaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Factura and its DTO FacturaDTO.
 */
@Mapper(componentModel = "spring", uses = {ClienteMapper.class})
public interface FacturaMapper extends EntityMapper<FacturaDTO, Factura> {

    @Mapping(source = "cliente.id", target = "clienteId")
    FacturaDTO toDto(Factura factura);

    @Mapping(source = "clienteId", target = "cliente")
    @Mapping(target = "abonos", ignore = true)
    Factura toEntity(FacturaDTO facturaDTO);

    default Factura fromId(Long id) {
        if (id == null) {
            return null;
        }
        Factura factura = new Factura();
        factura.setId(id);
        return factura;
    }
}
