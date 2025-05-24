package com.example.demo.Repositories;


import com.example.demo.Enums.TipoMovimentacao;
import com.example.demo.Models.MovimentacaoEstoque;


import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {


    List<MovimentacaoEstoque> findByProdutoId (Long produtoId);
    
    List<MovimentacaoEstoque> findByCodigo(UUID codigo);

    List<MovimentacaoEstoque> findByProdutoIdAndTipo(Long produtoId, TipoMovimentacao tipo);
}
