package com.example.demo.Vendas;

import com.example.demo.Dtos.Responses.ItemVendaResponseDTO;
import com.example.demo.Dtos.Responses.VendaResponseDTO;
import com.example.demo.Models.*;
import com.example.demo.Repositories.*;
import com.example.demo.Services.MovimentoEstoqueService;
import com.example.demo.Services.VendaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListarVendasTest {

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
    void testListarVendas_sucesso() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João");

        Produto produto = new Produto();
        produto.setId(10L);
        produto.setNome("Produto Teste");

        ItemVenda itemVenda = new ItemVenda();
        itemVenda.setProduto(produto);
        itemVenda.setQuantidade(2);
        itemVenda.setPrecoUnitario(BigDecimal.valueOf(50));

        Venda venda = new Venda();
        venda.setId(100L);
        venda.setCodigo(UUID.randomUUID());
        venda.setCliente(cliente);
        venda.setVlTotal(BigDecimal.valueOf(100));
        venda.setItemVenda(List.of(itemVenda));
        venda.setDtVenda(LocalDate.now());

        when(vendaRepository.findAll()).thenReturn(List.of(venda));

        // Act
        List<VendaResponseDTO> resultado = vendaService.listarVendas();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        VendaResponseDTO vendaDTO = resultado.get(0);
        assertEquals(100L, vendaDTO.getId());
        assertEquals(cliente.getId(), vendaDTO.getClienteId());
        assertEquals(cliente.getNome(), vendaDTO.getClienteNome());
        assertEquals(BigDecimal.valueOf(100), vendaDTO.getVlTotal());
        assertEquals(1, vendaDTO.getItens().size());

        ItemVendaResponseDTO itemDTO = vendaDTO.getItens().get(0);
        assertEquals(produto.getId(), itemDTO.getProdutoId());
        assertEquals(produto.getNome(), itemDTO.getProdutoNome());
        assertEquals(2, itemDTO.getQuantidade());
        assertEquals(BigDecimal.valueOf(50), itemDTO.getPrecoUnitario());
    }
}
