package com.example.demo.Dtos.Updater;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaixarReceitaDTO {

    private Long receitaId;
    
    @NotNull(message = "Conta é obrigatório")
    private Long contaId;

}
