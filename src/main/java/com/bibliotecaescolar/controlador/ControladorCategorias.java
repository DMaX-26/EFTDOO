package com.bibliotecaescolar.controlador;

import com.bibliotecaescolar.dao.CategoriaDAO;
import com.bibliotecaescolar.modelo.Categoria;

import java.sql.SQLException;
import java.util.List;

public class ControladorCategorias {
    private final CategoriaDAO categoriaDAO;

    public ControladorCategorias(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    public List<Categoria> listarCategorias() throws SQLException {
        return categoriaDAO.readAll();
    }

    public void agregarCategoria(Categoria categoria) throws SQLException {
        categoriaDAO.create(categoria);
    }

    public void modificarCategoria(Categoria categoria) throws SQLException {
        categoriaDAO.update(categoria);
    }

    public void eliminarCategoria(int id) throws SQLException {
        categoriaDAO.delete(id);
    }
}
