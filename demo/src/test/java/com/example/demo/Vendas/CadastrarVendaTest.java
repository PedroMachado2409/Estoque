package com.example.demo.Vendas;

import com.example.demo.Dtos.Requests.ItemVendaRequestDTO;
import com.example.demo.Dtos.Requests.VendaRequestDTO;
import com.example.demo.Models.*;
import com.example.demo.Repositories.*;
import com.example.demo.Services.MovimentoEstoqueService;
import com.example.demo.Services.VendaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CadastrarVendaTest {

    @InjectMocks
    private VendaService vendaService;

    @Mock
    private VendaRepository vendaRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private ItemVendaRepository itemVendaRepository;
    @Mock
    private ReceitaRepository receitaRepository;
    @Mock
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    @Mock
    private MovimentoEstoqueService movimentoEstoqueService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarVenda_sucesso() {
        // Arrange
        Long clienteId = 1L;
        Long produtoId = 10L;
        UUID codigoVenda = UUID.randomUUID();

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setNome("João");

        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Produto X");

        ItemVendaRequestDTO itemDTO = new ItemVendaRequestDTO();
        itemDTO.setProdutoId(produtoId);
        itemDTO.setQuantidade(2);
        itemDTO.setPrecoUnitario(BigDecimal.valueOf(50));

        VendaRequestDTO vendaDTO = new VendaRequestDTO();
        vendaDTO.setClienteId(clienteId);
        vendaDTO.setDtVenda(LocalDate.now());
        vendaDTO.setCodigo(codigoVenda);
        vendaDTO.setItemVenda(List.of(itemDTO));

        Venda vendaSalva = new Venda();
        vendaSalva.setId(1L);
        vendaSalva.setCodigo(codigoVenda);
        vendaSalva.setCliente(cliente);

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(vendaRepository.save(any(Venda.class))).thenReturn(vendaSalva);

        // Act
        Venda resultado = vendaService.criarVenda(vendaDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(clienteId, resultado.getCliente().getId());
        assertEquals(codigoVenda, resultado.getCodigo());
        assertEquals(BigDecimal.valueOf(100), resultado.getVlTotal());

        verify(receitaRepository, times(1)).save(any(Receita.class));
        verify(itemVendaRepository, times(1)).saveAll(anyList());
        verify(movimentoEstoqueService, times(1)).registrarSaida(eq(produtoId), eq(2), anyString(), eq(codigoVenda));
    }
}
