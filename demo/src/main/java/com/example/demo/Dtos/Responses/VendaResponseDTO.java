package com.example.demo.Dtos.Responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class VendaResponseDTO {
    
    private Long id;
    private Long clienteId;
    private String clienteNome;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dtCadastro;
    private List<ItemVendaResponseDTO> itens;
    private BigDecimal vlTotal;

}
