package com.beebee.caronas.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beebee.caronas.dto.ViagemAlunoDTO;
import com.beebee.caronas.dto.ViagemDTO;
import com.beebee.caronas.entities.Aluno;
import com.beebee.caronas.entities.Viagem;
import com.beebee.caronas.entities.ViagemAluno;
import com.beebee.caronas.entities.ViagemAluno.Situacao;
import com.beebee.caronas.exceptions.BusinessRuleException;
import com.beebee.caronas.exceptions.ResourceNotFoundException;
import com.beebee.caronas.repositories.AlunoRepository;
import com.beebee.caronas.repositories.ViagemRepository;
import com.beebee.caronas.repositories.ViagemAlunoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViagemAlunoService {
    private final ViagemAlunoRepository viagemAlunoRepository;
    private final AlunoRepository alunoRepository;
    private final ViagemRepository viagemRepository;
    private final NotificacaoService notificacaoService;

    private ViagemAlunoDTO toDTO(ViagemAluno viagemAluno) {
        return ViagemAlunoDTO.builder()
            .id(viagemAluno.getId())
            .dataSolicitacao(viagemAluno.getDataSolicitacao())
            .dataConfirmacao(viagemAluno.getDataConfirmacao())
            .observacao(viagemAluno.getObservacao())
            .situacao(viagemAluno.getSituacao())
            .alunoId(viagemAluno.getAluno().getId())
            .alunoNome(viagemAluno.getAluno().getNome())
            .viagem(toViagemDTO(viagemAluno.getViagem()))
            .mediaCaronista(viagemAluno.getAluno().getMediaCaronista())
            .build();
    }

    private ViagemAluno toEntity(ViagemAlunoDTO dto) {
        Aluno student = alunoRepository.findById(dto.getAlunoId())
            .orElseThrow(() -> new ResourceNotFoundException("Aluno", dto.getAlunoId()));

        Viagem trip = viagemRepository.findById(dto.getViagem().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Viagem", dto.getViagem().getId()));

        return ViagemAluno.builder()
            .id(dto.getId())
            .dataSolicitacao(LocalDateTime.now()) 
            .observacao(dto.getObservacao())
            .situacao(dto.getSituacao())
            .aluno(student)
            .viagem(trip)
            .build();
    }

    private ViagemDTO toViagemDTO(Viagem viagem) {
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

    @Transactional
    public ViagemAlunoDTO save(ViagemAlunoDTO dto) {
        if (viagemAlunoRepository.existsByAlunoIdAndViagemId(dto.getAlunoId(), dto.getViagem().getId())) {
            throw new BusinessRuleException("Você já solicitou participação nesta viagem.");
        }
        ViagemAluno savedStudentTrip = toEntity(dto);
        savedStudentTrip = viagemAlunoRepository.save(savedStudentTrip);

        Aluno motorista = savedStudentTrip.getViagem().getMotorista();
        String mensagem = savedStudentTrip.getAluno().getNome() + " solicitou participação na sua carona.";
        String link = "/app/viagens/" + savedStudentTrip.getViagem().getId();
        notificacaoService.criarNotificacao(motorista, mensagem, link);

        return toDTO(savedStudentTrip);
    }
    public List<ViagemAlunoDTO> getAll() {
        return viagemAlunoRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    public ViagemAlunoDTO getById(Long id) {
        ViagemAluno studentTrip = viagemAlunoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ViagemAluno", id));
        return toDTO(studentTrip);
    }

    @Transactional
    public ViagemAlunoDTO update(Long id, ViagemAlunoDTO dto) {
        ViagemAluno studentTrip = viagemAlunoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ViagemAluno", id));

        Situacao statusAntigo = studentTrip.getSituacao();
        Situacao statusNovo = dto.getSituacao();

        if (dto.getObservacao() != null) {
            studentTrip.setObservacao(dto.getObservacao());
        }

        if (statusNovo != null) {
            studentTrip.setSituacao(statusNovo);
            if (statusNovo == Situacao.CONFIRMADA) {
                studentTrip.setDataConfirmacao(LocalDateTime.now());
            }
        }

        ViagemAluno updatedStudentTrip = viagemAlunoRepository.save(studentTrip);

        if (statusNovo != null && statusNovo != statusAntigo) {
            Aluno passageiro = updatedStudentTrip.getAluno();
            String link = "/app/viagens/" + updatedStudentTrip.getViagem().getId();
            String mensagem = "";

            if (statusNovo == Situacao.CONFIRMADA) {
                mensagem = "Seu pedido de carona para " + updatedStudentTrip.getViagem().getDestino() + " foi aceite!";
                notificacaoService.criarNotificacao(passageiro, mensagem, link);
            } else if (statusNovo == Situacao.RECUSADA) {
                mensagem = "Seu pedido de carona para " + updatedStudentTrip.getViagem().getDestino() + " foi recusado.";
                notificacaoService.criarNotificacao(passageiro, mensagem, link);
            }
        }

        return toDTO(updatedStudentTrip);
    }

    public void delete(Long id) {
        if (!viagemAlunoRepository.existsById(id)) {
            throw new ResourceNotFoundException("ViagemAluno", id);
        }
        viagemAlunoRepository.deleteById(id);
    }

    /**
     * @param alunoId 
     * @return 
     */
    public List<ViagemAlunoDTO> findByAlunoId(Long alunoId) {
        List<ViagemAluno> viagensDoAluno = viagemAlunoRepository.findByAlunoId(alunoId);

        return viagensDoAluno.stream()
                .map(ViagemAlunoDTO::new) 
                .collect(Collectors.toList()); 
    }

    /**
     * @param viagemId 
     * @return 
     */
    public List<ViagemAlunoDTO> findByViagemId(Long viagemId) {
        List<ViagemAluno> pedidosDaViagem = viagemAlunoRepository.findByViagemId(viagemId);

        return pedidosDaViagem.stream()
                .map(ViagemAlunoDTO::new) 
                .collect(Collectors.toList()); 
    }

}