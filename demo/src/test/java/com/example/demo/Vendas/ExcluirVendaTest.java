package com.example.demo.Vendas;

import com.example.demo.Models.*;
import com.example.demo.Repositories.*;
import com.example.demo.Services.MovimentoEstoqueService;
import com.example.demo.Services.VendaService;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcluirVendaTest {

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
    void testExcluirVenda_sucesso_quandoReceitaNaoBaixada() {
        // Arrange
        Long vendaId = 1L;
        UUID codigoVenda = UUID.randomUUID();

        Venda venda = new Venda();
        venda.setId(vendaId);
        venda.setCodigo(codigoVenda);

        Receita receita = new Receita();
        receita.setId(10L);
        receita.setCodigo(codigoVenda);
        receita.setStBaixado(false); // <- Receita não está baixada

        ItemVenda item = new ItemVenda();
        MovimentacaoEstoque mov = new MovimentacaoEstoque();

        when(vendaRepository.findById(vendaId)).thenReturn(Optional.of(venda));
        when(receitaRepository.findByCodigo(codigoVenda)).thenReturn(Optional.of(receita));
        when(itemVendaRepository.findByVenda(venda)).thenReturn(List.of(item));
        when(movimentacaoEstoqueRepository.findByCodigo(codigoVenda)).thenReturn(List.of(mov));

        // Act
        vendaService.excluirVenda(vendaId);

        // Assert
        verify(itemVendaRepository).deleteAll(List.of(item));
        verify(movimentacaoEstoqueRepository).deleteAll(List.of(mov));
        verify(receitaRepository).deleteById(receita.getId());
        verify(vendaRepository).deleteById(vendaId);
    }

    @Test
    void testExcluirVenda_falha_quandoReceitaBaixada() {
        // Arrange
        Long vendaId = 2L;
        UUID codigoVenda = UUID.randomUUID();

        Venda venda = new Venda();
        venda.setId(vendaId);
        venda.setCodigo(codigoVenda);

        Receita receita = new Receita();
        receita.setId(11L);
        receita.setCodigo(codigoVenda);
        receita.setStBaixado(true); // <- Receita está baixada

        when(vendaRepository.findById(vendaId)).thenReturn(Optional.of(venda));
        when(receitaRepository.findByCodigo(codigoVenda)).thenReturn(Optional.of(receita));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> vendaService.excluirVenda(vendaId));
        assertTrue(ex.getMessage().contains("Receita já baixada"));

        verify(itemVendaRepository, never()).deleteAll(anyList());
        verify(vendaRepository, never()).deleteById(any());
    }
}
