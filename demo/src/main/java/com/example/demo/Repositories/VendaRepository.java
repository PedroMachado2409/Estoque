package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.ItemVenda;
import com.example.demo.Models.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    

}
