package com.example.demo.Repositories;


import com.example.demo.Models.MovimentacaoEstoque;
import com.example.demo.Models.Produto;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findByProdutoIdOrderByDataAsc(Long produtoId);

    List<MovimentacaoEstoque> findByProdutoId (Long produtoId);
 
}
