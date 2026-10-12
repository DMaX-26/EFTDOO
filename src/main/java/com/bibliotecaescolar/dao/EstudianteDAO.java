package com.bibliotecaescolar.dao;

import com.bibliotecaescolar.conexion.DatabaseConnection;
import com.bibliotecaescolar.modelo.Categoria;
import com.bibliotecaescolar.modelo.Estudiante;
import com.bibliotecaescolar.modelo.Libro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {
    private final DatabaseConnection conexion;

    public EstudianteDAO() {
        this.conexion = DatabaseConnection.getInstancia();
    }

    /**
     * Metodo para crear un Estudiante en la BD
     */
    public void create(Estudiante estudiante) throws SQLException{
        /**
         * Instrucción sql que crea un Estudiante en la tabla "estudiantes" de la BD
         */
        String sql = "INSERT INTO estudiantes(nombre, rut, curso, correo) VALUES (?, ?, ?, ?)";

        try(PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {
            /**
             * Se obtienen los datos del sistema y se asigna una posición al nombre, rut, curso y correo del Estudiante en la BD
             */
            statement.setString(1, estudiante.getNombre());
            statement.setString(2, estudiante.getRut());
            statement.setString(3, estudiante.getCurso());
            statement.setString(4, estudiante.getCorreo());

            /**
             * Se ejecuta la instrucción y se crea el Estudiante en la BD
             */
            statement.executeUpdate();
        }
    }

    /**
     * Metodo para leer todos los estudiantes registrados en la BD
     */
    public List<Estudiante> readAll() throws SQLException{
        /**
         * Se crea una lista que guardará todos los Estudiantes
         */
        List<Estudiante> estudiantes = new ArrayList<>();

        /**
         * Instrucción sql para obtener todos los estudiantes registrados
         */
        String sql  = "SELECT * FROM estudiantes";

        /**
         * Se obtiene la conexión con la BD, se prepara la ejecución de sql, se ejecuta la instrucción
         * Cierra todo de forma automática al final
         */
        try (PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql);
             /**
              * Se ejecuta la instrucción sql y se guarda en rs
              */
             ResultSet rs = statement.executeQuery()) {

            /**
             * Mientras existan datos
             */
            while (rs.next()) {
                /**
                 * Se obtienen todos los datos de la tabla estudiantes en la BD
                 */
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String rut =  rs.getString("rut");
                String curso =  rs.getString("curso");
                String correo =  rs.getString("correo");

                /**
                 * Se crea un Estudiante
                 */
                Estudiante estudiante = new Estudiante(id, nombre, rut, curso, correo);
                /**
                 * Se agrega estudiante a la lista estudiantes
                 */
                estudiantes.add(estudiante);
            }
        }
        return estudiantes;
    }

    /**
     * Metodo que busca un estudiante por su id en la BD
     * @param idEstudiante
     * @return
     * @throws SQLException
     */
    public Estudiante buscarPorId(int idEstudiante) throws SQLException {
        /**
         * Instrucción sql que obtiene estudiantes según el "id" indicado
         */
        String sql = "SELECT * FROM estudiantes WHERE id = ?";

        try (PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {

            /**
             * Se obtiene el idEstudiante y se posiciona en el "?".
             */
            statement.setInt(1, idEstudiante);

            /**
             * Se ejecuta la instrucción sql
             */
            try (ResultSet rs = statement.executeQuery()) {
                /**
                 * Si existen datos
                 */
                if (rs.next()) {
                    /**
                     * Se obtienen id, nombre, rut, curso y correo de la tabla
                     */
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String rut = rs.getString("rut");
                    String curso = rs.getString("curso");
                    String correo = rs.getString("correo");

                    /**
                     * Se crea un Estudiante y se retorna
                     */
                    return new Estudiante(id, nombre, rut, curso, correo);
                }
            }
        }
        return null;
    }

    /**
     * Metodo para modificar un Estudiante
     */
    public void update(Estudiante estudiante) throws SQLException{
        /**
         * Instrucción que actualiza los datos de la tabla estudiantes según el "id" indicado
         */
        String sql = "UPDATE estudiantes SET nombre = ?, rut = ? , curso = ?, correo = ? WHERE id = ?";

        try(PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {

            /**
             * Se obtienen los datos del Estudiante y se les asigna una posición en la tabla
             */
            statement.setString(1, estudiante.getNombre());
            statement.setString(2, estudiante.getRut());
            statement.setString(3, estudiante.getCurso());
            statement.setString(4, estudiante.getCorreo());
            statement.setInt(5, estudiante.getId()); // id del estudiante a modificar

            /**
             * Se ejecuta la instrucción y se actualiza el libro en la BD
             */
            statement.executeUpdate();
        }
    }

    /**
     * Metodo para eliminar un Estudiante
     * Recibe un int idEstudiante
     */
    public void delete(int idEstudiante) throws SQLException{
        /**
         * Instrucción que elimina un Estudiante de la tabla según su id
         */
        String sql = "DELETE FROM estudiantes WHERE id = ?";

        try(PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {

            /**
             * Se le asigna la posición a idEstudiante
             */
            statement.setInt(1, idEstudiante);

            /**
             * Se ejecuta la instrucción y se elimina el Estudiante de la BD
             */
            statement.executeUpdate();
        }
    }
}
