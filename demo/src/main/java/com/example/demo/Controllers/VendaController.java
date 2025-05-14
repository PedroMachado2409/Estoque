package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.demo.Dtos.Requests.VendaRequestDTO;
import com.example.demo.Dtos.Responses.VendaResponseDTO;
import com.example.demo.Services.VendaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {
    
    @Autowired
    private VendaService vendaService;

    @PostMapping
    public ResponseEntity<?> cadastrarVenda(@RequestBody VendaRequestDTO dto){
        vendaService.criarVenda(dto);
        return ResponseEntity.ok().body("Venda cadastrada com sucesso !");
    }

    @GetMapping
    public List<VendaResponseDTO> listarVendas(){
        return vendaService.listarVendas();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirVenda(@PathVariable @Valid Long id){
        vendaService.excluirVenda(id);
        return ResponseEntity.ok().body("Venda excluida com sucesso !");
    }


}
