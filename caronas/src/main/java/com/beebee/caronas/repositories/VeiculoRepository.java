package com.beebee.caronas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beebee.caronas.entities.Veiculo;
import java.util.List;


@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    List<Veiculo> findByMotoristaId(Long motoristaId);
}
