package com.beebee.caronas.repositories;

import com.beebee.caronas.entities.HorarioAcademico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioAcademicoRepository extends JpaRepository<HorarioAcademico, Long> {
    List<HorarioAcademico> findByAlunoId(Long idAluno);
}