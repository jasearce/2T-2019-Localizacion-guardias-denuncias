package com.sectrack.a2t2019_localizacion_de_guardias_para_denuncias;

public class Usuario {

    private String nombre;
    private String apellidos;
    private String email;
    private String tipoDeCuenta;
    private String telefono;
    private String foto;

    public Usuario() {
    }

    public Usuario(String nombre, String apellidos, String email, String tipoDeCuenta, String telefono) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.tipoDeCuenta = tipoDeCuenta;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoDeCuenta() {
        return tipoDeCuenta;
    }

    public void setTipoDeCuenta(String tipoDeCuenta) {
        this.tipoDeCuenta = tipoDeCuenta;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
