package com.example.demo.Clientes;

import com.example.demo.Dtos.Requests.ClienteRequestDTO;
import com.example.demo.Dtos.Requests.EnderecoClienteRequestDTO;
import com.example.demo.Dtos.Responses.ClienteResponseDTO;
import com.example.demo.Models.Cliente;
import com.example.demo.Models.EnderecoCliente;
import com.example.demo.Repositories.ClienteRepository;
import com.example.demo.Repositories.EnderecoClienteRepository;
import com.example.demo.Services.ClienteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EnderecoClienteRepository enderecoClienteRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCadastrarCliente_sucesso() {
        // Arrange
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNome("Maria Silva");
        dto.setCpf("12345678900");
        dto.setDtNascimento(LocalDate.of(1988, 4, 12));
        dto.setObservacao("Observação qualquer");

        EnderecoClienteRequestDTO enderecoDto = new EnderecoClienteRequestDTO();
        enderecoDto.setRua("Rua das Flores");
        enderecoDto.setCep("12345000");
        enderecoDto.setBairro("Jardim");
        enderecoDto.setEstado("SP");
        enderecoDto.setCidade("São Paulo");
        enderecoDto.setNumero("123");
        enderecoDto.setComplemento("Apto 45");
        enderecoDto.setReferencia("Perto da escola");
        dto.setEnderecos(List.of(enderecoDto));

        Cliente clienteSalvo = new Cliente();
        clienteSalvo.setId(1L);
        clienteSalvo.setNome(dto.getNome());
        clienteSalvo.setCpf(dto.getCpf());
        clienteSalvo.setDtNascimento(dto.getDtNascimento());
        clienteSalvo.setObservacao(dto.getObservacao());
        clienteSalvo.setDtCadastro(LocalDate.now());

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);
        when(enderecoClienteRepository.saveAll(anyList())).thenReturn(null);

        // Act
        Cliente resultado = clienteService.cadastrarCliente(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(dto.getNome(), resultado.getNome());
        assertEquals(dto.getCpf(), resultado.getCpf());

        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(enderecoClienteRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testListarTodosClientes_retornaListaDTO() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(2L);
        cliente.setNome("João Pereira");
        cliente.setCpf("98765432100");
        cliente.setDtNascimento(LocalDate.of(1975, 1, 10));
        cliente.setDtCadastro(LocalDate.of(2020, 6, 15));
        cliente.setObservacao("Cliente especial");

        EnderecoCliente endereco = new EnderecoCliente();
        endereco.setRua("Av Brasil");
        endereco.setCep("54321000");
        endereco.setBairro("Centro");
        endereco.setEstado("RJ");
        endereco.setCidade("Rio de Janeiro");
        endereco.setNumero("500");
        endereco.setComplemento("Casa");
        endereco.setReferencia("Próximo ao banco");

        cliente.setEnderecos(List.of(endereco));

        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        // Act
        List<ClienteResponseDTO> clientes = clienteService.listarTodosClientes();

        // Assert
        assertNotNull(clientes);
        assertEquals(1, clientes.size());

        ClienteResponseDTO dto = clientes.get(0);
        assertEquals(cliente.getId(), dto.getId());
        assertEquals(cliente.getNome(), dto.getNome());
        assertEquals(cliente.getCpf(), dto.getCpf());
        assertEquals(cliente.getObservacao(), dto.getObservacao());
        assertEquals(1, dto.getEnderecos().size());
        assertEquals(endereco.getRua(), dto.getEnderecos().get(0).getRua());
    }
}
