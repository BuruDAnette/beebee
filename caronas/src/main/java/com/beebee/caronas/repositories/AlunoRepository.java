package com.beebee.caronas.repositories;

import com.beebee.caronas.entities.Aluno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByCpf(String cpf);
    Optional<Aluno> findByLogin(String login);
    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<Aluno> findByLoginAndSenha(String login, String senha);
}
