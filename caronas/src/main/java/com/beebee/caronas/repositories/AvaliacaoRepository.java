package com.beebee.caronas.repositories;

import com.beebee.caronas.entities.Avaliacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long>{
    
}
