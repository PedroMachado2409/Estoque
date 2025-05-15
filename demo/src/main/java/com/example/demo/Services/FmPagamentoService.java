package com.example.demo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.FmPagamento;
import com.example.demo.Repositories.FmPagamentoRepository;

@Service
public class FmPagamentoService {
    
    @Autowired
    private FmPagamentoRepository fmPagamentoRepository;

    public FmPagamento cadastrarFmPagamento(FmPagamento fmPagamento){
        return fmPagamentoRepository.save(fmPagamento);
    }

    public List<FmPagamento> listarFormasDePagamento(){
        return fmPagamentoRepository.findAll();
    }

}
