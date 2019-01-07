package com.sistema.credito.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.sistema.credito.domain.enumeration.EstadoDeFactura;

/**
 * A DTO for the Factura entity.
 */
public class FacturaDTO implements Serializable {

    private Long id;

    @NotNull
    private String numeroDeFactura;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private Long total;

    private Long abonado;

    @NotNull
    private EstadoDeFactura estadoDeFactura;

    private Long clienteId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroDeFactura() {
        return numeroDeFactura;
    }

    public void setNumeroDeFactura(String numeroDeFactura) {
        this.numeroDeFactura = numeroDeFactura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getAbonado() {
        return abonado;
    }

    public void setAbonado(Long abonado) {
        this.abonado = abonado;
    }

    public EstadoDeFactura getEstadoDeFactura() {
        return estadoDeFactura;
    }

    public void setEstadoDeFactura(EstadoDeFactura estadoDeFactura) {
        this.estadoDeFactura = estadoDeFactura;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FacturaDTO facturaDTO = (FacturaDTO) o;
        if (facturaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), facturaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FacturaDTO{" +
            "id=" + getId() +
            ", numeroDeFactura='" + getNumeroDeFactura() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", total=" + getTotal() +
            ", abonado=" + getAbonado() +
            ", estadoDeFactura='" + getEstadoDeFactura() + "'" +
            ", cliente=" + getClienteId() +
            "}";
    }
}
