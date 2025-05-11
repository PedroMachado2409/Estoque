package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dtos.Saldo.SaldoProdutoDTO;
import com.example.demo.Models.Produto;
import com.example.demo.Services.ProdutoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/produto")
public class ProdutoController {
    
    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public List<Produto> listarProdutos(){
        return produtoService.listarProdutos();
    }

    @PostMapping
    public ResponseEntity<?> cadastrarProdutos(@RequestBody @Valid Produto produto){
        produtoService.cadastrarProduto(produto);
        return ResponseEntity.ok().body("Produto Cadastrado com Sucesso");
    }

    @GetMapping("/{id}")
    public Produto obterProdutoPorId(@PathVariable Long id){
        return produtoService.obterProdutoPorId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> EditarProduto(@PathVariable Long id, @RequestBody Produto produtoEditado){
        produtoService.editarProduto(id, produtoEditado);
        return ResponseEntity.ok().body("Produto editado com sucesso !");
    }


    @GetMapping("/{produtoId}/saldo")
    public ResponseEntity<SaldoProdutoDTO> getSaldoProduto(@PathVariable Long produtoId) {
        int saldoAtual = produtoService.calcularSaldoProduto(produtoId);
        Produto produto = produtoService.obterProdutoPorId(produtoId);
        SaldoProdutoDTO saldoProdutoDTO = new SaldoProdutoDTO(produto.getNome(), saldoAtual);
        return ResponseEntity.ok(saldoProdutoDTO);
    }
}


