package com.example.demo.Dtos.Responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoClienteQuantidadeDTO {
    private Long clienteId;
    private String nomeCliente;
    private Long produtoId;
    private String nomeProduto;
    private int quantidadeTotal;
    
    
}
