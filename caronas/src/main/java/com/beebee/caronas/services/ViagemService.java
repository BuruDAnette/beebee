package com.beebee.caronas.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beebee.caronas.dto.ViagemDTO;
import com.beebee.caronas.entities.Aluno;
import com.beebee.caronas.entities.Viagem;
import com.beebee.caronas.entities.ViagemAluno;
import com.beebee.caronas.exceptions.BusinessRuleException;
import com.beebee.caronas.exceptions.ResourceNotFoundException;
import com.beebee.caronas.repositories.AlunoRepository;
import com.beebee.caronas.repositories.VeiculoRepository;
import com.beebee.caronas.repositories.ViagemAlunoRepository;
import com.beebee.caronas.repositories.ViagemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViagemService {
    private final ViagemRepository viagemRepository;
    private final AlunoRepository alunoRepository;
    private final VeiculoRepository veiculoRepository; 
    private final ViagemAlunoRepository viagemAlunoRepository;
    
    private ViagemDTO toDTO(Viagem viagem) {
        return ViagemDTO.builder()
            .id(viagem.getId())
            .descricao(viagem.getDescricao())
            .dataInicio(viagem.getDataInicio())
            .dataFim(viagem.getDataFim())
            .origem(viagem.getOrigem())
            .destino(viagem.getDestino())
            .situacao(viagem.getSituacao())
            .motoristaId(viagem.getMotorista().getId())
            .motoristaNome(viagem.getMotorista().getNome())
            .build();
    }

    private Viagem toEntity(ViagemDTO dto) {
        Aluno driver = alunoRepository.findById(dto.getMotoristaId())
            .orElseThrow(() -> new ResourceNotFoundException("Motorista", dto.getMotoristaId()));
        
        return Viagem.builder()
            .id(dto.getId())
            .descricao(dto.getDescricao())
            .dataInicio(dto.getDataInicio())
            .dataFim(dto.getDataFim())
            .origem(dto.getOrigem())
            .destino(dto.getDestino())
            .situacao(dto.getSituacao())
            .motorista(driver)
            .build();
    }

    public ViagemDTO save(ViagemDTO dto) {
        boolean motoristaTemVeiculo = !veiculoRepository.findByMotoristaId(dto.getMotoristaId()).isEmpty();
        if (!motoristaTemVeiculo) {
            throw new BusinessRuleException("Para criar uma viagem, é necessário ter pelo menos um veículo cadastrado.");
        }
        
        if (dto.getDataInicio().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("A data de início da viagem não pode ser no passado.");
        }

        Viagem savedTrip = toEntity(dto);
        savedTrip = viagemRepository.save(savedTrip);
        return toDTO(savedTrip);
    }

    public List<ViagemDTO> getAll() {
        return viagemRepository.findAll()
            .stream()
            .filter(viagem -> "PLANEJADA".equals(viagem.getSituacao()) || "EM_ANDAMENTO".equals(viagem.getSituacao()))
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    public List<ViagemDTO> getByMotoristaId(Long motoristaId) {
        return viagemRepository.findByMotoristaId(motoristaId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    public ViagemDTO getById(Long id) {
        Viagem trip = viagemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Viagem", id));
        return toDTO(trip);
    }

    @Transactional
    public ViagemDTO update(Long id, ViagemDTO dto) {
        Viagem viagemExistente = viagemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Viagem", id));

        String statusAntigo = viagemExistente.getSituacao();
        String statusNovo = dto.getSituacao();

        if ("CANCELADA".equals(statusNovo) && !"CANCELADA".equals(statusAntigo)) {
            List<ViagemAluno> solicitacoes = viagemAlunoRepository.findByViagemId(id);
            for (ViagemAluno solicitacao : solicitacoes) {
                solicitacao.setSituacao(ViagemAluno.Situacao.CANCELADA);
            }
            viagemAlunoRepository.saveAll(solicitacoes);
        }

        if ("FINALIZADA".equals(statusNovo) && !"FINALIZADA".equals(statusAntigo)) {
            viagemExistente.setDataFim(LocalDateTime.now());
            List<ViagemAluno> solicitacoes = viagemAlunoRepository.findByViagemId(id);
            for (ViagemAluno solicitacao : solicitacoes) {
                if (solicitacao.getSituacao() == ViagemAluno.Situacao.CONFIRMADA || solicitacao.getSituacao() == ViagemAluno.Situacao.PENDENTE) {
                    solicitacao.setSituacao(ViagemAluno.Situacao.FINALIZADA);
                }
            }
            viagemAlunoRepository.saveAll(solicitacoes);
        }
        
        if (dto.getDescricao() != null) viagemExistente.setDescricao(dto.getDescricao());
        if (statusNovo != null) viagemExistente.setSituacao(statusNovo);

        Viagem viagemAtualizada = viagemRepository.save(viagemExistente);
        return toDTO(viagemAtualizada);
    }

    public void delete(Long id) {
        if (!viagemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Viagem", id);
        }
        viagemRepository.deleteById(id);
    }

    public List<ViagemDTO> getHistoricoByMotoristaId(Long motoristaId) {
        List<String> situacoesHistorico = List.of("FINALIZADA", "CANCELADA");
        
        return viagemRepository.findByMotoristaIdAndSituacaoIn(motoristaId, situacoesHistorico)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}