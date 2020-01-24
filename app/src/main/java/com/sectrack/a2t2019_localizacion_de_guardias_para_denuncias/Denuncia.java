package com.sectrack.a2t2019_localizacion_de_guardias_para_denuncias;

public class Denuncia {
    private String id;
    private String tipo_delito;
    private String nombre;
    private String fecha;
    private double latitud;
    private double longitud;
    private String area;
    private String descripcion;
    private String estado;

    public Denuncia(){

    }

    public Denuncia(String id,String tipo_delito, String nombre, String fecha, double latitud, double longitud, String area, String descripcion,String estado) {
        this.id = id;
        this.tipo_delito=tipo_delito;
        this.nombre = nombre;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
        this.area = area;
        this.descripcion = descripcion;
        this.estado=estado;
    }

    public String getId() {
        return id;
    }

    public String getTipo_delito() {
        return tipo_delito;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getArea() {
        return area;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEstado() { return estado; }
}
