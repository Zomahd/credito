package com.sistema.credito.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * The HistorialDeCredito entity.
 */
@ApiModel(description = "The HistorialDeCredito entity.")
@Entity
@Table(name = "historial_de_credito")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HistorialDeCredito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @OneToOne    @JoinColumn(unique = true)
    private Factura factura;

    @OneToOne    @JoinColumn(unique = true)
    private Cliente cliente;

    @OneToOne    @JoinColumn(unique = true)
    private Abono abono;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public HistorialDeCredito startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public HistorialDeCredito endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Factura getFactura() {
        return factura;
    }

    public HistorialDeCredito factura(Factura factura) {
        this.factura = factura;
        return this;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public HistorialDeCredito cliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Abono getAbono() {
        return abono;
    }

    public HistorialDeCredito abono(Abono abono) {
        this.abono = abono;
        return this;
    }

    public void setAbono(Abono abono) {
        this.abono = abono;
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
        HistorialDeCredito historialDeCredito = (HistorialDeCredito) o;
        if (historialDeCredito.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), historialDeCredito.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HistorialDeCredito{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
