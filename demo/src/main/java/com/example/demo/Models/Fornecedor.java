package com.example.demo.Models;


import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Table(name = "Fornecedores")
@Entity
@Getter
@Setter
public class Fornecedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "nome é obrigatório")
    private String nome;

    @NotNull(message = "cpf é obrigatório")
    private String cpf;
    
    @NotNull(message = "telefone é obrigatório")
    private String telefone;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dtCadastro = LocalDate.now();

    private UUID codigo = UUID.randomUUID();

    private String observacao;


}
