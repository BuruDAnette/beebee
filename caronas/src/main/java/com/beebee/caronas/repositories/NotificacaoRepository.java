package com.beebee.caronas.repositories;

import com.beebee.caronas.entities.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    
    List<Notificacao> findByDestinatarioIdOrderByDataDesc(Long destinatarioId);
    
    long countByDestinatarioIdAndLidaIsFalse(Long destinatarioId);
}