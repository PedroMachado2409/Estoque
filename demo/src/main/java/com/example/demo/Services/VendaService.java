package com.example.demo.Services;



import com.example.demo.Dtos.Requests.VendaRequestDTO;
import com.example.demo.Dtos.Responses.ItemVendaResponseDTO;
import com.example.demo.Dtos.Responses.VendaResponseDTO;
import com.example.demo.Models.Cliente;
import com.example.demo.Models.ItemVenda;
import com.example.demo.Models.Produto;
import com.example.demo.Models.Receita;
import com.example.demo.Models.Venda;
import com.example.demo.Repositories.ClienteRepository;
import com.example.demo.Repositories.ItemVendaRepository;
import com.example.demo.Repositories.ProdutoRepository;
import com.example.demo.Repositories.ReceitaRepository;
import com.example.demo.Repositories.VendaRepository;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemVendaRepository itemVendaRepository;
    private final ReceitaRepository receitaRepository;
    private final MovimentoEstoqueService movimentoEstoqueService;

    @Transactional
    public Venda criarVenda(VendaRequestDTO dto) {
    
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + dto.getClienteId()));
    
        Venda novaVenda = new Venda();
        novaVenda.setCliente(cliente);
        novaVenda.setDtVenda(dto.getDtVenda());
        novaVenda.setCodigo(dto.getCodigo());
    
        final Venda vendaSalva = vendaRepository.save(novaVenda);
    
        List<ItemVenda> itens = dto.getItemVenda().stream().map(itemDto -> {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + itemDto.getProdutoId()));
    
            ItemVenda item = new ItemVenda();
            item.setProduto(produto);
            item.setQuantidade(itemDto.getQuantidade());
            item.setPrecoUnitario(itemDto.getPrecoUnitario());
            item.setVenda(vendaSalva);

            movimentoEstoqueService.registrarSaida(produto.getId(), itemDto.getQuantidade(),
             "Sainda referente a Venda: " + vendaSalva.getId(), vendaSalva.getCodigo());

            return item;
        }).collect(Collectors.toList());
    
        BigDecimal valorTotal = itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    
     
        vendaSalva.setVlTotal(valorTotal);
        vendaRepository.save(vendaSalva);
    

        itemVendaRepository.saveAll(itens);
        vendaSalva.setItemVenda(itens);

        Receita receita = new Receita();
        receita.setCodigo(vendaSalva.getCodigo());
        receita.setDescricao("Receita gerada pela venda de codigo: " + vendaSalva.getId());
        receita.setVlReceita(vendaSalva.getVlTotal());
        receita.setCliente(vendaSalva.getCliente());
        receitaRepository.save(receita);
    
        return vendaSalva;
        
    }
    
    public List<VendaResponseDTO> listarVendas(){
        List<Venda> vendas = vendaRepository.findAll();

        return vendas.stream().map(venda -> {
            VendaResponseDTO dto = new VendaResponseDTO();
            dto.setId(venda.getId());
            dto.setVlTotal(venda.getVlTotal());
            dto.setClienteId(venda.getCliente().getId());
            dto.setClienteNome(venda.getCliente().getNome());
            dto.setDtCadastro(LocalDate.now());
            
            List<ItemVendaResponseDTO> itens = venda.getItemVenda().stream().map(item -> {
                ItemVendaResponseDTO iDto = new ItemVendaResponseDTO();
                iDto.setPrecoUnitario(item.getPrecoUnitario());
                iDto.setProdutoId(item.getProduto().getId());
                iDto.setProdutoNome(item.getProduto().getNome());
                iDto.setQuantidade(item.getQuantidade());
                return iDto;

            }).toList();

            dto.setItens(itens);
            return dto;

        }).toList();
    }


}
