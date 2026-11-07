package com.petsalud.backendpetsalud.dto;

import com.petsalud.backendpetsalud.entity.Rol;

import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;

// Controla los campos que se le solicitan al usuario para evitar que puedan alterar datos sensibles como el rol o id.
public class RegistroUsuarioDato {

    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;

    public RegistroUsuarioDato() {
    }

    public RegistroUsuarioDato(String email, String password, String nombre, String apellido, String telefono,
        String direccion) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
}
