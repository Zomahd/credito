package com.sistema.credito.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.sistema.credito.domain.enumeration.EstadoDeFactura;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the Factura entity. This class is used in FacturaResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /facturas?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FacturaCriteria implements Serializable {
    /**
     * Class for filtering EstadoDeFactura
     */
    public static class EstadoDeFacturaFilter extends Filter<EstadoDeFactura> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numeroDeFactura;

    private LocalDateFilter fecha;

    private LongFilter total;

    private LongFilter abonado;

    private EstadoDeFacturaFilter estadoDeFactura;

    private LongFilter clienteId;

    private LongFilter abonoId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNumeroDeFactura() {
        return numeroDeFactura;
    }

    public void setNumeroDeFactura(StringFilter numeroDeFactura) {
        this.numeroDeFactura = numeroDeFactura;
    }

    public LocalDateFilter getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateFilter fecha) {
        this.fecha = fecha;
    }

    public LongFilter getTotal() {
        return total;
    }

    public void setTotal(LongFilter total) {
        this.total = total;
    }

    public LongFilter getAbonado() {
        return abonado;
    }

    public void setAbonado(LongFilter abonado) {
        this.abonado = abonado;
    }

    public EstadoDeFacturaFilter getEstadoDeFactura() {
        return estadoDeFactura;
    }

    public void setEstadoDeFactura(EstadoDeFacturaFilter estadoDeFactura) {
        this.estadoDeFactura = estadoDeFactura;
    }

    public LongFilter getClienteId() {
        return clienteId;
    }

    public void setClienteId(LongFilter clienteId) {
        this.clienteId = clienteId;
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
        final FacturaCriteria that = (FacturaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(numeroDeFactura, that.numeroDeFactura) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(total, that.total) &&
            Objects.equals(abonado, that.abonado) &&
            Objects.equals(estadoDeFactura, that.estadoDeFactura) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(abonoId, that.abonoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        numeroDeFactura,
        fecha,
        total,
        abonado,
        estadoDeFactura,
        clienteId,
        abonoId
        );
    }

    @Override
    public String toString() {
        return "FacturaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (numeroDeFactura != null ? "numeroDeFactura=" + numeroDeFactura + ", " : "") +
                (fecha != null ? "fecha=" + fecha + ", " : "") +
                (total != null ? "total=" + total + ", " : "") +
                (abonado != null ? "abonado=" + abonado + ", " : "") +
                (estadoDeFactura != null ? "estadoDeFactura=" + estadoDeFactura + ", " : "") +
                (clienteId != null ? "clienteId=" + clienteId + ", " : "") +
                (abonoId != null ? "abonoId=" + abonoId + ", " : "") +
            "}";
    }

}
