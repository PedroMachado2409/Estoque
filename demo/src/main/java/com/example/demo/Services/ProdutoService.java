package com.example.demo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Produto;
import com.example.demo.Repositories.ProdutoRepository;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository produtoRepository;

    
    public List<Produto> listarProdutos (){
        List<Produto> produtos = produtoRepository.findAll();
        if(produtos.isEmpty()){
            throw new RuntimeException("Não foi encontrado nenhum produto cadastrado");
        }
        return produtos;
    }

    public Produto obterProdutoPorId(Long id){
        return produtoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Produto com o ID: " + id + " Não foi encontrado"));
    }

    public Produto cadastrarProduto(Produto produto){
        return produtoRepository.save(produto);
    }

    public Produto editarProduto(Long id, Produto produto){
        Produto produtoEditado = produtoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Produto com o ID: " + id + " Não foi encontrado"));

        produtoEditado.setNome(produto.getNome());
        produtoEditado.setObservacao(produto.getObservacao());
        produtoEditado.setPreco(produto.getPreco());
        produtoEditado.setUnidade(produto.getUnidade());

        return produtoEditado;
    }

    public void deletarProduto(Long id){
        produtoRepository.deleteById(id);
    }

}
