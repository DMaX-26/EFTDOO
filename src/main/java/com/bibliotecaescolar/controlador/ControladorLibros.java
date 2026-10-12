package com.bibliotecaescolar.controlador;

import com.bibliotecaescolar.dao.LibroDAO;
import com.bibliotecaescolar.modelo.Libro;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador que recibe las solicitudes desde la interfaz y delega operaciones al DAO
 */
public class ControladorLibros {
    private final LibroDAO libroDAO;

    public ControladorLibros(LibroDAO libroDAO) {
        this.libroDAO = libroDAO;
    }

    /**
     * Metodo que lista todos los libros obtenidos de la BD.
     * @return
     * @throws SQLException
     */
    public List<Libro> listarLibros() throws SQLException {
        /**
         * Delega el trabajo de listar todos los libros al DAO mediante el metodo "readAll"
         */
        return libroDAO.readAll();
    }

    /**
     * Metodo que agrega un libro a la BD
     * @param libro
     * @throws SQLException
     */
    public void agregarLibro(Libro libro) throws SQLException {
        /**
         * Delega el trabajo de agregar libros al DAO mediante el metodo "create"
         */
        libroDAO.create(libro);
    }

    /**
     * Metodo que modifica un libro en la BD
     * @param libro
     * @throws SQLException
     */
    public void modificarLibro(Libro libro) throws SQLException {
        /**
         * Delega el trabajo al DAO
         */
        libroDAO.update(libro);
    }

    /**
     * Metodo que elimina un libro de la BD
     * @param id
     * @throws SQLException
     */
    public void eliminarLibro(int id) throws SQLException {
        /**
         * Delega el trabajo al DAO
         */
        libroDAO.delete(id);
    }
}
