package com.example.demo.Dtos.Responses;

import lombok.Data;

@Data
public class EnderecoClienteResponseDTO {
    private String rua;
    private String cep;
    private String bairro;
    private String estado;
    private String cidade;
    private String numero;
    private String complemento;
    private String referencia;
}

