package com.example.demo.Models;

import java.math.BigDecimal;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Produto")
@Getter
@Setter
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Unidade é obrigatório")
    private String unidade;

    @NotNull(message = "Preço sugerido é obrigatório")
    private BigDecimal preco;

    private String observacao;


}
