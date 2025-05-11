package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.ItemVenda;
import com.example.demo.Models.Venda;

import java.util.List;


public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    
    List<ItemVenda> findByVenda(Venda venda);

}
