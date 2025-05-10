package com.example.demo.Models;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Cliente")
@Getter
@Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Data de Nascimento é obrigatório")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dtNascimento;

    @NotNull(message = "Cpf é obrigatório")
    private String Cpf;

    private String observacao;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dtCadastro;

    private UUID codigo = UUID.randomUUID();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnderecoCliente> enderecos;
    
}
