package com.bibliotecaescolar.modelo;

public class Categoria {
    private int id;
    private String nombre;

    /**
     * Constructor
     * @param id
     * @param nombre
     */
    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Constructor solo con el nombre
     * @param nombre
     */
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "ID: "+id+" | Nombre: "+nombre;
    }
}
