package com.sistema.credito.service.mapper;

import com.sistema.credito.domain.*;
import com.sistema.credito.service.dto.AbonoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Abono and its DTO AbonoDTO.
 */
@Mapper(componentModel = "spring", uses = {FacturaMapper.class})
public interface AbonoMapper extends EntityMapper<AbonoDTO, Abono> {

    @Mapping(source = "factura.id", target = "facturaId")
    AbonoDTO toDto(Abono abono);

    @Mapping(source = "facturaId", target = "factura")
    Abono toEntity(AbonoDTO abonoDTO);

    default Abono fromId(Long id) {
        if (id == null) {
            return null;
        }
        Abono abono = new Abono();
        abono.setId(id);
        return abono;
    }
}
