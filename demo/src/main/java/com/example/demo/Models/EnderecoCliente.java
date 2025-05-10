package com.example.demo.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "EnderecoCliente")
@Getter
@Setter
public class EnderecoCliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Cliente é obrigatório")
    @JoinColumn(name = "IdCliente")
    @ManyToOne
    private Cliente cliente;

    @NotNull(message = "Rua é obrigatório")
    private String rua;

    @NotNull(message = "Cep é obrigatório")
    private String cep;
    
    @NotNull(message = "bairro é obrigatório")
    private String bairro;

    @NotNull(message = "Estado é obrigatório")
    private String estado;

    @NotNull(message = "Cidade é obrigatório")
    private String cidade;

    @NotNull(message = "numero é obrigatório")
    private String numero;

    private String complemento;

    private String referencia;

}
