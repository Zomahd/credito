package com.sistema.credito.service.mapper;

import com.sistema.credito.domain.*;
import com.sistema.credito.service.dto.HistorialDeCreditoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity HistorialDeCredito and its DTO HistorialDeCreditoDTO.
 */
@Mapper(componentModel = "spring", uses = {ClienteMapper.class, FacturaMapper.class, AbonoMapper.class})
public interface HistorialDeCreditoMapper extends EntityMapper<HistorialDeCreditoDTO, HistorialDeCredito> {

    @Mapping(source = "cliente.id", target = "clienteId")
    @Mapping(source = "factura.id", target = "facturaId")
    @Mapping(source = "abono.id", target = "abonoId")
    HistorialDeCreditoDTO toDto(HistorialDeCredito historialDeCredito);

    @Mapping(source = "clienteId", target = "cliente")
    @Mapping(source = "facturaId", target = "factura")
    @Mapping(source = "abonoId", target = "abono")
    HistorialDeCredito toEntity(HistorialDeCreditoDTO historialDeCreditoDTO);

    default HistorialDeCredito fromId(Long id) {
        if (id == null) {
            return null;
        }
        HistorialDeCredito historialDeCredito = new HistorialDeCredito();
        historialDeCredito.setId(id);
        return historialDeCredito;
    }
}
