package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    
}
