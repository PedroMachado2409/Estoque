package com.example.demo.Models;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "Despesas")
@Entity
@Getter
@Setter
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private BigDecimal vlDespesa;

    @ManyToOne
    private FmPagamento fmPagamento;

    @ManyToOne
    private Fornecedor fornecedor;

    private UUID codigo = UUID.randomUUID();

    private Boolean stBaixado = false;
    
    @ManyToOne
    private Conta contaBaixada;

}
