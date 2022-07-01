package com.example.sebo.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sebo.Repository.UsuarioRepository;
import com.example.sebo.Service.UsuarioService;
import com.example.sebo.model.Usuario;
import com.example.sebo.model.UsuarioLogin;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired UsuarioRepository usuarioRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Usuario>> getAll(){
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity <Usuario> getById(@PathVariable Long id){
        return usuarioRepository.findById(id)
                        .map(resposta -> ResponseEntity.ok(resposta))
                        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/cadastrar")
   public ResponseEntity <Usuario> postUsuario(@Valid @RequestBody Usuario usuario){
    return usuarioService.cadastrarUsuario(usuario)
                        .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
                        .orElse(ResponseEntity.badRequest().build());
   }

   @PutMapping("/atualizar")
   public ResponseEntity <Usuario> putUsuario(@Valid @RequestBody Usuario usuario){
    return usuarioService.atualizarUsuario(usuario)
                        .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                        .orElse(ResponseEntity.notFound().build());
   }

   @PostMapping("/logar")
   public ResponseEntity <UsuarioLogin> loginUsuario(@RequestBody Optional<UsuarioLogin> usuarioLogin) {
        return usuarioService.autenticarUsuario(usuarioLogin)
                                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    
   }
   
        
    }
    

