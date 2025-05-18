package com.example.demo.Services;

import java.time.LocalDate;
import java.util.List;


import org.springframework.stereotype.Service;

import com.example.demo.Dtos.Requests.ClienteRequestDTO;
import com.example.demo.Dtos.Requests.EnderecoClienteRequestDTO;
import com.example.demo.Dtos.Responses.ClienteResponseDTO;
import com.example.demo.Dtos.Responses.EnderecoClienteResponseDTO;
import com.example.demo.Exception.ClienteException;
import com.example.demo.Models.Cliente;
import com.example.demo.Models.EnderecoCliente;
import com.example.demo.Repositories.ClienteRepository;
import com.example.demo.Repositories.EnderecoClienteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

  
    private final ClienteRepository clienteRepository;
    private final EnderecoClienteRepository enderecoClienteRepository;

    @Transactional
    public Cliente cadastrarCliente(ClienteRequestDTO dto) {
        Cliente novoCliente = criarCliente(dto);
        validarCpfUnico(dto.getCpf());
        clienteRepository.save(novoCliente);
        
        List<EnderecoCliente> enderecos = criarEnderecos(dto.getEnderecos(), novoCliente);
        enderecoClienteRepository.saveAll(enderecos);

        return novoCliente;
    }

    public List<ClienteResponseDTO> listarTodosClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public Cliente obterClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteException.ClienteNaoEncontradoException(
                        "Cliente não encontrado com ID: " + id));
    }

    // --------- MÉTODOS PRIVADOS ---------

    private Cliente criarCliente(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setDtCadastro(LocalDate.now());
        cliente.setDtNascimento(dto.getDtNascimento());
        cliente.setObservacao(dto.getObservacao());
        return cliente;
    }

      private void validarCpfUnico(String cpf) {
        if (clienteRepository.existsByCpf(cpf)) {
            throw new ClienteException.ClienteDuplicadoException(cpf);
        }
    }

    private List<EnderecoCliente> criarEnderecos(List<EnderecoClienteRequestDTO> enderecosDto, Cliente cliente) {
        return enderecosDto.stream()
                .map(dto -> {
                    EnderecoCliente endereco = new EnderecoCliente();
                    endereco.setRua(dto.getRua());
                    endereco.setCep(dto.getCep());
                    endereco.setBairro(dto.getBairro());
                    endereco.setEstado(dto.getEstado());
                    endereco.setCidade(dto.getCidade());
                    endereco.setNumero(dto.getNumero());
                    endereco.setComplemento(dto.getComplemento());
                    endereco.setReferencia(dto.getReferencia());
                    endereco.setCliente(cliente);
                    return endereco;
                }).toList();
    }

    private ClienteResponseDTO converterParaResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setCpf(cliente.getCpf());
        dto.setDtNascimento(cliente.getDtNascimento());
        dto.setDtCadastro(cliente.getDtCadastro());
        dto.setObservacao(cliente.getObservacao());
        dto.setEnderecos(converterEnderecosParaResponseDTO(cliente.getEnderecos()));
        return dto;
    }

    private List<EnderecoClienteResponseDTO> converterEnderecosParaResponseDTO(List<EnderecoCliente> enderecos) {
        return enderecos.stream()
                .map(end -> {
                    EnderecoClienteResponseDTO dto = new EnderecoClienteResponseDTO();
                    dto.setRua(end.getRua());
                    dto.setCep(end.getCep());
                    dto.setBairro(end.getBairro());
                    dto.setEstado(end.getEstado());
                    dto.setCidade(end.getCidade());
                    dto.setNumero(end.getNumero());
                    dto.setComplemento(end.getComplemento());
                    dto.setReferencia(end.getReferencia());
                    return dto;
                }).toList();
    }

    
}

