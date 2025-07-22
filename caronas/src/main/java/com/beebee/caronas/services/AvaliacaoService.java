package com.beebee.caronas.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.beebee.caronas.dto.AvaliacaoDTO;
import com.beebee.caronas.entities.Aluno;
import com.beebee.caronas.entities.Avaliacao;
import com.beebee.caronas.entities.Viagem;
import com.beebee.caronas.entities.ViagemAluno;
import com.beebee.caronas.exceptions.BusinessRuleException;
import com.beebee.caronas.exceptions.ResourceNotFoundException;
import com.beebee.caronas.repositories.AlunoRepository;
import com.beebee.caronas.repositories.AvaliacaoRepository;
import com.beebee.caronas.repositories.ViagemAlunoRepository;
import com.beebee.caronas.repositories.ViagemRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {
    private final AvaliacaoRepository avaliacaoRepository;
    private final ViagemAlunoRepository viagemAlunoRepository;
    private final ViagemRepository viagemRepository;
    private final AlunoRepository alunoRepository;

    private AvaliacaoDTO toDTO(Avaliacao avaliacao) {
        return AvaliacaoDTO.builder()
            .id(avaliacao.getId())
            .descricao(avaliacao.getDescricao())
            .data(avaliacao.getData())
            .comentarioMotorista(avaliacao.getComentarioMotorista())
            .notaMotorista(avaliacao.getNotaMotorista())
            .comentarioCaronista(avaliacao.getComentarioCaronista())
            .notaCaronista(avaliacao.getNotaCaronista())
            .viagemAlunoId(avaliacao.getViagemAluno().getId())
            .build();
    }

    private Avaliacao toEntity(AvaliacaoDTO dto) {
        ViagemAluno studentTrip = viagemAlunoRepository.findById(dto.getViagemAlunoId())
            .orElseThrow(() -> new ResourceNotFoundException("ViagemAluno", dto.getViagemAlunoId()));

        if (dto.getNotaMotorista() < 1 || dto.getNotaMotorista() > 5) {
            throw new BusinessRuleException("A nota deve ser entre 1 e 5");
        }

        return Avaliacao.builder()
            .id(dto.getId())
            .descricao(dto.getDescricao())
            .data(dto.getData())
            .comentarioMotorista(dto.getComentarioMotorista())
            .notaMotorista(dto.getNotaMotorista())
            .comentarioCaronista(dto.getComentarioCaronista())
            .notaCaronista(dto.getNotaCaronista())
            .viagemAluno(studentTrip)
            .build();
    }

    @Transactional
    public AvaliacaoDTO save(AvaliacaoDTO dto) {
        Avaliacao avaliacao = avaliacaoRepository.findByViagemAlunoId(dto.getViagemAlunoId())
            .orElseGet(() -> {
                ViagemAluno viagemAluno = viagemAlunoRepository.findById(dto.getViagemAlunoId())
                    .orElseThrow(() -> new ResourceNotFoundException("ViagemAluno", dto.getViagemAlunoId()));
                return Avaliacao.builder().viagemAluno(viagemAluno).data(LocalDate.now()).build();
            });

        if (dto.getNotaMotorista() != null) {
            if (avaliacao.getNotaMotorista() != null) {
                throw new BusinessRuleException("O motorista já foi avaliado para esta carona.");
            }
            avaliacao.setNotaMotorista(dto.getNotaMotorista());
            avaliacao.setComentarioMotorista(dto.getComentarioMotorista());
        }

        if (dto.getNotaCaronista() != null) {
            if (avaliacao.getNotaCaronista() != null) {
                throw new BusinessRuleException("O caronista já foi avaliado para esta carona.");
            }
            avaliacao.setNotaCaronista(dto.getNotaCaronista());
            avaliacao.setComentarioCaronista(dto.getComentarioCaronista());
        }
        
        if (dto.getNotaMotorista() == null && dto.getNotaCaronista() == null) {
            throw new BusinessRuleException("É necessário fornecer uma nota.");
        }

        Avaliacao avaliacaoSalva = avaliacaoRepository.save(avaliacao);

        if (dto.getNotaMotorista() != null) {
            recalcularMediaMotorista(avaliacaoSalva.getViagemAluno().getViagem().getMotorista());
        }
        if (dto.getNotaCaronista() != null) {
            recalcularMediaCaronista(avaliacaoSalva.getViagemAluno().getAluno());
        }

        return toDTO(avaliacaoSalva);
    }

    private void recalcularMediaMotorista(Aluno motorista) {
        List<Viagem> viagens = viagemRepository.findByMotoristaId(motorista.getId());
        List<ViagemAluno> participacoes = viagemAlunoRepository.findByViagemIn(viagens);
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByViagemAlunoIn(participacoes);

        double media = avaliacoes.stream()
            .map(Avaliacao::getNotaMotorista)
            .filter(Objects::nonNull)
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0.0);

        motorista.setMediaMotorista(media);
        alunoRepository.save(motorista);
    }

    private void recalcularMediaCaronista(Aluno caronista) {
        List<ViagemAluno> participacoes = viagemAlunoRepository.findByAlunoId(caronista.getId());
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByViagemAlunoIn(participacoes);
        
        double media = avaliacoes.stream()
            .map(Avaliacao::getNotaCaronista)
            .filter(Objects::nonNull)
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0.0);
            
        caronista.setMediaCaronista(media);
        alunoRepository.save(caronista);
    }
    
    public List<AvaliacaoDTO> getAll() {
        return avaliacaoRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public AvaliacaoDTO getById(Long id) {
        Avaliacao rating = avaliacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Avaliação", id));
        return toDTO(rating);
    }
    public AvaliacaoDTO update(Long id, AvaliacaoDTO dto) {
        Avaliacao rating = avaliacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Avaliação", id));
    
        Avaliacao updatedRating = avaliacaoRepository.save(rating);
        return toDTO(updatedRating);
    }

    public void delete(Long id) {
        if (!avaliacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Avaliação", id);
        }
        avaliacaoRepository.deleteById(id);
    }

    public AvaliacaoDTO getByViagemAlunoId(Long viagemAlunoId) {
        return avaliacaoRepository.findByViagemAlunoId(viagemAlunoId)
            .map(this::toDTO) 
            .orElse(null);  
    }
}
