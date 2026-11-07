package com.petsalud.backendpetsalud.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.petsalud.backendpetsalud.dto.RegistroUsuarioDato;
import com.petsalud.backendpetsalud.entity.Rol;
import com.petsalud.backendpetsalud.entity.Usuario;
import com.petsalud.backendpetsalud.service.UsuarioService;

@RestController
@RequestMapping("/api/gerente")
public class GerenteController {

    private UsuarioService usuarioService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/registrar")
        public ResponseEntity<Usuario> crearEmpleado(@RequestBody RegistroUsuarioDato registroUsuarioDato) {
        
        Usuario nuevoEmpleado = usuarioService.crearUsuarioE(registroUsuarioDato, Rol.VETERINARIO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEmpleado);
    }

}
