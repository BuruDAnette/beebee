package com.beebee.caronas.repositories;

import com.beebee.caronas.entities.Avaliacao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long>{
    Optional<Avaliacao> findByViagemAlunoId(Long viagemAlunoId);
}
