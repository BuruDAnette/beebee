package com.beebee.caronas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beebee.caronas.entities.Viagem;

@Repository
public interface ViagemRepository extends JpaRepository<Viagem, Long>{
    
}
