package com.bibliotecaescolar.dao;

import com.bibliotecaescolar.conexion.DatabaseConnection;
import com.bibliotecaescolar.modelo.Categoria;
import com.bibliotecaescolar.modelo.Estudiante;
import com.bibliotecaescolar.modelo.Libro;
import com.bibliotecaescolar.modelo.Prestamo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    private final DatabaseConnection conexion;
    /**
     * Referencia a los DAO para acceder a sus métodos
     */
    private final EstudianteDAO estudianteDAO;
    private final LibroDAO libroDAO;

    /**
     * Constructor que inicializa la conexión, estudianteDAO y libroDAO
     */
    public PrestamoDAO(LibroDAO libroDAO) {
        this.conexion = DatabaseConnection.getInstancia();
        this.estudianteDAO = new EstudianteDAO();
        this.libroDAO = libroDAO;
    }

    /**
     * Metodo para crear un Prestamo
     */
    /**
     * Metodo para crear un Prestamo
     */
    public void create(Prestamo prestamo) throws SQLException {
        /**
         * Instrucción sql que crea un Prestamo en la tabla "prestamos" de la BD
         */
        String sql = "INSERT INTO prestamos(id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto) VALUES (?, ?, ?, ?, ?)";

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {
            /**
             * Se obtienen los datos del sistema y se asigna una posición a cada dato
             */
            statement.setInt(1, prestamo.getEstudiante().getId());
            statement.setInt(2, prestamo.getLibro().getId());

            LocalDate fechaPrestamo = LocalDate.parse(prestamo.getFechaPrestamo(), formato);
            LocalDate fechaDevolucion = LocalDate.parse(prestamo.getFechaDevolucion(), formato);

            statement.setDate(3, java.sql.Date.valueOf(fechaPrestamo));
            statement.setDate(4, java.sql.Date.valueOf(fechaDevolucion));
            statement.setBoolean(5, prestamo.isDevuelto());

            /**
             * Se ejecuta la instrucción y se crea el prestamo en la BD
             */
            statement.executeUpdate();
        }
    }

    /**
     * Metodo para leer todos los prestamos registrados
     */
    public List<Prestamo> readAll() throws SQLException{
        /**
         * Lista que almacena los prestamos
         */
        List<Prestamo> prestamos = new ArrayList<>();

        /**
         * Instrucción sql para obtener todos los prestamos registradas
         */
        String sql  = "SELECT * FROM prestamos";

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
                 * Se obtiene id y nombre de la tabla prestamos
                 */
                int id = rs.getInt("id");

                int idEstudiante = rs.getInt("id_estudiante"); //Se obtiene idEstudiante como int
                Estudiante estudiante = estudianteDAO.buscarPorId(idEstudiante); // Le pasamos el idEstudiante al DAO para que busque ese estudiante en la BD
                int idLibro = rs.getInt("id_libro"); //Se obtiene idLibro como int
                Libro libro = libroDAO.buscarPorId(idLibro); // Le pasamos el idLibro al DAO para que busque ese libro en la BD (Se guarda en una variable de tipo Libro)

                String fechaPrestamo = rs.getString("fecha_prestamo");
                String fechaDevolucion = rs.getString("fecha_devolucion");
                boolean devuelto = rs.getBoolean("devuelto");

                /**
                 * Se crea un Prestamo con los datos obtenidos en el paso anterior
                 */
                Prestamo prestamo = new Prestamo(id, estudiante, libro, fechaPrestamo, fechaDevolucion, devuelto);

                /**
                 * Se agrega la categoria creada a la lista
                 */
                prestamos.add(prestamo);
            }
        }
        return prestamos;
    }

    /**
     * Metodo que busca un prestamo por su id en la BD
     * @param idPrestamo
     * @return
     * @throws SQLException
     */
    public Prestamo buscarPorId(int idPrestamo) throws SQLException {
        /**
         * Instrucción sql que obtiene prestamos según el "id" indicado
         */
        String sql = "SELECT * FROM prestamos WHERE id = ?";

        try (PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {

            /**
             * Se obtiene el idEstudiante y se posiciona en el "?".
             */
            statement.setInt(1, idPrestamo);

            /**
             * Se ejecuta la instrucción sql
             */
            try (ResultSet rs = statement.executeQuery()) {
                /**
                 * Si existen datos
                 */
                if (rs.next()) {
                    /**
                     * Se obtienen id, id_estudiante, id_libro, fecha_prestamo, fecha_devolucion y devuelto de la tabla
                     */
                    int id = rs.getInt("id");

                    int idEstudiante = rs.getInt("id_estudiante"); // Se obtiene id_estudiante como int
                    Estudiante estudiante = estudianteDAO.buscarPorId(idEstudiante); // Le pasamos el idEstudiante al DAO para que busque ese estudiante en la BD
                    int idLibro = rs.getInt("id_libro"); // Se obtiene id_libro como int
                    Libro libro = libroDAO.buscarPorId(idLibro); // Le pasamos el idLibro al DAO para que busque ese libro en la BD

                    String fechaPrestamo = rs.getString("fecha_prestamo");
                    String fechaDevolucion = rs.getString("fecha_devolucion");
                    boolean devuelto = rs.getBoolean("devuelto");

                    /**
                     * Se crea un Prestamo y se retorna
                     */
                    return new Prestamo(id, estudiante, libro, fechaPrestamo, fechaDevolucion, devuelto);
                }
            }
        }
        return null;
    }

    /**
     * Metodo para modificar un Prestamo
     */
    /**
     * Metodo para modificar un Prestamo
     */
    public void update(Prestamo prestamo) throws SQLException {
        /**
         * Instrucción que actualiza los datos de la tabla prestamos según el "id" indicado
         */
        String sql = "UPDATE prestamos SET id_estudiante = ?, id_libro = ?, fecha_prestamo = ?, fecha_devolucion = ?, devuelto = ? WHERE id = ?";

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {
            /**
             * Se obtienen los datos del prestamo y se les asigna una posición en la tabla
             */
            statement.setInt(1, prestamo.getEstudiante().getId());
            statement.setInt(2, prestamo.getLibro().getId());

            LocalDate fechaPrestamo;
            LocalDate fechaDevolucion;

            if (prestamo.getFechaPrestamo().contains("-") &&
                    prestamo.getFechaPrestamo().indexOf('-') == 4) {
                fechaPrestamo = LocalDate.parse(prestamo.getFechaPrestamo());
            } else {
                fechaPrestamo = LocalDate.parse(prestamo.getFechaPrestamo(), formato);
            }

            if (prestamo.getFechaDevolucion().contains("-") &&
                    prestamo.getFechaDevolucion().indexOf('-') == 4) {
                fechaDevolucion = LocalDate.parse(prestamo.getFechaDevolucion());
            } else {
                fechaDevolucion = LocalDate.parse(prestamo.getFechaDevolucion(), formato);
            }

            statement.setDate(3, java.sql.Date.valueOf(fechaPrestamo));
            statement.setDate(4, java.sql.Date.valueOf(fechaDevolucion));
            statement.setBoolean(5, prestamo.isDevuelto());
            statement.setInt(6, prestamo.getId());

            /**
             * Se ejecuta la instrucción y se actualiza el prestamo en la BD
             */
            statement.executeUpdate();
        }
    }

    /**
     * Metodo para eliminar un Prestamo de la BD
     */
    public void delete(int idPrestamo) throws SQLException{
        /**
         * Instrucción que elimina un Prestamo de la tabla según su id
         */
        String sql = "DELETE FROM prestamos WHERE id = ?";

        try(PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {

            /**
             * Se le asigna la posición a idPrestamo
             */
            statement.setInt(1, idPrestamo);

            /**
             * Se ejecuta la instrucción y se elimina el Prestamo de la BD
             */
            statement.executeUpdate();
        }
    }

    /**
     * Metodo que muestra los libros mas prestados.
     */
    public ResultSet librosMasPrestados() throws SQLException {

        String sql = "SELECT l.id, l.titulo, l.autor, COUNT(p.id) AS total " + "FROM libros l " + "INNER JOIN prestamos p ON l.id = p.id_libro " + "GROUP BY l.id, l.titulo, l.autor " +
                "ORDER BY total DESC";

        PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql);

        return statement.executeQuery();
    }
}
