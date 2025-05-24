package com.example.demo.Services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Dtos.Requests.FornecedorRequestDTO;
import com.example.demo.Dtos.Responses.FornecedorResponseDTO;
import com.example.demo.Models.Fornecedor;
import com.example.demo.Repositories.FornecedorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FornecedorService {
    
    private final FornecedorRepository fornecedorRepository;

    public List<FornecedorResponseDTO> listarFornecedores(){
        List<Fornecedor> fornecedor = fornecedorRepository.findAll();
        return fornecedor.stream().map(this::converterResponseDTO).toList();
    }

    @Transactional
    public Fornecedor cadastrarFornecedor(FornecedorRequestDTO fornecedor){
        Fornecedor novoFornecedor = criarFornecedor(fornecedor);
        return fornecedorRepository.save(novoFornecedor);
    }

    public Fornecedor obterFornecedorPorId(Long id){
        return fornecedorRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado com o id" + id));
    }




    //------------MEOTODOS PRIVADOS-----------

    private FornecedorResponseDTO converterResponseDTO(Fornecedor fornecedor){
        FornecedorResponseDTO dto = new FornecedorResponseDTO();
        dto.setId(fornecedor.getId());
        dto.setNome(fornecedor.getNome());
        dto.setCpf(fornecedor.getCpf());
        dto.setDataCadastro(fornecedor.getDtCadastro());
        dto.setObservacao(fornecedor.getObservacao());
        dto.setTelefone(fornecedor.getTelefone());
        return dto;
    }

    private Fornecedor criarFornecedor(FornecedorRequestDTO dto){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(dto.getNome());
        fornecedor.setCpf(dto.getCpf());
        fornecedor.setObservacao(dto.getObservacao());
        fornecedor.setTelefone(dto.getTelefone());
        return fornecedor; 
    }


}
