package com.bibliotecaescolar.controlador;

import com.bibliotecaescolar.dao.PrestamoDAO;
import com.bibliotecaescolar.modelo.Prestamo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ControladorPrestamos {

    private final PrestamoDAO prestamoDAO;

    public ControladorPrestamos(PrestamoDAO prestamoDAO) {
        this.prestamoDAO = prestamoDAO;
    }

    public List<Prestamo> listarPrestamos() throws SQLException {
        return prestamoDAO.readAll();
    }

    public void agregarPrestamo(Prestamo prestamo) throws SQLException {
        prestamoDAO.create(prestamo);
    }

    public void modificarPrestamo(Prestamo prestamo) throws SQLException {
        prestamoDAO.update(prestamo);
    }

    public void eliminarPrestamo(int id) throws SQLException {
        prestamoDAO.delete(id);
    }

    public Prestamo buscarPrestamo(int id) throws SQLException {
        return prestamoDAO.buscarPorId(id);
    }

    /**
     * Metodo para obtener los libros más prestados
     * @return
     * @throws SQLException
     */
    public ResultSet librosMasPrestados() throws SQLException {
        /**
         * Le pide al DAO que obtenga los libros mas prestados de la BD
         */
        return prestamoDAO.librosMasPrestados();
    }
}
