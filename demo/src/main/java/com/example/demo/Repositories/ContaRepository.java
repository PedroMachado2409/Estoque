package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long>{
    
}
