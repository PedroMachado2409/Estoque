package com.example.demo.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dtos.Requests.CompraRequestDTO;
import com.example.demo.Dtos.Responses.CompraResponseDTO;
import com.example.demo.Services.CompraService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraController {
    
    private final CompraService compraService;

    @PostMapping
    public ResponseEntity<?> cadastrarCompra(@RequestBody @Valid CompraRequestDTO dto){
         compraService.realizarCompra(dto);
         return ResponseEntity.ok().body("Compra cadastrada com Sucesso !");
    }

    @GetMapping
    public List<CompraResponseDTO> listarCompras(){
        return compraService.listarCompras();
    }

}
