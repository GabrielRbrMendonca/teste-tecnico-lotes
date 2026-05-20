package com.empresa.lotes.repository;

import com.empresa.lotes.model.Lote;
import com.empresa.lotes.model.StatusLote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoteRepository extends JpaRepository<Lote, Long> {

    Page<Lote> findByStatus(StatusLote status, Pageable pageable);

    Page<Lote> findByOperador(String operador, Pageable pageable);

    Page<Lote> findByStatusAndOperador(StatusLote status, String operador, Pageable pageable);
}
