package com.beebee.caronas.repositories;

import com.beebee.caronas.entities.HorarioAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioAcademicoRepository extends JpaRepository<HorarioAcademico, Long> {
    List<HorarioAcademico> findByAlunoId(Long idAluno);
}