package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.EnderecoCliente;

public interface EnderecoClienteRepository extends JpaRepository<EnderecoCliente, Long > {
    
}
