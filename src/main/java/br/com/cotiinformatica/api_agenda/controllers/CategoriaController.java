package br.com.cotiinformatica.api_agenda.controllers;

import br.com.cotiinformatica.api_agenda.Exceptions.CategoriaNaoEncontradaException;
import br.com.cotiinformatica.api_agenda.components.JwtTokenComponent;
import br.com.cotiinformatica.api_agenda.dtos.CategoriaRequest;
import br.com.cotiinformatica.api_agenda.services.CategoriaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private JwtTokenComponent jwtTokenComponent;

    @PostMapping("cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody CategoriaRequest request, HttpServletRequest http) {
        try {
            var usuarioId = jwtTokenComponent.getUserId(http);
            var response = categoriaService.cadastrar(request, usuarioId);
            return ResponseEntity.status(201).body(response);
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id,
                                       @RequestBody CategoriaRequest request, HttpServletRequest http) {
        try {
            var usuarioId = jwtTokenComponent.getUserId(http);
            var response = categoriaService.atualizar(id, request, usuarioId);
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
    public ResponseEntity<?> excluir(@PathVariable Integer id, HttpServletRequest http) {
        try {
            var usuarioId = jwtTokenComponent.getUserId(http);
            var response = categoriaService.excluir(id, usuarioId);
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
    public ResponseEntity<?> consultar(HttpServletRequest http) {
        try {
            var usuarioId = jwtTokenComponent.getUserId(http);
            var response = categoriaService.consultar(usuarioId);
            return ResponseEntity.status(200).body(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("obter/{id}")
    public ResponseEntity<?> obter(@PathVariable Integer id, HttpServletRequest http) {
        try {
            var usuarioId = jwtTokenComponent.getUserId(http);
            var response = categoriaService.obterPorId(id, usuarioId);
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
