package com.example.demo.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dtos.Requests.ReceitaRequestDTO;
import com.example.demo.Dtos.Responses.ReceitaResponseDTO;
import com.example.demo.Dtos.Updater.BaixarReceitaDTO;
import com.example.demo.Models.Receita;
import com.example.demo.Services.ReceitaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/receitas")
public class ReceitaController {

    private final ReceitaService receitaService;

    @GetMapping
    public ResponseEntity<List<ReceitaResponseDTO>> listar() {
        List<ReceitaResponseDTO> lista = receitaService.listarTodasReceitas();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<ReceitaResponseDTO> cadastrar(@RequestBody @Valid ReceitaRequestDTO dto) {
        Receita receita = receitaService.cadastrarReceita(dto);
        ReceitaResponseDTO responseDTO = receitaService.converterParaDTO(receita);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/baixar/{id}")
    public ResponseEntity<ReceitaResponseDTO> baixar(@PathVariable Long id, @RequestBody @Valid BaixarReceitaDTO dto) {
        dto.setReceitaId(id);
        Receita receita = receitaService.realizarBaixaDaReceita(dto);
        ReceitaResponseDTO responseDTO = receitaService.converterParaDTO(receita);
        return ResponseEntity.ok(responseDTO);
    }
}
