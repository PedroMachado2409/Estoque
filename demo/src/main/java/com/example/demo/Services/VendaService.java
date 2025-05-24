package com.example.demo.Services;

import com.example.demo.Dtos.Requests.ItemVendaRequestDTO;
import com.example.demo.Dtos.Requests.VendaRequestDTO;
import com.example.demo.Dtos.Responses.ItemVendaResponseDTO;
import com.example.demo.Dtos.Responses.ProdutoClienteQuantidadeDTO;
import com.example.demo.Dtos.Responses.VendaResponseDTO;
import com.example.demo.Enums.TipoMovimentacao;
import com.example.demo.Models.*;
import com.example.demo.Repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoService produtoService;
    private final ItemVendaRepository itemVendaRepository;
    private final ReceitaRepository receitaRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final MovimentoEstoqueService movimentoEstoqueService;
    private final ClienteService clienteService;

    @Transactional
    public Venda criarVenda(VendaRequestDTO dto) {
        final Cliente cliente = clienteService.obterClientePorId(dto.getClienteId());

        final Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setDtVenda(LocalDate.now());

        final Venda vendaSalva = vendaRepository.save(venda);

        final List<ItemVenda> itens = criarItensVenda(dto.getItemVenda(), vendaSalva);
        itemVendaRepository.saveAll(itens);

        final BigDecimal total = calcularTotalVenda(itens);
        vendaSalva.setVlTotal(total);
        vendaSalva.setItemVenda(itens);
        vendaRepository.save(vendaSalva);

        for (ItemVenda item : itens) {
            criarMovimentacao(item, vendaSalva);
        }

        gerarReceitaDaVenda(vendaSalva);

        return vendaSalva;
    }

    public List<VendaResponseDTO> listarVendas() {
        return vendaRepository.findAll().stream()
                .map(this::toVendaResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void excluirVenda(Long vendaId) {
        final Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new EntityNotFoundException("Venda não encontrada com ID: " + vendaId));

        final Receita receita = receitaRepository.findByCodigo(venda.getCodigo()).orElse(null);

        if (receita != null && Boolean.TRUE.equals(receita.getStBaixado())) {
            throw new RuntimeException("Receita já baixada (ID: " + receita.getId()
                    + "). Remova a baixa da receita antes de excluir a venda.");
        }

        itemVendaRepository.deleteAll(itemVendaRepository.findByVenda(venda));
        movimentacaoEstoqueRepository.deleteAll(movimentacaoEstoqueRepository.findByCodigo(venda.getCodigo()));

        if (receita != null) {
            receitaRepository.deleteById(receita.getId());
        }

        vendaRepository.deleteById(vendaId);
    }

    // ----------------- Métodos Privados ------------------


    private List<ItemVenda> criarItensVenda(List<ItemVendaRequestDTO> itensDto, Venda venda) {
        return itensDto.stream().map(dto -> {
            final Produto produto = produtoService.obterProdutoPorId(dto.getProdutoId());
            
            final ItemVenda item = new ItemVenda();
            item.setProduto(produto);
            item.setQuantidade(dto.getQuantidade());
            item.setPrecoUnitario(dto.getPrecoUnitario());
            item.setVenda(venda);

            
            return item;
        }).collect(Collectors.toList());
    }

    private void criarMovimentacao(ItemVenda itemVenda, Venda venda) {
        movimentoEstoqueService.registrarSaida(itemVenda.getProduto().getId(), itemVenda.getQuantidade(),
                "Movimentação referente a venda: " + venda.getId(), venda.getCodigo(), venda.getCliente());
    }

    private BigDecimal calcularTotalVenda(List<ItemVenda> itens) {
        return itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void gerarReceitaDaVenda(Venda venda) {
        final Receita receita = new Receita();
        receita.setCodigo(venda.getCodigo());
        receita.setDescricao("Receita gerada pela venda ID: " + venda.getId());
        receita.setVlReceita(venda.getVlTotal());
        receita.setCliente(venda.getCliente());

        receitaRepository.save(receita);
    }

    private VendaResponseDTO toVendaResponseDTO(Venda venda) {
        final VendaResponseDTO dto = new VendaResponseDTO();
        dto.setId(venda.getId());
        dto.setVlTotal(venda.getVlTotal());
        dto.setClienteId(venda.getCliente().getId());
        dto.setClienteNome(venda.getCliente().getNome());
        dto.setDtCadastro(venda.getDtVenda());

        final List<ItemVendaResponseDTO> itens = venda.getItemVenda().stream().map(item -> {
            final ItemVendaResponseDTO iDto = new ItemVendaResponseDTO();
            iDto.setProdutoId(item.getProduto().getId());
            iDto.setProdutoNome(item.getProduto().getNome());
            iDto.setPrecoUnitario(item.getPrecoUnitario());
            iDto.setQuantidade(item.getQuantidade());
            return iDto;
        }).collect(Collectors.toList());

        dto.setItens(itens);
        return dto;
    }


}
