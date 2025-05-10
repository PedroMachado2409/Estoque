package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dtos.Requests.VendaRequestDTO;
import com.example.demo.Dtos.Responses.VendaResponseDTO;
import com.example.demo.Services.VendaService;

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


}
