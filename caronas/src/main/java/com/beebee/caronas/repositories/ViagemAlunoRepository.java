package com.beebee.caronas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beebee.caronas.entities.ViagemAluno;
import com.beebee.caronas.entities.Viagem;

@Repository
public interface ViagemAlunoRepository extends JpaRepository<ViagemAluno, Long> {
    List<ViagemAluno> findByViagemId(Long viagemId);

    boolean existsByAlunoIdAndViagemId(Long alunoId, Long viagemId);

    List<ViagemAluno> findByAlunoId(Long alunoId);
    List<ViagemAluno> findByViagemIn(List<Viagem> viagens);
    
}