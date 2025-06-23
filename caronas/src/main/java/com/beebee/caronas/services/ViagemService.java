package com.beebee.caronas.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.beebee.caronas.dto.ViagemDTO;
import com.beebee.caronas.entities.Aluno;
import com.beebee.caronas.entities.Viagem;
import com.beebee.caronas.exceptions.BusinessRuleException;
import com.beebee.caronas.exceptions.ResourceNotFoundException;
import com.beebee.caronas.repositories.AlunoRepository;
import com.beebee.caronas.repositories.ViagemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViagemService {
    private final ViagemRepository viagemRepository;
    private final AlunoRepository alunoRepository;

    private ViagemDTO converterParaDTO(Viagem viagem) {
        return ViagemDTO.builder()
            .id(viagem.getId())
            .descricao(viagem.getDescricao())
            .dataInicio(viagem.getDataInicio())
            .dataFim(viagem.getDataFim())
            .origem(viagem.getOrigem())
            .destino(viagem.getDestino())
            .situacao(viagem.getSituacao())
            .motoristaId(viagem.getMotorista().getId())
            .build();
    }

    private Viagem converterParaEntidade(ViagemDTO dto) {
        Aluno motorista = alunoRepository.findById(dto.getMotoristaId())
            .orElseThrow(() -> new ResourceNotFoundException("Motorista", dto.getMotoristaId()));

        if (dto.getDataInicio().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("Data de início não pode ser no passado");
        }
        
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

    public ViagemDTO salvar(ViagemDTO dto) {
        Viagem viagem = converterParaEntidade(dto);
        viagem = viagemRepository.save(viagem);
        return converterParaDTO(viagem);
    }
    public List<ViagemDTO> listarTodos() {
        return viagemRepository.findAll()
            .stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }
    public ViagemDTO buscarPorId(Long id) {
        Viagem viagem = viagemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Viagem", id));
        return converterParaDTO(viagem);
    }
    public void excluir(Long id) {
        if (!viagemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Viagem", id);
        }
        viagemRepository.deleteById(id);
    }
}
