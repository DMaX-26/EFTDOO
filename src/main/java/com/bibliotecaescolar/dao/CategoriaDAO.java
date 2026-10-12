package com.bibliotecaescolar.dao;

import com.bibliotecaescolar.conexion.DatabaseConnection;
import com.bibliotecaescolar.modelo.Categoria;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private final DatabaseConnection conexion;

    public CategoriaDAO() {
        this.conexion = DatabaseConnection.getInstancia();
    }

    /**
     * Metodo para crear una Categoria en la BD
     */
    public void create(Categoria categoria) throws SQLException {
        /**
         * Instrucción sql que crea una Categoria en la tabla "categorias" de la BD
         */
        String sql = "INSERT INTO categorias(nombre) VALUES (?)";

        try(PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {
            /**
             * Se obtiene el nombre de la categoria en el sistema y se asigna una posición al nombre en la BD
             */
            statement.setString(1, categoria.getNombre());

            /**
             * Se ejecuta la instrucción y se crea la categoria en la BD
             */
            statement.executeUpdate();
        }
    }

    /**
     * Metodo para leer todas las categorias registradas en la BD
     */
    public List<Categoria> readAll() throws SQLException {
        /**
         * Lista que almacena las categorias
         */
        List<Categoria> categorias = new ArrayList<>();

        /**
         * Instrucción sql para obtener todas las categorias registradas
         */
        String sql  = "SELECT * FROM categorias";

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
                 * Se obtiene id y nombre de la tabla categorias
                 */
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");

                /**
                 * Se crea una Categoria
                 */
                Categoria categoria = new Categoria(id, nombre);

                /**
                 * Se agrega la categoria creada a la lista
                 */
                categorias.add(categoria);
            }
        }
        return categorias;
    }

    /**
     * Metodo que busca una categoria en la BD por su id
     * @param idCategoria
     * @return
     * @throws SQLException
     */
    public Categoria buscarPorId(int idCategoria) throws SQLException {
        /**
         * Instrucción sql que obtiene categorias según el "id" indicado
         */
        String sql = "SELECT * FROM categorias WHERE id = ?";

        try (PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {

            /**
             * Se obtiene el idCategoria y se posiciona en el "?".
             */
            statement.setInt(1, idCategoria);

            /**
             * Se ejecuta la instrucción sql
             */
            try (ResultSet rs = statement.executeQuery()) {
                /**
                 * Si existen datos
                 */
                if (rs.next()) {
                    /**
                     * Se obtienen id y nombre de la tabla
                     */
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");

                    /**
                     * Se crea una Categoria y se retorna
                     */
                    return new Categoria(id, nombre);
                }
            }
        }
        return null;
    }

    /**
     * Metodo para modificar una Categoria
     */
    public void update(Categoria categoria) throws SQLException {
        /**
         * Instrucción que actualiza los datos de la tabla categorias según el "id" indicado
         */
        String sql = "UPDATE categorias SET nombre = ? WHERE id = ?";

        try(PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {

            /**
             * Se obtiene el nombre y id, y se les asigna una posición en la tabla
             */
            statement.setString(1, categoria.getNombre());
            statement.setInt(2, categoria.getId());

            /**
             * Se ejecuta la instrucción y se actualiza la categoria en la BD
             */
            statement.executeUpdate();
        }
    }


    /**
     * Metodo para eliminar una Categoria
     */
    public void delete(int idCategoria) throws SQLException {
        /**
         * Instrucción que elimina una Categoria de la tabla según su id
         */
        String sql = "DELETE FROM categorias WHERE id = ?";

        try(PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {

            /**
             * Se le asigna la posición a idCategoria
             */
            statement.setInt(1, idCategoria);

            /**
             * Se ejecuta la instrucción y se elimina la Categoria de la BD
             */
            statement.executeUpdate();
        }
    }
}
