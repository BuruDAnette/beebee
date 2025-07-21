package com.beebee.caronas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.beebee.caronas.entities.Viagem;

@Repository
public interface ViagemRepository extends JpaRepository<Viagem, Long>{
    @Query(value = "SELECT * FROM tb_viagem WHERE id_aluno = :motoristaId", nativeQuery = true)
    List<Viagem> findByMotoristaId(Long motoristaId);
}
