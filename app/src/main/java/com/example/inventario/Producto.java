package com.example.inventario;

public class Producto {
    private String nombre;
    private String unidades;
    private String grupo;

    public Producto(String nombre, String unidades, String grupo) {
        this.nombre = nombre;
        this.unidades = unidades;
        this.grupo = grupo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}
