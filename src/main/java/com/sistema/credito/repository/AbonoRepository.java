package com.sistema.credito.repository;

import com.sistema.credito.domain.Abono;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Abono entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbonoRepository extends JpaRepository<Abono, Long>, JpaSpecificationExecutor<Abono> {

}
