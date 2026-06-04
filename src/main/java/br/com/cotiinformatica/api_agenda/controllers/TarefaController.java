package br.com.cotiinformatica.api_agenda.controllers;

import br.com.cotiinformatica.api_agenda.Exceptions.CategoriaNaoEncontradaException;
import br.com.cotiinformatica.api_agenda.Exceptions.TarefaNaoEncontradaException;
import br.com.cotiinformatica.api_agenda.components.JwtTokenComponent;
import br.com.cotiinformatica.api_agenda.components.PublisherComponent;
import br.com.cotiinformatica.api_agenda.dtos.TarefaRequest;

import br.com.cotiinformatica.api_agenda.services.TarefaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private JwtTokenComponent jwtTokenComponent;

    @PostMapping("cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody TarefaRequest request, HttpServletRequest http) {
        try {
            var usuarioId = jwtTokenComponent.getUserId(http);

            var response = tarefaService.cadastrar(request, usuarioId);
            return ResponseEntity.status(201).body(response);
        }
        catch(CategoriaNaoEncontradaException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody TarefaRequest request, HttpServletRequest http) {

        try {
            var usuarioId = jwtTokenComponent.getUserId(http);

            var response = tarefaService.atualizar(id, request, usuarioId);
            return ResponseEntity.status(200).body(response);
        }
        catch(CategoriaNaoEncontradaException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch(TarefaNaoEncontradaException e) {
            return ResponseEntity.status(404).body(e.getMessage());
            }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable Integer id, HttpServletRequest http) {
        try {
            var usuarioId = jwtTokenComponent.getUserId(http);

            var response = tarefaService.excluir(id, usuarioId);
            return ResponseEntity.status(200).body(response);
        }
        catch(TarefaNaoEncontradaException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("consultar")
    public ResponseEntity<?> consultar(LocalDate dataInicio, LocalDate dataFim, HttpServletRequest http) {
        try {
            var usuarioId = jwtTokenComponent.getUserId(http);

            var response = tarefaService.consultar(dataInicio, dataFim, usuarioId);
            return ResponseEntity.status(200).body(response);
        }
        catch(TarefaNaoEncontradaException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("obter/{id}")
    public ResponseEntity<?> obter(@PathVariable Integer id, HttpServletRequest http) {
        try {
            var usuarioId = jwtTokenComponent.getUserId(http);

            var response = tarefaService.excluir(id, usuarioId);
            return ResponseEntity.status(200).body(response);
        }
        catch(TarefaNaoEncontradaException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
