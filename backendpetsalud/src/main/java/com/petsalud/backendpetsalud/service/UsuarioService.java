package com.petsalud.backendpetsalud.service;

import com.petsalud.backendpetsalud.dto.RegistroUsuarioDato;
import com.petsalud.backendpetsalud.entity.Rol;
import com.petsalud.backendpetsalud.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    Usuario crearUsuarioE(RegistroUsuarioDato registroUsuarioDato, Rol rol);
    
    Usuario registrarUsuario(RegistroUsuarioDato registroUsuarioDato);
    Optional<Usuario> obtenerUsuarioPorId(Long id);
    Optional<Usuario> obtenerUsuarioPorEmail(String email);
    Usuario actualizarUsuario(Long id, Usuario usuario);
    void eliminarUsuario(Long id);
    List<Usuario> listarUsuarios();
}
