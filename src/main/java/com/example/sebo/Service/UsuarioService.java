package com.example.sebo.Service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import com.example.sebo.Repository.UsuarioRepository;
import com.example.sebo.model.Usuario;
import com.example.sebo.model.UsuarioLogin;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Optional<Usuario> cadastrarUsuario(Usuario usuario) {

        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
            return Optional.empty();

        usuario.setSenha(criptografarSenha(usuario.getSenha()));

        return Optional.of(usuarioRepository.save(usuario));
    }

    public Optional<Usuario> atualizarUsuario(Usuario usuario) {

        if (usuarioRepository.findById(usuario.getId()).isPresent()) {

            usuario.setSenha(criptografarSenha(usuario.getSenha()));
            return Optional.ofNullable(usuarioRepository.save(usuario));

        }
        return Optional.empty();
    }

    public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {

        Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());

        if (usuario.isPresent()) {

            if (compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {

                usuarioLogin.get().setId(usuario.get().getId());
                usuarioLogin.get().setNome(usuario.get().getNome());
                usuarioLogin.get()
                        .setToken(gerarBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
                usuarioLogin.get().setSenha(usuario.get().getSenha());
                usuarioLogin.get().setTipoUsuario(usuario.get().getTipoUsuario());

                return usuarioLogin;
            }
        }
        return Optional.empty();
    }

    private String gerarBasicToken(String usuario, String senha) {
        String token = usuario + ":" + senha;
        byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(tokenBase64);
    }

    private boolean compararSenhas(String senha, String senha2) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(senha, senha2);
    }

    private String criptografarSenha(String senha) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(senha);
    }
}