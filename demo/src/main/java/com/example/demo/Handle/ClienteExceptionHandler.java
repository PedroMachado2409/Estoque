package com.example.demo.Handle;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.Exception.ClienteException;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler global para exceções de Cliente.
 */
@ControllerAdvice
public class ClienteExceptionHandler {

    @ExceptionHandler(ClienteException.ClienteNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleClienteNaoEncontrado(ClienteException.ClienteNaoEncontradoException ex) {
        String mensagem = "Cliente não encontrado com o nome: " + ex.getNomeCliente();
        Map<String, String> error = new HashMap<>();
        error.put("error", mensagem);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClienteException.ClienteDuplicadoException.class)
    public ResponseEntity<Map<String, String>> handleClienteDuplicado(ClienteException.ClienteDuplicadoException ex) {
        String mensagem = "Cliente duplicado com CPF: " + ex.getCpfCliente();
        Map<String, String> error = new HashMap<>();
        error.put("error", mensagem);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

}
