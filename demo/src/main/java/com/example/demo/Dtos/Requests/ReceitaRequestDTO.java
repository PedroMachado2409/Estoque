package com.example.demo.Dtos.Requests;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.demo.Models.Conta;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceitaRequestDTO {
    
    private Long id;

    @NotNull(message = "Descrição é obrigatório")
    private String descricao;
    @NotNull(message = "Valor da Receitaé obrigatório")
    @DecimalMin(value = "0", inclusive = false, message = "O valor não pode ser menor ou igual a 0" )
    private BigDecimal vlReceita;
    @NotNull(message = "Forma de Pagamento é obrigatório")
    private Long fmPagamentoId;
    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;

    private UUID codigo = UUID.randomUUID();
    private Boolean stBaixado = false;
    
}
