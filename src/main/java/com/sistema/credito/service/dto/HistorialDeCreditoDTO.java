package com.sistema.credito.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the HistorialDeCredito entity.
 */
public class HistorialDeCreditoDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate fecha;

    private Long clienteId;

    private Long facturaId;

    private Long abonoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(Long facturaId) {
        this.facturaId = facturaId;
    }

    public Long getAbonoId() {
        return abonoId;
    }

    public void setAbonoId(Long abonoId) {
        this.abonoId = abonoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HistorialDeCreditoDTO historialDeCreditoDTO = (HistorialDeCreditoDTO) o;
        if (historialDeCreditoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), historialDeCreditoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HistorialDeCreditoDTO{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", cliente=" + getClienteId() +
            ", factura=" + getFacturaId() +
            ", abono=" + getAbonoId() +
            "}";
    }
}
