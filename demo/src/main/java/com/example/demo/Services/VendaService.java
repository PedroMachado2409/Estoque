package com.example.demo.Services;

import com.example.demo.Dtos.Requests.ItemVendaRequestDTO;
import com.example.demo.Dtos.Requests.VendaRequestDTO;
import com.example.demo.Dtos.Responses.ItemVendaResponseDTO;
import com.example.demo.Dtos.Responses.VendaResponseDTO;
import com.example.demo.Models.Cliente;
import com.example.demo.Models.ItemVenda;
import com.example.demo.Models.MovimentacaoEstoque;
import com.example.demo.Models.Produto;
import com.example.demo.Models.Receita;
import com.example.demo.Models.Venda;
import com.example.demo.Repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

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

        List<ItemVenda> itens = adicionarItensVenda(dto.getItemVenda(), vendaSalva);

        BigDecimal valorTotal = calcularValorTotal(itens);

        vendaSalva.setVlTotal(valorTotal);
        vendaRepository.save(vendaSalva);
        gerarReceita(vendaSalva);

        itemVendaRepository.saveAll(itens);
        vendaSalva.setItemVenda(itens);

        return vendaSalva;

    }

    public List<VendaResponseDTO> listarVendas() {
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

    @Transactional
    public void excluirVenda(Long vendaId) {
        Venda vendaExcluida = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new EntityNotFoundException("Venda não localizada com o ID: " + vendaId));

        Receita receitaVenda = localizarReceitaPorVenda(vendaExcluida);

        if (receitaVenda != null && Boolean.TRUE.equals(receitaVenda.getStBaixado())) {
            throw new RuntimeException(
                    "Receita já baixada (ID: " + (receitaVenda.getId() != null ? receitaVenda.getId() : "N/A")
                            + "), para excluir a venda, remova a baixa da receita.");
        }

        List<ItemVenda> itensDaVenda = localizarOsItensDaVenda(vendaExcluida);
        itemVendaRepository.deleteAll(itensDaVenda);

        List<MovimentacaoEstoque> movimentacoes = localizarMovimentacoesGeradas(vendaExcluida);
        movimentacaoEstoqueRepository.deleteAll(movimentacoes);

        if (receitaVenda != null) {
            receitaRepository.deleteById(receitaVenda.getId());
        }
        vendaRepository.deleteById(vendaId);
    }


    // ---------- Métodos privados ----------

    private List<ItemVenda> adicionarItensVenda(List<ItemVendaRequestDTO> itensDto, Venda venda) {
        return itensDto.stream().map(itemDto -> {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Produto não encontrado com ID: " + itemDto.getProdutoId()));

            ItemVenda item = new ItemVenda();
            item.setProduto(produto);
            item.setQuantidade(itemDto.getQuantidade());
            item.setPrecoUnitario(itemDto.getPrecoUnitario());
            item.setVenda(venda);

            movimentoEstoqueService.registrarSaida(produto.getId(), itemDto.getQuantidade(),
                    "Saída referente a Venda: " + venda.getId(), venda.getCodigo());

            return item;
        }).collect(Collectors.toList());
    }

    private BigDecimal calcularValorTotal(List<ItemVenda> itens) {
        return itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void gerarReceita(Venda venda) {
        Receita receita = new Receita();
        receita.setCodigo(venda.getCodigo());
        receita.setDescricao("Receita gerada pela venda de código: " + venda.getId());
        receita.setVlReceita(venda.getVlTotal());
        receita.setCliente(venda.getCliente());
        receitaRepository.save(receita);
    }

    private Receita localizarReceitaPorVenda(Venda venda) {
        return receitaRepository.findByCodigo(venda.getCodigo()).orElse(null);
    }

    private List<ItemVenda> localizarOsItensDaVenda(Venda venda) {
        return itemVendaRepository.findByVenda(venda);
    }

    private List<MovimentacaoEstoque> localizarMovimentacoesGeradas(Venda venda){
        return movimentacaoEstoqueRepository.findByCodigo(venda.getCodigo());
    }
}