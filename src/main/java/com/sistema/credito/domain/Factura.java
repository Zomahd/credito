package com.sistema.credito.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.sistema.credito.domain.enumeration.EstadoDeFactura;

/**
 * The Factura entity.
 */
@ApiModel(description = "The Factura entity.")
@Entity
@Table(name = "factura")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "numero_de_factura", nullable = false)
    private String numeroDeFactura;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "total", nullable = false)
    private Long total;

    @Column(name = "abonado")
    private Long abonado;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_de_factura", nullable = false)
    private EstadoDeFactura estadoDeFactura;

    @ManyToOne
    @JsonIgnoreProperties("facturas")
    private Cliente cliente;

    @OneToMany(mappedBy = "factura")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Abono> abonos = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroDeFactura() {
        return numeroDeFactura;
    }

    public Factura numeroDeFactura(String numeroDeFactura) {
        this.numeroDeFactura = numeroDeFactura;
        return this;
    }

    public void setNumeroDeFactura(String numeroDeFactura) {
        this.numeroDeFactura = numeroDeFactura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Factura fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getTotal() {
        return total;
    }

    public Factura total(Long total) {
        this.total = total;
        return this;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getAbonado() {
        return abonado;
    }

    public Factura abonado(Long abonado) {
        this.abonado = abonado;
        return this;
    }

    public void setAbonado(Long abonado) {
        this.abonado = abonado;
    }

    public EstadoDeFactura getEstadoDeFactura() {
        return estadoDeFactura;
    }

    public Factura estadoDeFactura(EstadoDeFactura estadoDeFactura) {
        this.estadoDeFactura = estadoDeFactura;
        return this;
    }

    public void setEstadoDeFactura(EstadoDeFactura estadoDeFactura) {
        this.estadoDeFactura = estadoDeFactura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Factura cliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Set<Abono> getAbonos() {
        return abonos;
    }

    public Factura abonos(Set<Abono> abonos) {
        this.abonos = abonos;
        return this;
    }

    public Factura addAbono(Abono abono) {
        this.abonos.add(abono);
        abono.setFactura(this);
        return this;
    }

    public Factura removeAbono(Abono abono) {
        this.abonos.remove(abono);
        abono.setFactura(null);
        return this;
    }

    public void setAbonos(Set<Abono> abonos) {
        this.abonos = abonos;
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
        Factura factura = (Factura) o;
        if (factura.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), factura.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Factura{" +
            "id=" + getId() +
            ", numeroDeFactura='" + getNumeroDeFactura() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", total=" + getTotal() +
            ", abonado=" + getAbonado() +
            ", estadoDeFactura='" + getEstadoDeFactura() + "'" +
            "}";
    }
}
