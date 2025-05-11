package com.example.demo.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

import com.example.demo.Enums.TipoMovimentacao;

@Entity
@Table(name = "movimentacao_estoque")
@Getter
@Setter
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Produto produto;

    @NotNull
    private LocalDate data;

    @NotNull
    private Integer quantidade; 

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;
    
    private String observacao;

    private UUID codigo = UUID.randomUUID();
}
