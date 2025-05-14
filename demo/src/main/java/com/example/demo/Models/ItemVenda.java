package com.example.demo.Models;


import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Table(name = "itemVenda")
@Entity
@Getter
@Setter
public class ItemVenda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "pelomenos um produto é obrigatório")
    private Produto produto;

    private Integer quantidade;

    private BigDecimal precoUnitario;

    @ManyToOne 
    private Venda venda;

}
