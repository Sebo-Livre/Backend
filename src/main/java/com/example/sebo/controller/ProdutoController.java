package com.example.sebo.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.sebo.Repository.CategoriaRepository;
import com.example.sebo.Repository.ProdutoRepository;
import com.example.sebo.model.Categoria;
import com.example.sebo.model.Produto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {
    
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/categoria/all")
    public ResponseEntity <List<Categoria>>getAllByContainingIgnoreCase(){
        return ResponseEntity.ok(categoriaRepository.findAll());

    }

    @GetMapping("/produto/all")
    public ResponseEntity <List<Produto>>getAll(){
        return ResponseEntity.ok(produtoRepository.findAll());
    }
    @GetMapping("/{id}")
	public ResponseEntity <Produto> getById(@PathVariable Long id){
		return produtoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.notFound().build());
    
	}  @GetMapping("/produto/{produto}") 
       public ResponseEntity<Object> getByProduto(@PathVariable String produto){
          return ResponseEntity.ok(produtoRepository.findAllByProdutoContainingIgnoreCase(produto));
		
	}
	@PostMapping
    public ResponseEntity <Produto> postProduto(@Valid @RequestBody Produto produto){

        if (categoriaRepository.existsById(produto.getCategoria().getId()))
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
	@PutMapping
    public ResponseEntity <Produto> putProduto(@Valid @RequestBody Produto produto){

        if (produtoRepository.existsById(produto.getId())) {

            if (categoriaRepository.existsById(produto.getCategoria().getId()))
                return ResponseEntity.status(HttpStatus.OK).body(produtoRepository.save(produto));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
	
	@DeleteMapping("/{id}")
    public ResponseEntity <?> deleteProduto(@PathVariable Long id){

        return produtoRepository.findById(id)
                .map(resposta -> {
                    produtoRepository.deleteById(id);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
	
}


