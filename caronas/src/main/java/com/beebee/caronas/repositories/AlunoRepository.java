package com.beebee.caronas.repositories;

import com.beebee.caronas.entities.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByCpf(String cpf);
    Optional<Aluno> findByLogin(String login);
}
