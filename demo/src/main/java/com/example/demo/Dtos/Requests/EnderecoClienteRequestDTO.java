package com.example.demo.Dtos.Requests;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoClienteRequestDTO {

    @NotNull
    private String rua;

    @NotNull
    private String cep;

    @NotNull
    private String bairro;

    @NotNull
    private String estado;

    @NotNull
    private String cidade;

    @NotNull
    private String numero;

    private String complemento;
    private String referencia;
}
