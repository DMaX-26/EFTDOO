package com.bibliotecaescolar.modelo;

/**
 * Clase que representa a un usuario del sistema
 */
public class Usuario {
    protected int id;
    protected String nombre;
    protected String rut;
    protected String correo;
    protected String contrasena;
    protected Rol rol;

    /**
     * Constructor
     * @param id
     * @param nombre
     * @param rut
     * @param correo
     * @param contrasena
     * @param rol
     */
    public Usuario(int id, String nombre, String rut, String correo, String contrasena, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.rut = rut;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    /**
     * Constructor para la interfaz
     * @param nombre
     * @param rut
     * @param correo
     * @param contrasena
     * @param rol
     */
    public Usuario(String nombre, String rut, String correo, String contrasena, Rol rol) {
        this.nombre = nombre;
        this.rut = rut;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    /**
     * Metodos getter and setter
     * @return
     */
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

    public String getRut() {
        return rut;
    }
    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Representación textual
     * @return
     */
    @Override
    public String toString() {
        return "ID Usuario: "+id+" | Nombre: "+nombre+" | Rut: "+rut+" | Correo: "+correo+" | Contraseña: "+contrasena+" | rol: "+rol;
    }
}
