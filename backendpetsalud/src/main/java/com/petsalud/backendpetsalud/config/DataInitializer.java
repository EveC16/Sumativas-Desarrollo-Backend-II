package com.petsalud.backendpetsalud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.petsalud.backendpetsalud.entity.Rol;
import com.petsalud.backendpetsalud.entity.Usuario;
import com.petsalud.backendpetsalud.repository.UsuarioRepository;

// Crea el usuario Gerente para registrar empleados
@Component 
public class DataInitializer implements CommandLineRunner{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (usuarioRepository.findByEmail("gerente@petsalud.com").isEmpty()) {
            Usuario gerente = new Usuario();
            gerente.setNombre("Gerente");
            gerente.setApellido("Principal");
            gerente.setEmail("gerente@petsalud.com");
            gerente.setPassword(passwordEncoder.encode("gerente123")); // Contraseña cifrada con BCrypt
            gerente.setRol(Rol.ADMIN); // Asigna el rol maestro
            
            usuarioRepository.save(gerente); //ADMIN
            System.out.println("Gerente creado con exito.");
        }
    }

}
