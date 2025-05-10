package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Produto;
import java.util.List;


@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{
    
    List<Produto> findByNomeContainingIgnoreCase(String nome);

}
