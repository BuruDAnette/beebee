package com.beebee.caronas.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
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
import com.beebee.caronas.entities.Veiculo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViagemService {
    private final ViagemRepository viagemRepository;
    private final AlunoRepository alunoRepository;
    private final VeiculoRepository veiculoRepository; 
    private final ViagemAlunoRepository viagemAlunoRepository;
    private final NotificacaoService notificacaoService;
    
    // O método toDTO permanece o mesmo
    private ViagemDTO toDTO(Viagem viagem) {
        ViagemDTO.ViagemDTOBuilder builder = ViagemDTO.builder()
            .id(viagem.getId())
            .descricao(viagem.getDescricao())
            .dataInicio(viagem.getDataInicio())
            .dataFim(viagem.getDataFim())
            .origem(viagem.getOrigem())
            .destino(viagem.getDestino())
            .situacao(viagem.getSituacao())
            .motoristaId(viagem.getMotorista().getId())
            .motoristaNome(viagem.getMotorista().getNome())
            .mediaMotorista(viagem.getMotorista().getMediaMotorista());
        
        if (viagem.getVeiculo() != null) {
            builder.veiculoId(viagem.getVeiculo().getId())
                .veiculoModelo(viagem.getVeiculo().getModelo())
                .veiculoPlaca(viagem.getVeiculo().getPlaca())
                .veiculoCor(viagem.getVeiculo().getCor());
        }
        
        return builder.build();
    }

    // O método toEntity permanece o mesmo
    private Viagem toEntity(ViagemDTO dto) {
        Aluno driver = alunoRepository.findById(dto.getMotoristaId())
            .orElseThrow(() -> new ResourceNotFoundException("Motorista", dto.getMotoristaId()));
        
        Veiculo veiculo = null;
        if (dto.getVeiculoId() != null) {
            veiculo = veiculoRepository.findById(dto.getVeiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo", dto.getVeiculoId()));
        }
        
        return Viagem.builder()
            .id(dto.getId())
            .descricao(dto.getDescricao())
            .dataInicio(dto.getDataInicio())
            .dataFim(dto.getDataFim())
            .origem(dto.getOrigem())
            .destino(dto.getDestino())
            .situacao(dto.getSituacao())
            .motorista(driver)
            .veiculo(veiculo)
            .build();
    }

    // O método save permanece o mesmo
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

    // Os métodos getAll, getByMotoristaId e getById permanecem os mesmos
    public List<ViagemDTO> getAll(String origem, String destino, LocalDate data) {
        Specification<Viagem> spec = (root, query, criteriaBuilder) -> {
            return root.get("situacao").in("PLANEJADA", "EM_ANDAMENTO");
        };

        if (origem != null && !origem.trim().isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(criteriaBuilder.lower(root.get("origem")), "%" + origem.toLowerCase() + "%")
            );
        }

        if (destino != null && !destino.trim().isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(criteriaBuilder.lower(root.get("destino")), "%" + destino.toLowerCase() + "%")
            );
        }

        if (data != null) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                LocalDateTime inicioDoDia = data.atStartOfDay();
                LocalDateTime fimDoDia = data.plusDays(1).atStartOfDay().minusNanos(1);
                return criteriaBuilder.between(root.get("dataInicio"), inicioDoDia, fimDoDia);
            });
        }

        return viagemRepository.findAll(spec)
            .stream()
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
    
    // MÉTODO UPDATE AJUSTADO
    @Transactional
    public ViagemDTO update(Long id, ViagemDTO dto) {
        Viagem viagemExistente = viagemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Viagem", id));

        if (!"PLANEJADA".equals(viagemExistente.getSituacao())) {
            throw new BusinessRuleException("Só é possível editar os detalhes de viagens que estão no estado 'PLANEJADA'.");
        }

        if (!viagemAlunoRepository.findByViagemId(id).isEmpty()) {
            throw new BusinessRuleException("Não é possível editar uma viagem que já possui solicitações de passageiros.");
        }
        
        viagemExistente.setOrigem(dto.getOrigem());
        viagemExistente.setDestino(dto.getDestino());
        viagemExistente.setDataInicio(dto.getDataInicio());
        viagemExistente.setDescricao(dto.getDescricao());
        if (dto.getVeiculoId() != null) {
            Veiculo veiculo = veiculoRepository.findById(dto.getVeiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo", dto.getVeiculoId()));
            viagemExistente.setVeiculo(veiculo);
        }

        Viagem viagemAtualizada = viagemRepository.save(viagemExistente);
        return toDTO(viagemAtualizada);
    }

    // NOVO MÉTODO
    @Transactional
    public ViagemDTO iniciarViagem(Long id) {
        Viagem viagem = viagemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Viagem", id));

        if (!"PLANEJADA".equals(viagem.getSituacao())) {
            throw new BusinessRuleException("Só é possível iniciar viagens que estão no estado 'PLANEJADA'.");
        }
        viagem.setSituacao("EM_ANDAMENTO");
        return toDTO(viagemRepository.save(viagem));
    }

    // NOVO MÉTODO
    @Transactional
    public ViagemDTO encerrarViagem(Long id) {
        Viagem viagem = viagemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Viagem", id));

        if (!"EM_ANDAMENTO".equals(viagem.getSituacao())) {
            throw new BusinessRuleException("Só é possível encerrar viagens que estão 'EM ANDAMENTO'.");
        }
        viagem.setSituacao("FINALIZADA");
        viagem.setDataFim(LocalDateTime.now());
        return toDTO(viagemRepository.save(viagem));
    }

    // NOVO MÉTODO
    @Transactional
    public ViagemDTO cancelarViagem(Long id) {
        Viagem viagem = viagemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Viagem", id));
        
        if ("FINALIZADA".equals(viagem.getSituacao()) || "CANCELADA".equals(viagem.getSituacao())) {
            throw new BusinessRuleException("Esta viagem não pode mais ser cancelada.");
        }

        viagem.setSituacao("CANCELADA");
        // Notificar passageiros
        List<ViagemAluno> solicitacoes = viagemAlunoRepository.findByViagemId(id);
        for (ViagemAluno solicitacao : solicitacoes) {
            solicitacao.setSituacao(ViagemAluno.Situacao.CANCELADA);
            // Criar notificação
            String mensagem = "A viagem de " + viagem.getOrigem() + " para " + viagem.getDestino() + " foi cancelada pelo motorista.";
            notificacaoService.criarNotificacao(solicitacao.getAluno(), mensagem, "/app/minhas-viagens");
        }
        viagemAlunoRepository.saveAll(solicitacoes);
        
        return toDTO(viagemRepository.save(viagem));
    }
    
    // O método delete e getHistoricoByMotoristaId permanecem os mesmos
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