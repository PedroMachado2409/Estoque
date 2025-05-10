package com.example.demo.Dtos.Requests;


import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteRequestDTO {

    @NotNull
    private String nome;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dtNascimento;

    @NotNull
    private String cpf;

    private String observacao;

    @NotNull
    private List<EnderecoClienteRequestDTO> enderecos;
}
