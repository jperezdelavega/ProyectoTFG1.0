package com.example.inventario;

public class Producto {
    private String nombre;
    private String unidades;
    private String grupo;
    private String unidadeLimite;
    private String cod_barras;
    private int imagenDraw;



    public Producto(String nombre, String unidades, String grupo, String unidadeLimite, String cod_barras) {
        this.nombre = nombre;
        this.unidades = unidades;
        this.grupo = grupo;
        this.unidadeLimite = unidadeLimite;
        this.cod_barras = cod_barras;
    }
public Producto (){

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

    public String getUnidadeLimite() {
        return unidadeLimite;
    }

    public void setUnidadeLimite(String unidadeLimite) {
        this.unidadeLimite = unidadeLimite;
    }

    public String getCod_barras() {
        return cod_barras;
    }

    public void setCod_barras(String cod_barras) {
        this.cod_barras = cod_barras;
    }

    public int getImagenDraw() {
        return imagenDraw;
    }

    public void setImagenDraw(int imagenDraw) {
        this.imagenDraw = imagenDraw;
    }

}
