package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.ItemVenda;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    
}
