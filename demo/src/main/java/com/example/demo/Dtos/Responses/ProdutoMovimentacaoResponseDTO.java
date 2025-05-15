package com.example.demo.Dtos.Responses;


import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoMovimentacaoResponseDTO {
    
    private Long produtoId;
    private String nomeProduto;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dtMovimentacao;

    private Integer quantidade;
    private Enum tipo;
    private String observacao;

}
