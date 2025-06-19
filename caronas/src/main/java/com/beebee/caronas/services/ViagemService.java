package com.beebee.caronas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beebee.caronas.dto.ViagemDTO;
import com.beebee.caronas.entities.Aluno;
import com.beebee.caronas.entities.Viagem;
import com.beebee.caronas.repositories.AlunoRepository;
import com.beebee.caronas.repositories.ViagemRepository;

public class ViagemService {

    @Autowired
    private ViagemRepository viagemRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    // Conversão de DTO para Entity
    private Viagem toEntity(ViagemDTO dto) {
        Aluno motorista = alunoRepository.findById(dto.getMotoristaId())
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado"));

        return Viagem.builder()
                .id(dto.getId())
                .descricao(dto.getDescricao())
                .dataInicio(dto.getDataInicio())
                .dataFim(dto.getDataFim())
                .origem(dto.getOrigem())
                .destino(dto.getDestino())
                .situacao(dto.getSituacao())
                .motorista(motorista)
                .build();
    }

    // Conversão de Entity para DTO
    private ViagemDTO toDTO(Viagem viagem) {
        ViagemDTO dto = new ViagemDTO();
        dto.setId(viagem.getId());
        dto.setDescricao(viagem.getDescricao());
        dto.setDataInicio(viagem.getDataInicio());
        dto.setDataFim(viagem.getDataFim());
        dto.setOrigem(viagem.getOrigem());
        dto.setDestino(viagem.getDestino());
        dto.setSituacao(viagem.getSituacao());

        if (viagem.getMotorista() != null) {
            dto.setMotoristaId(viagem.getMotorista().getId());
        } else {
            dto.setMotoristaId(null);
        }

        return dto;
    }

    public ViagemDTO salvar(ViagemDTO dto) {
        Viagem viagem = toEntity(dto);
        viagem = viagemRepository.save(viagem);
        return toDTO(viagem);
    }

    public List<ViagemDTO> listarTodos() {
        return viagemRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ViagemDTO buscarPorId(Long id) {
        Viagem viagem = viagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viagem não encontrada"));
        return toDTO(viagem);
    }

    public void deletar(Long id) {
        viagemRepository.deleteById(id);
    }
}
