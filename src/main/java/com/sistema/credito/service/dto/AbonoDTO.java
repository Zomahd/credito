package com.sistema.credito.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Abono entity.
 */
public class AbonoDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private Long abono;

    private Long facturaId;

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

    public Long getAbono() {
        return abono;
    }

    public void setAbono(Long abono) {
        this.abono = abono;
    }

    public Long getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(Long facturaId) {
        this.facturaId = facturaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbonoDTO abonoDTO = (AbonoDTO) o;
        if (abonoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), abonoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AbonoDTO{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", abono=" + getAbono() +
            ", factura=" + getFacturaId() +
            "}";
    }
}
