package com.example.demo.Dtos.Responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

@Getter
@Setter
public class CompraResponseDTO {

    private Long id;
    private Long FornecedorId;
    private String FornecedorNome;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dtCadastro;
    private List<ItemCompraResponseDTO> itens;
    private BigDecimal vlTotal;

}
