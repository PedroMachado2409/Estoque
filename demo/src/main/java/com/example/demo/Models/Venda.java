package com.example.demo.Models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Vendas")
@Getter
@Setter
public class Venda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "Cliente é obrigatório")
    private Cliente cliente;

    @OneToMany(mappedBy = "venda")
    private List<ItemVenda> itemVenda;

    private BigDecimal vlTotal;

    @ManyToOne
    // @NotNull(message = "Forma de Pagamento é obrigatório")
    private FmPagamento fmPagamento;

    private UUID codigo = UUID.randomUUID();

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dtVenda;

}
