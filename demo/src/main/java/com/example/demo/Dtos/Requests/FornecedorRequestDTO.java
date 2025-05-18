package com.example.demo.Dtos.Requests;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FornecedorRequestDTO {
    

    @NotNull(message = "nome é obrigatório")
    private String nome;

    @NotNull(message = "cpf é obrigatório")
    private String cpf;
    
    @NotNull(message = "telefone é obrigatório")
    private String telefone;

    private String observacao;

}
