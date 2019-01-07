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

/**
 * Criteria class for the Cliente entity. This class is used in ClienteResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /clientes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClienteCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter primerApellido;

    private StringFilter segundoApellido;

    private StringFilter correo;

    private StringFilter telefono;

    private LongFilter facturaId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(StringFilter primerApellido) {
        this.primerApellido = primerApellido;
    }

    public StringFilter getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(StringFilter segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public StringFilter getCorreo() {
        return correo;
    }

    public void setCorreo(StringFilter correo) {
        this.correo = correo;
    }

    public StringFilter getTelefono() {
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
    }

    public LongFilter getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(LongFilter facturaId) {
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
        final ClienteCriteria that = (ClienteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(primerApellido, that.primerApellido) &&
            Objects.equals(segundoApellido, that.segundoApellido) &&
            Objects.equals(correo, that.correo) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(facturaId, that.facturaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nombre,
        primerApellido,
        segundoApellido,
        correo,
        telefono,
        facturaId
        );
    }

    @Override
    public String toString() {
        return "ClienteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nombre != null ? "nombre=" + nombre + ", " : "") +
                (primerApellido != null ? "primerApellido=" + primerApellido + ", " : "") +
                (segundoApellido != null ? "segundoApellido=" + segundoApellido + ", " : "") +
                (correo != null ? "correo=" + correo + ", " : "") +
                (telefono != null ? "telefono=" + telefono + ", " : "") +
                (facturaId != null ? "facturaId=" + facturaId + ", " : "") +
            "}";
    }

}
