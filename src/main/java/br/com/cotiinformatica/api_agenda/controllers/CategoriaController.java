package br.com.cotiinformatica.api_agenda.controllers;

import br.com.cotiinformatica.api_agenda.Exceptions.CategoriaNaoEncontradaException;
import br.com.cotiinformatica.api_agenda.dtos.CategoriaRequest;
import br.com.cotiinformatica.api_agenda.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody CategoriaRequest request) {
        try {
            var response = categoriaService.cadastrar(request);
            return ResponseEntity.status(201).body(response);
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody CategoriaRequest request) {
        try {
            var response = categoriaService.atualizar(id, request);
            return ResponseEntity.status(200).body(response);
        }
        catch (CategoriaNaoEncontradaException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable Integer id) {
        try {
            var response = categoriaService.excluir(id);
            return ResponseEntity.status(200).body(response);
        }
        catch (CategoriaNaoEncontradaException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("consultar")
    public ResponseEntity<?> consultar() {
        try {
            var response = categoriaService.consultar();
            return ResponseEntity.status(200).body(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("obter/{id}")
    public ResponseEntity<?> obter(@PathVariable Integer id) {
        try {
            var response = categoriaService.obterPorId(id);
            return ResponseEntity.status(200).body(response);
        }
        catch (CategoriaNaoEncontradaException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
