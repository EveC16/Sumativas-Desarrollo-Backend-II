package com.petsalud.backendpetsalud.service;

import com.petsalud.backendpetsalud.dto.RegistroUsuarioDato;
import com.petsalud.backendpetsalud.entity.Rol;
import com.petsalud.backendpetsalud.entity.Usuario;
import com.petsalud.backendpetsalud.repository.UsuarioRepository;
import com.petsalud.backendpetsalud.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    //Inyeccion de encriptador de contraseña
    @Autowired
    private PasswordEncoder passwordEncoder;

        @Override
    public Usuario crearUsuarioE(RegistroUsuarioDato registroUsuarioDato, Rol rol) {
        
        throw new UnsupportedOperationException("Unimplemented method 'crearUsuarioE'");
    }

    @Override
    public Usuario registrarUsuario(RegistroUsuarioDato registroUsuarioDato) {
        // Verificar si el email ya está registrado
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(registroUsuarioDato.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(registroUsuarioDato.getEmail());

        // Se encripta la contraseña 
        usuario.setPassword(passwordEncoder.encode(registroUsuarioDato.getPassword()));

        usuario.setNombre(registroUsuarioDato.getNombre());
        usuario.setApellido(registroUsuarioDato.getApellido());
        usuario.setTelefono(registroUsuarioDato.getTelefono());
        usuario.setDireccion(registroUsuarioDato.getDireccion());

        // Se asigna rol por defecto para que no lo puedan alterar sin permisos
        usuario.setRol(Rol.CLIENTE);

        // Se guarda en la base de datos
        return usuarioRepository.save(usuario);
    }

    // Crea el usuario Empleado
    public Usuario crearUsuarioEmpleado(RegistroUsuarioDato registroUsuarioDato, Rol rol) {
    Usuario usuario = new Usuario();
    usuario.setNombre(registroUsuarioDato.getNombre());
    usuario.setApellido(registroUsuarioDato.getApellido());
    usuario.setEmail(registroUsuarioDato.getEmail());
    usuario.setPassword(passwordEncoder.encode(registroUsuarioDato.getPassword())); // Encripta la contraseña
    usuario.setRol(rol); // Asigna el rol de empleado
    
        // Se guarda en la base de datos
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setApellido(usuarioActualizado.getApellido());
            usuario.setTelefono(usuarioActualizado.getTelefono());
            usuario.setDireccion(usuarioActualizado.getDireccion());
            // No actualizar email ni password aquí por seguridad
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
}
