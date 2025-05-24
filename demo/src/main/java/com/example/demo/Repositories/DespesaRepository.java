package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Despesa;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    
} 