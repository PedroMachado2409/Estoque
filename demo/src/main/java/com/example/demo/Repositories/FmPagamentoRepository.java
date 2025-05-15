package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.FmPagamento;

public interface FmPagamentoRepository extends JpaRepository<FmPagamento, Long> {
    
}
