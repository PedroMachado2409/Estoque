package com.example.demo.Models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "Compras")
@Entity
@Getter
@Setter
public class Compra {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Fornecedor fornecedor;

    private BigDecimal vlCompra;

    @ManyToOne
    private FmPagamento fmPagamento;

    @OneToMany
    private List<ItemCompra> itens;

    private UUID codigo = UUID.randomUUID();

    private String observacao;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dtCompra = LocalDate.now();

}   
