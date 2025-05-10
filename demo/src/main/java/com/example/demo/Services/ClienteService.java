package com.example.demo.Services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Dtos.Requests.ClienteRequestDTO;
import com.example.demo.Dtos.Requests.EnderecoClienteRequestDTO;
import com.example.demo.Dtos.Responses.ClienteResponseDTO;
import com.example.demo.Dtos.Responses.EnderecoClienteResponseDTO;
import com.example.demo.Models.Cliente;
import com.example.demo.Models.EnderecoCliente;
import com.example.demo.Repositories.ClienteRepository;
import com.example.demo.Repositories.EnderecoClienteRepository;

import jakarta.transaction.Transactional;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoClienteRepository enderecoClienteRepository;

    @Transactional
    public Cliente cadastrarCliente(ClienteRequestDTO dto){
        
        Cliente novoCliente = new Cliente();
        novoCliente.setNome(dto.getNome());
        novoCliente.setCpf(dto.getCpf());
        novoCliente.setDtCadastro(LocalDate.now());
        novoCliente.setDtNascimento(dto.getDtNascimento());
        novoCliente.setObservacao(dto.getObservacao());

        clienteRepository.save(novoCliente);

        List<EnderecoCliente> enderecos = new ArrayList<>();
        for (EnderecoClienteRequestDTO enderecoDto : dto.getEnderecos()){
            EnderecoCliente endereco = new EnderecoCliente();
            endereco.setRua(enderecoDto.getRua());
            endereco.setCep(enderecoDto.getCep());
            endereco.setBairro(enderecoDto.getBairro());
            endereco.setEstado(enderecoDto.getEstado());
            endereco.setCidade(enderecoDto.getCidade());
            endereco.setNumero(enderecoDto.getNumero());
            endereco.setComplemento(enderecoDto.getComplemento());
            endereco.setReferencia(enderecoDto.getReferencia());
            endereco.setCliente(novoCliente);
            enderecos.add(endereco);
        }

        enderecoClienteRepository.saveAll(enderecos);
        return novoCliente;
         
    }   

    public List<ClienteResponseDTO> listarTodosClientes() {
    List<Cliente> clientes = clienteRepository.findAll();

    return clientes.stream().map(cliente -> {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setCpf(cliente.getCpf());
        dto.setDtNascimento(cliente.getDtNascimento());
        dto.setDtCadastro(cliente.getDtCadastro());
        dto.setObservacao(cliente.getObservacao());

        List<EnderecoClienteResponseDTO> enderecos = cliente.getEnderecos().stream().map(end -> {
            EnderecoClienteResponseDTO eDto = new EnderecoClienteResponseDTO();
            eDto.setRua(end.getRua());
            eDto.setCep(end.getCep());
            eDto.setBairro(end.getBairro());
            eDto.setEstado(end.getEstado());
            eDto.setCidade(end.getCidade());
            eDto.setNumero(end.getNumero());
            eDto.setComplemento(end.getComplemento());
            eDto.setReferencia(end.getReferencia());
            return eDto;
        }).toList();

        dto.setEnderecos(enderecos);
        return dto;
    }).toList();
}

}
