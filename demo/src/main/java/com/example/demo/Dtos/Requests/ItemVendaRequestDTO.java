package com.example.demo.Dtos.Requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemVendaRequestDTO {

    @NotNull
    private Long produtoId;  

    @NotNull
    private Integer quantidade;

    @NotNull
    private BigDecimal precoUnitario;

    @NotNull
    private Long vendaId;  

}