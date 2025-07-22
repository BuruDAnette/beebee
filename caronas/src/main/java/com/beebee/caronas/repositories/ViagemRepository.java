package com.beebee.caronas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.beebee.caronas.entities.Viagem;

@Repository
public interface ViagemRepository extends JpaRepository<Viagem, Long>, JpaSpecificationExecutor<Viagem>{
    List<Viagem> findByMotoristaId(Long motoristaId);

    List<Viagem> findByMotoristaIdAndSituacaoIn(Long motoristaId, List<String> situacoes);
}
