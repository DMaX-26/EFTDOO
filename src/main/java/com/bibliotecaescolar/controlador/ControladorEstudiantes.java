package com.bibliotecaescolar.controlador;

import com.bibliotecaescolar.dao.EstudianteDAO;
import com.bibliotecaescolar.modelo.Estudiante;

import java.sql.SQLException;
import java.util.List;

public class ControladorEstudiantes {
    private final EstudianteDAO estudianteDAO;

    public ControladorEstudiantes(EstudianteDAO estudianteDAO) {
        this.estudianteDAO = estudianteDAO;
    }

    /**
     * Metodo que lista todos los estudiantes de la BD.
     * @return
     * @throws SQLException
     */
    public List<Estudiante> listarEstudiantes() throws SQLException {
        /**
         * Delega el trabajo de listar todos los estudiantes al DAO mediante el metodo "readAll"
         */
        return estudianteDAO.readAll();
    }

    /**
     * Metodo para agregar un estudiante.
     */
    public void agregarEstudiante(Estudiante estudiante) throws SQLException {
        /**
         * Delega el trabajo de crear todos los estudiantes al DAO mediante el metodo "create"
         */
        estudianteDAO.create(estudiante);
    }

    /**
     * Metodo para modificar un estudiante.
     */
    public void modificarEstudiante(Estudiante estudiante) throws SQLException {
        /**
         * Delega el trabajo de modificar todos los estudiantes al DAO mediante el metodo "update"
         */
        estudianteDAO.update(estudiante);
    }

    /**
     * Metodo para eliminar un estudiante.
     */
    public void eliminarEstudiante(int id) throws SQLException {
        /**
         * Delega el trabajo de eliminar todos los estudiantes al DAO mediante el metodo "delete"
         */
        estudianteDAO.delete(id);
    }
}
