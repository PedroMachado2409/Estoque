package com.example.demo.Services;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Dtos.Responses.ProdutoClienteQuantidadeDTO;
import com.example.demo.Dtos.Responses.ProdutoMovimentacaoResponseDTO;
import com.example.demo.Enums.TipoMovimentacao;
import com.example.demo.Models.Cliente;
import com.example.demo.Models.MovimentacaoEstoque;
import com.example.demo.Models.Produto;
import com.example.demo.Repositories.MovimentacaoEstoqueRepository;
import com.example.demo.Repositories.ProdutoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MovimentoEstoqueService {

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public void registrarSaida(Long produtoId, Integer quantidade, String observacao, UUID codigo, Cliente cliente) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + produtoId));

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
        movimentacao.setCliente(cliente);
        movimentacao.setData(LocalDate.now());
        movimentacao.setQuantidade(-Math.abs(quantidade));
        movimentacao.setTipo(TipoMovimentacao.SAIDA);
        movimentacao.setObservacao(observacao);
        movimentacao.setCodigo(codigo);

        movimentacaoEstoqueRepository.save(movimentacao);
    }

    public void registrarEntrada(Long produtoId, Integer quantidade, String observacao, UUID codigo) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + produtoId));

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
        movimentacao.setData(LocalDate.now());
        movimentacao.setQuantidade(+Math.abs(quantidade));
        movimentacao.setTipo(TipoMovimentacao.ENTRADA);
        movimentacao.setObservacao(observacao);
        movimentacao.setCodigo(codigo);

        movimentacaoEstoqueRepository.save(movimentacao);
    }

    public int calcularSaldoProduto(Long produtoId) {
        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findByProdutoId(produtoId);

        int saldo = 0;

        for (MovimentacaoEstoque movimentacao : movimentacoes) {
            if (movimentacao.getTipo() == TipoMovimentacao.ENTRADA) {
                saldo += movimentacao.getQuantidade();
            } else if (movimentacao.getTipo() == TipoMovimentacao.SAIDA) {
                saldo -= movimentacao.getQuantidade();
            }
        }

        return saldo;
    }

    public List<ProdutoMovimentacaoResponseDTO> movimentacaoPorProduto(Long produtoId) {
        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findByProdutoId(produtoId);

        return movimentacoes.stream().map(mov -> {
            ProdutoMovimentacaoResponseDTO dto = new ProdutoMovimentacaoResponseDTO();
            dto.setProdutoId(produtoId);
            dto.setNomeProduto(mov.getProduto().getNome());
            dto.setDtMovimentacao(mov.getData());
            dto.setQuantidade(mov.getQuantidade());
            dto.setTipo(mov.getTipo());
            dto.setObservacao(mov.getObservacao());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<ProdutoClienteQuantidadeDTO> movimentacaoPorProdutoAgrupadoPorCliente(Long produtoId) {
        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findByProdutoIdAndTipo(produtoId,
                TipoMovimentacao.SAIDA);

        Map<Long, ProdutoClienteQuantidadeDTO> agrupado = new HashMap<>();

        for (MovimentacaoEstoque mov : movimentacoes) {
            if (mov.getCliente() == null)
                continue;

            Long clienteId = mov.getCliente().getId();

            agrupado.compute(clienteId, (id, dto) -> {
                if (dto == null) {
                    ProdutoClienteQuantidadeDTO novo = new ProdutoClienteQuantidadeDTO();
                    novo.setClienteId(clienteId);
                    novo.setNomeCliente(mov.getCliente().getNome());
                    novo.setProdutoId(produtoId);
                    novo.setNomeProduto(mov.getProduto().getNome());
                    novo.setQuantidadeTotal(Math.abs(mov.getQuantidade()));
                    return novo;
                } else {
                    dto.setQuantidadeTotal(dto.getQuantidadeTotal() + Math.abs(mov.getQuantidade()));
                    return dto;
                }
            });
        }

        return agrupado.values().stream()
                .sorted(Comparator.comparingInt(ProdutoClienteQuantidadeDTO::getQuantidadeTotal).reversed())
                .collect(Collectors.toList());
    }
}
