package com.example.demo.Services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Dtos.Requests.ReceitaRequestDTO;
import com.example.demo.Dtos.Responses.ReceitaResponseDTO;
import com.example.demo.Dtos.Updater.BaixarReceitaDTO;
import com.example.demo.Models.Cliente;
import com.example.demo.Models.Conta;
import com.example.demo.Models.FmPagamento;
import com.example.demo.Models.Receita;
import com.example.demo.Repositories.ClienteRepository;
import com.example.demo.Repositories.ContaRepository;
import com.example.demo.Repositories.FmPagamentoRepository;
import com.example.demo.Repositories.ReceitaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final ClienteRepository clienteRepository;
    private final ContaRepository contaRepository;
    private final FmPagamentoRepository fmPagamentoRepository;

    @Transactional
    public Receita cadastrarReceita(ReceitaRequestDTO dto) {

        Cliente cliente = buscarClientePorId(dto.getClienteId());
        FmPagamento fmPagamento = buscarFmPagamentoPorId(dto.getFmPagamentoId());

        Receita novaReceita = new Receita();
        novaReceita.setCliente(cliente);
        novaReceita.setDescricao(dto.getDescricao());
        novaReceita.setVlReceita(dto.getVlReceita());
        novaReceita.setFmPagamento(fmPagamento);

        return receitaRepository.save(novaReceita);

    }

    @Transactional
    public Receita realizarBaixaDaReceita(BaixarReceitaDTO dto) {
        Receita receitaBaixada = buscarReceitaPorId(dto.getReceitaId());
        Conta conta = buscarContaPorId(dto.getContaId());

        if (Boolean.TRUE.equals(receitaBaixada.getStBaixado())) {
            throw new RuntimeException("Receita ja está baixada !");
        }

        receitaBaixada.setStBaixado(true);
        receitaBaixada.setContaBaixada(conta);

        return receitaRepository.save(receitaBaixada);

    }

    @Transactional(readOnly = true)
    public List<ReceitaResponseDTO> listarTodasReceitas() {
        List<Receita> receitas = receitaRepository.findAll();
        return receitas.stream()
                .map(this::mapParaDTO)
                .toList();
    }

    public ReceitaResponseDTO converterParaDTO(Receita receita) {
        return mapParaDTO(receita);
    }

    // -----------METODOS PRIVADOS -------------------

    private Receita buscarReceitaPorId(Long id) {
        return receitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receita com ID " + id + " não encontrada!"));
    }

    private Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente com ID " + id + " não encontrado!"));
    }

    private Conta buscarContaPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("conta com ID " + id + " não encontrado!"));
    }

    private FmPagamento buscarFmPagamentoPorId(Long id) {
        return fmPagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forma de pagamento com ID " + id + " não encontrado!"));

    }

    private ReceitaResponseDTO mapParaDTO(Receita receita) {
        ReceitaResponseDTO dto = new ReceitaResponseDTO();

        dto.setId(receita.getId());
        dto.setVlReceita(receita.getVlReceita());
        dto.setStBaixado(receita.getStBaixado());
        dto.setClienteId(receita.getCliente() != null ? receita.getCliente().getId() : null);
        dto.setNomeCliente(receita.getCliente() != null ? receita.getCliente().getNome() : null);
        dto.setFmPagamento(receita.getFmPagamento() != null ? receita.getFmPagamento().getNome() : null);
        dto.setNmConta(receita.getContaBaixada() != null ? receita.getContaBaixada().getDescricao() : null);

        return dto;
    }

}
