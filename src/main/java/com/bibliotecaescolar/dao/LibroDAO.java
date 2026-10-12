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

public class LibroDAO {
    private final DatabaseConnection conexion;
    private final CategoriaDAO categoriaDAO;

    /**
     * Constructor que inicializa conexión y categoriaDAO
     */
    public LibroDAO() {
        this.conexion = DatabaseConnection.getInstancia();
        this.categoriaDAO = new CategoriaDAO();
    }

    /**
     * Metodo para crear un Libro en la BD
     * Recibe un libro
     */
    public void create(Libro libro) throws SQLException {
        /**
         * Instrucción sql que crea un Libro en la tabla "libros" de la BD
         */
        String sql = "INSERT INTO libros(titulo, autor, isbn, editorial, stock, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {
            /**
             * Se obtienen los datos del sistema y se asigna una posición al titulo, autor, isbn, editorial y stock del libro en la BD
             */
            statement.setString(1, libro.getTitulo());
            statement.setString(2, libro.getAutor());
            statement.setString(3, libro.getIsbn());
            statement.setString(4, libro.getEditorial());
            statement.setInt(5, libro.getStock());
            statement.setInt(6, libro.getCategoria().getId()); // Se obtiene el "id" de la categoria desde libro

            /**
             * Se ejecuta la instrucción y se crea el libro en la BD
             */
            statement.executeUpdate();
        }
    }

    /**
     * Metodo para leer todos los libros registrados
     */
    public List<Libro> readAll() throws SQLException{
        /**
         * Se crea una lista que guardará todos los libros
         */
        List<Libro> libros = new ArrayList<>();
        /**
         * Instrucción sql para obtener todos los libros registrados
         */
        String sql  = "SELECT * FROM libros";

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
                 * Se obtienen todos los datos de la tabla libros en la BD
                 */
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autor =  rs.getString("autor");
                String isbn =  rs.getString("isbn");
                String editorial =  rs.getString("editorial");
                int stock =  rs.getInt("stock");

                int idCategoria = rs.getInt("id_categoria"); // Se obtiene id_categoria como int
                Categoria categoria = categoriaDAO.buscarPorId(idCategoria); // Le pasamos el idCategoria al DAO para que busque esa categoria

                /**
                 * Se crea un libro
                 */
                Libro libro = new Libro(id, titulo, autor, isbn, editorial, stock, categoria);
                /**
                 * Se agrega a la lista
                 */
                libros.add(libro);
            }
        }
        return libros;
    }

    /**
     * Metodo que busca un libro por su id en la BD
     * @param idLibro
     * @return
     * @throws SQLException
     */
    public Libro buscarPorId(int idLibro) throws SQLException {
        /**
         * Instrucción sql que obtiene libros según el "id" indicado
         */
        String sql = "SELECT * FROM libros WHERE id = ?";

        try (PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {

            /**
             * Se obtiene el idLibro y se posiciona en el "?".
             */
            statement.setInt(1, idLibro);

            /**
             * Se ejecuta la instrucción sql
             */
            try (ResultSet rs = statement.executeQuery()) {
                /**
                 * Si existen datos
                 */
                if (rs.next()) {
                    /**
                     * Se obtienen los datos de la tabla
                     */
                    int id = rs.getInt("id");
                    String titulo = rs.getString("titulo");
                    String autor = rs.getString("autor");
                    String isbn = rs.getString("isbn");
                    String editorial = rs.getString("editorial");
                    int stock = rs.getInt("stock");
                    int idCategoria = rs.getInt("id_categoria");
                    Categoria categoria = categoriaDAO.buscarPorId(idCategoria);

                    /**
                     * Se crea un Libro y se retorna
                     */
                    return new Libro(id, titulo, autor, isbn, editorial, stock, categoria);
                }
            }
        }
        return null;
    }

    /**
     * Metodo para modificar un Libro de la BD
     */
    public void update(Libro libro) throws SQLException {
        /**
         * Instrucción que actualiza los datos de la tabla libros según el "id" indicado
         */
        String sql = "UPDATE libros SET titulo = ?, autor = ? , isbn = ?, editorial = ?, stock = ?, id_categoria = ? WHERE id = ?";

        try(PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {

            /**
             * Se obtienen los datos del libro y se les asigna una posición en la tabla
             */
            statement.setString(1, libro.getTitulo());
            statement.setString(2, libro.getAutor());
            statement.setString(3, libro.getIsbn());
            statement.setString(4, libro.getEditorial());
            statement.setInt(5, libro.getStock());
            statement.setInt(6, libro.getCategoria().getId()); // De la categoria se obtiene el id
            statement.setInt(7, libro.getId()); // id del libro a modificar

            /**
             * Se ejecuta la instrucción y se actualiza el libro en la BD
             */
            statement.executeUpdate();
        }
    }

    /**
     * Metodo para eliminar un Libro de la BD
     */
    public void delete(int idLibro) throws SQLException {
        /**
         * Instrucción que elimina un Libro de la tabla según su id
         */
        String sql = "DELETE FROM libros WHERE id = ?";

        try(PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {

            /**
             * Se le asigna la posición a idLibro
             */
            statement.setInt(1, idLibro);

            /**
             * Se ejecuta la instrucción y se elimina el Librode la BD
             */
            statement.executeUpdate();
        }
    }
}
