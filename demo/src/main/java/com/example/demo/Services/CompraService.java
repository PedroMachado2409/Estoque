package com.example.demo.Services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.Dtos.Requests.CompraRequestDTO;
import com.example.demo.Dtos.Requests.ItemCompraRequestDTO;
import com.example.demo.Dtos.Responses.CompraResponseDTO;
import com.example.demo.Dtos.Responses.ItemCompraResponseDTO;
import com.example.demo.Models.Compra;
import com.example.demo.Models.Despesa;
import com.example.demo.Models.FmPagamento;
import com.example.demo.Models.Fornecedor;
import com.example.demo.Models.ItemCompra;
import com.example.demo.Models.Produto;
import com.example.demo.Repositories.CompraRepository;
import com.example.demo.Repositories.DespesaRepository;
import com.example.demo.Repositories.ItemCompraRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final DespesaRepository despesaRepository;
    private final MovimentoEstoqueService movimentoEstoqueService;
    private final CompraRepository compraRepository;
    private final ProdutoService produtoService;
    private final FornecedorService fornecedorService;
    private final ItemCompraRepository itemCompraRepository;
    private final FmPagamentoService fmPagamentoService;


 

    
    public Compra realizarCompra(CompraRequestDTO dto){
        final Fornecedor fornecedor = fornecedorService.obterFornecedorPorId(dto.getFornecedorId());
        final FmPagamento pagamento = fmPagamentoService.obterFmPagamentoPorId(dto.getFmPagamentoId());

        final Compra compra = new Compra();
        compra.setFornecedor(fornecedor);
        compra.setFmPagamento(pagamento);
        compra.setObservacao(dto.getObservacao());
        
        final Compra compraSalva = compraRepository.save(compra);
        
        final List<ItemCompra> itens = cirarItensCompra(dto.getItensCompra(), compraSalva);
        itemCompraRepository.saveAll(itens);

        final BigDecimal total = calcularValorCompra(itens);
        compraSalva.setVlCompra(total);
        compraSalva.setItens(itens);
        compraRepository.save(compraSalva);

         for(ItemCompra item : itens){
            criarMovimentacao(item, compraSalva);
         }

         criarDespesa(compraSalva);

        return compraSalva;

       
    }


    public List<CompraResponseDTO> listarCompras(){
        return compraRepository.findAll().stream()
        .map(this::converterDto)
        .collect(Collectors.toList());
    }



//------------METODOS PRIVADOS----------------

    private CompraResponseDTO converterDto(Compra compra){
        final CompraResponseDTO dto = new CompraResponseDTO();
        dto.setId(compra.getId());
        dto.setFornecedorId(compra.getFmPagamento().getId());
        dto.setFornecedorNome(compra.getFornecedor().getNome());
        dto.setDtCadastro(compra.getDtCompra());
        dto.setVlTotal(compra.getVlCompra());

        final List<ItemCompraResponseDTO> itens = compra.getItens().stream().map(item ->{
            final ItemCompraResponseDTO iDto = new ItemCompraResponseDTO();
            iDto.setProdutoId(item.getProduto().getId());
            iDto.setProdutoNome(item.getProduto().getNome());
            iDto.setPrecoUnitario(item.getPrecoUnitario());
            iDto.setQuantidade(item.getQuantidade());
            return iDto; 
        }).collect(Collectors.toList());
        dto.setItens(itens);
        return dto;
   }


    private List<ItemCompra> cirarItensCompra(List<ItemCompraRequestDTO> itensDto, Compra compra){
        return itensDto.stream().map(dto -> {
            final Produto produto = produtoService.obterProdutoPorId(dto.getProdutoId());
            final ItemCompra item = new ItemCompra();
            item.setProduto(produto);
            item.setPrecoUnitario(dto.getPrecoUnitario());
            item.setQuantidade(dto.getQuantidade());
            item.setCompra(compra);
            return item;
        }).collect(Collectors.toList());
    }

    private BigDecimal calcularValorCompra(List<ItemCompra> itens){
        return itens.stream().map(item -> item.getPrecoUnitario().
        multiply(BigDecimal.valueOf(item.getQuantidade())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void criarMovimentacao(ItemCompra itemCompra, Compra compra){
        movimentoEstoqueService.registrarEntrada(itemCompra.getProduto().getId(), itemCompra.getQuantidade(),
         "Movimentação referente a compra" + compra.getId(), compra.getCodigo());
    }

    private void criarDespesa(Compra compra){
        Despesa despesa = new Despesa();
        despesa.setCodigo(compra.getCodigo());
        despesa.setDescricao("Depesa referente a compra: " + compra.getId());
        despesa.setFmPagamento(compra.getFmPagamento());
        despesa.setContaBaixada(null);
        despesa.setFornecedor(compra.getFornecedor());
        despesa.setVlDespesa(compra.getVlCompra());
         despesaRepository.save(despesa);

    }


}
