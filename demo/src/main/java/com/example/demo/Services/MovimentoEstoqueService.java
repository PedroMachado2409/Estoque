package com.example.demo.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Dtos.Responses.ProdutoMovimentacaoResponseDTO;
import com.example.demo.Enums.TipoMovimentacao;
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

    public void registrarSaida(Long produtoId, Integer quantidade, String observacao, UUID codigo) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + produtoId));

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
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

    public List<ProdutoMovimentacaoResponseDTO> movimentacaoPorProduto(Long produtoId ){
        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findByProdutoId(produtoId);

        return movimentacoes.stream().map(mov ->{
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
}
