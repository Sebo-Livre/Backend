package com.example.sebo.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.sebo.Repository.CategoriaRepository;
import com.example.sebo.model.Categoria;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/categoria")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public ResponseEntity <List<Categoria>> getAll(){
        return ResponseEntity.ok(categoriaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity <Categoria> getById(@PathVariable Long id){
        return categoriaRepository.findById(id)
                        .map(resposta -> ResponseEntity.ok(resposta))
        .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<Object> getByCategoria(@PathVariable String categoria){
        return ResponseEntity.ok(categoriaRepository.findAllByCategoriaContainingIgnoreCase(categoria));
    }

    @PostMapping
    public ResponseEntity <Categoria> postCategoria(@Valid @RequestBody Categoria categoria){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaRepository.save(categoria));
    }

    @PutMapping
    public ResponseEntity <Categoria> putCategoria(@Valid @RequestBody Categoria categoria){
        return categoriaRepository.findById(categoria.getId())
                .map(resposta -> ResponseEntity.ok().body(categoriaRepository.save(categoria)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void deleteCategoria (@Valid @PathVariable Long id){
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if (categoria.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        categoriaRepository.deleteById(id);

    }
    

}
