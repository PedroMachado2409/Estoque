package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.ItemCompra;

public interface ItemCompraRepository extends JpaRepository<ItemCompra, Long > {
    
}
