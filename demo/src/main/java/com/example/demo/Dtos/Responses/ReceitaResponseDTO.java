package com.example.demo.Dtos.Responses;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceitaResponseDTO {
    
    private Long id;
    private String fmPagamento;
    private Long clienteId;
    private String nomeCliente;
    private BigDecimal vlReceita;
    private Boolean stBaixado;
    private String nmConta;

}
