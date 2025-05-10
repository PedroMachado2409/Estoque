package com.example.demo.Dtos.Responses;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ItemVendaResponseDTO {
    
    private Long produtoId;
    private String produtoNome;
    private Integer quantidade;
    private BigDecimal precoUnitario;


}
