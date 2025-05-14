package com.example.demo.Dtos.Requests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class VendaRequestDTO {

    @NotNull
    private Long clienteId;
    
    @NotNull
    private List<ItemVendaRequestDTO> itemVenda;

    @NotNull
    private BigDecimal vlTotal;

    private UUID codigo = UUID.randomUUID();

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull
    private LocalDate dtVenda;


}
