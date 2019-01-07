package com.sistema.credito.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the HistorialDeCredito entity. This class is used in HistorialDeCreditoResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /historial-de-creditos?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HistorialDeCreditoCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fecha;

    private LongFilter clienteId;

    private LongFilter facturaId;

    private LongFilter abonoId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateFilter fecha) {
        this.fecha = fecha;
    }

    public LongFilter getClienteId() {
        return clienteId;
    }

    public void setClienteId(LongFilter clienteId) {
        this.clienteId = clienteId;
    }

    public LongFilter getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(LongFilter facturaId) {
        this.facturaId = facturaId;
    }

    public LongFilter getAbonoId() {
        return abonoId;
    }

    public void setAbonoId(LongFilter abonoId) {
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
        final HistorialDeCreditoCriteria that = (HistorialDeCreditoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(facturaId, that.facturaId) &&
            Objects.equals(abonoId, that.abonoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fecha,
        clienteId,
        facturaId,
        abonoId
        );
    }

    @Override
    public String toString() {
        return "HistorialDeCreditoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fecha != null ? "fecha=" + fecha + ", " : "") +
                (clienteId != null ? "clienteId=" + clienteId + ", " : "") +
                (facturaId != null ? "facturaId=" + facturaId + ", " : "") +
                (abonoId != null ? "abonoId=" + abonoId + ", " : "") +
            "}";
    }

}
