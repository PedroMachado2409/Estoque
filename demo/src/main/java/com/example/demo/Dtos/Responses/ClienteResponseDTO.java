package com.example.demo.Dtos.Responses;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ClienteResponseDTO {
    private Long id;
    private String nome;
    private String cpf;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dtNascimento;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dtCadastro;
    private String observacao;
    private List<EnderecoClienteResponseDTO> enderecos;
}