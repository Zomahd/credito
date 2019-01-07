package com.sistema.credito.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * The Abono entity.
 */
@ApiModel(description = "The Abono entity.")
@Entity
@Table(name = "abono")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Abono implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha")
    private Instant fecha;

    @Column(name = "abono")
    private Long abono;

    @ManyToOne
    @JsonIgnoreProperties("abonos")
    private Factura factura;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFecha() {
        return fecha;
    }

    public Abono fecha(Instant fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Long getAbono() {
        return abono;
    }

    public Abono abono(Long abono) {
        this.abono = abono;
        return this;
    }

    public void setAbono(Long abono) {
        this.abono = abono;
    }

    public Factura getFactura() {
        return factura;
    }

    public Abono factura(Factura factura) {
        this.factura = factura;
        return this;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Abono abono = (Abono) o;
        if (abono.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), abono.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Abono{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", abono=" + getAbono() +
            "}";
    }
}
