package com.example.demo.Dtos.Saldo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaldoProdutoDTO {
    
    private String nomeProduto;
    private int saldoAtual;

    public SaldoProdutoDTO(String nomeProduto, int saldoAtual) {
        this.nomeProduto = nomeProduto;
        this.saldoAtual = saldoAtual;
    }

}
