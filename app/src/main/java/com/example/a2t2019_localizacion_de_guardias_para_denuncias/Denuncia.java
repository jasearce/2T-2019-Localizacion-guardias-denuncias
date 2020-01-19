package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import java.util.Date;

public class Denuncia {

    private Usuario usuario;
    private String descripcion;
    private Date fecha;

    public Denuncia(){
    }

    public Denuncia(Usuario usuario, String descripcion, Date fecha) {
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
