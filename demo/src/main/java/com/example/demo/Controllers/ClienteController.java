package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dtos.Requests.ClienteRequestDTO;
import com.example.demo.Dtos.Responses.ClienteResponseDTO;
import com.example.demo.Services.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {
   
    @Autowired
    private ClienteService clienteService;


    @PostMapping
    public ResponseEntity<?> cadastrarCliente(@RequestBody @Valid ClienteRequestDTO dto){
        clienteService.cadastrarCliente(dto);
        return ResponseEntity.ok().body("Cliente cadastrado com sucesso !");
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarTodosClientes());
    }

}
