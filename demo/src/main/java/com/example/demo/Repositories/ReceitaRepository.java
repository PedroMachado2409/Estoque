package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Receita;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    
}
