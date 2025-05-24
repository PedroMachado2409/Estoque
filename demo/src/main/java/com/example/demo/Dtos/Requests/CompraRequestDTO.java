package com.example.demo.Dtos.Requests;

import java.math.BigDecimal;
import java.util.List;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompraRequestDTO {
    
    private Long fornecedorId;

    private List<ItemCompraRequestDTO> itensCompra;

    private Long fmPagamentoId;

    private BigDecimal vlTotal;

    private String observacao;


}
