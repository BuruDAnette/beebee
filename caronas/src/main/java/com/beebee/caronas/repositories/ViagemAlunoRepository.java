package com.beebee.caronas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beebee.caronas.entities.ViagemAluno;

@Repository
public interface ViagemAlunoRepository extends JpaRepository<ViagemAluno, Long> {
    
}