package com.example.demo.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Receita;

import java.util.Optional;
import java.util.UUID;


public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    
     Optional<Receita> findByCodigo(UUID codigo);

}
