package com.example.demo.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dtos.Requests.FornecedorRequestDTO;
import com.example.demo.Dtos.Responses.FornecedorResponseDTO;
import com.example.demo.Services.FornecedorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {
    
    private final FornecedorService fornecedorService;

    @GetMapping 
    public ResponseEntity<List<FornecedorResponseDTO>> listarFornecedores(){
            return ResponseEntity.ok(fornecedorService.listarFornecedores());
    }   

    @PostMapping
    public ResponseEntity<?> cadastrarFornecedor(@RequestBody @Valid FornecedorRequestDTO dto ){
        fornecedorService.cadastrarFornecedor(dto);
        return ResponseEntity.ok().body("Fornecedor Cadastrado com sucesso ! ");
    }

}
