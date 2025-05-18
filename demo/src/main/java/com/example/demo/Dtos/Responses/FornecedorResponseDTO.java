package com.example.demo.Dtos.Responses;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FornecedorResponseDTO {
    
    private Long id;

    private String nome;

    private String cpf;

    private String telefone;

    private String observacao;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataCadastro;

}
