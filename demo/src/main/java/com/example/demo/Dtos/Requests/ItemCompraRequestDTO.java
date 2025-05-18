package com.example.demo.Dtos.Requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCompraRequestDTO {
    
    @NotNull
    private Long produtoId;

    private Integer quantidade;

    private BigDecimal precoUnitario;

    private Long vendaId;

}
