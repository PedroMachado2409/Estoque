package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.FmPagamento;
import com.example.demo.Services.FmPagamentoService;

@RestController
@RequestMapping("/api/fmPagamento")
public class FmPagamentoController {
    
    @Autowired
    private FmPagamentoService fmPagamentoService;

    @GetMapping
    public List<FmPagamento> listarFormasDePagamento(){
        return fmPagamentoService.listarFormasDePagamento();
    }

    @PostMapping
    public FmPagamento cadastrar(@RequestBody FmPagamento fmPagamento){
        return fmPagamentoService.cadastrarFmPagamento(fmPagamento);
    }

}
