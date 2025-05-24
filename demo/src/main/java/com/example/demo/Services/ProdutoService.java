package com.example.demo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Enums.TipoMovimentacao;
import com.example.demo.Models.MovimentacaoEstoque;
import com.example.demo.Models.Produto;
import com.example.demo.Repositories.MovimentacaoEstoqueRepository;
import com.example.demo.Repositories.ProdutoRepository;

import jakarta.transaction.Transactional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public List<Produto> listarProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        if (produtos.isEmpty()) {
            throw new RuntimeException("Não foi encontrado nenhum produto cadastrado");
        }
        return produtos;
    }

    public Produto obterProdutoPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto com o ID: " + id + " Não foi encontrado"));
    }

    public Produto cadastrarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto editarProduto(Long id, Produto produto) {
        Produto produtoEditado = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto com o ID: " + id + " Não foi encontrado"));

        produtoEditado.setNome(produto.getNome());
        produtoEditado.setObservacao(produto.getObservacao());
        produtoEditado.setPreco(produto.getPreco());
        produtoEditado.setUnidade(produto.getUnidade());

        produtoRepository.save(produtoEditado);

        return produtoEditado;
    }



    @Transactional
    public int calcularSaldoProduto(Long produtoId) {
        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findByProdutoId(produtoId);

        return movimentacoes.stream()
                .mapToInt(mov -> {
                    if (mov.getTipo() == TipoMovimentacao.ENTRADA) {
                        return mov.getQuantidade();
                    } else {
                        return mov.getQuantidade();
                    }
                })
                .sum();
    }

}
