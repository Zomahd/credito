package com.sistema.credito.repository;

import com.sistema.credito.domain.HistorialDeCredito;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the HistorialDeCredito entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistorialDeCreditoRepository extends JpaRepository<HistorialDeCredito, Long> {

}
