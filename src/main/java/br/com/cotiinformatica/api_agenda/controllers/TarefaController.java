package br.com.cotiinformatica.api_agenda.controllers;

import br.com.cotiinformatica.api_agenda.Exceptions.CategoriaNaoEncontradaException;
import br.com.cotiinformatica.api_agenda.components.PublisherComponent;
import br.com.cotiinformatica.api_agenda.dtos.TarefaRequest;

import br.com.cotiinformatica.api_agenda.services.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @PostMapping("cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody TarefaRequest request) {
        try {
            var response = tarefaService.cadastrar(request);
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
    public ResponseEntity<?> atualizar(@PathVariable Integer id) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable Integer id) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("consultar")
    public ResponseEntity<?> consultar() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("obter/{id}")
    public ResponseEntity<?> obter(@PathVariable Integer id) {
        return ResponseEntity.ok().build();
    }
}
