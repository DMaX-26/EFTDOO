package com.bibliotecaescolar.dao;

import com.bibliotecaescolar.conexion.DatabaseConnection;
import com.bibliotecaescolar.modelo.Usuario;
import com.bibliotecaescolar.modelo.Rol;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    /**
     * Referencia a la conexión con la BD
     */
    private final DatabaseConnection conexion;

    /**
     * Constructor que inicializa la conexión con la BD
     */
    public UsuarioDAO() {
        this.conexion = DatabaseConnection.getInstancia();
    }

    public Usuario autenticarUsuario(String correo, String contrasena) throws SQLException {
        /**
         * Instrucción sql que obtiene de la tabla "usuarios" el correo y contraseña indicados.
         * Se usan marcadores de posición para correo y contraseña
         */
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND contraseña = ?";
        /**
         * Se obtiene la conexión con la BD y se prepara la instrucción para ser ejecutada
         */
        try(PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {
            /**
             * Se asigna una posición al correo y contrasena
             */
            statement.setString(1, correo);
            statement.setString(2, contrasena);
            /**
             * Se ejecuta la instrucción sql y se guarda en rs
             */
            ResultSet rs = statement.executeQuery();

            /**
             * Si existen datos
             */
            if (rs.next()) {
                return new Usuario(
                        /**
                         * Se obtienen los datos
                         */
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("rut"),
                        rs.getString("correo"),
                        rs.getString("contraseña"),
                        Rol.valueOf(rs.getString("rol").toUpperCase())
                );
            }
        }
        return null;
    }

    /**
     * Metodo para crear un Usuario en la BD
     */
    public void create(Usuario usuario) throws SQLException{
        /**
         * Instrucción sql que crea un Usuario en la tabla "usuarios" de la BD
         */
        String sql = "INSERT INTO usuarios(nombre, rut, correo, contraseña, rol) VALUES (?, ?, ?, ?, ?)";

        try(PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {
            /**
             * Se obtienen los datos del sistema y se asigna una posición al nombre, rut, correo, contrasena y rol del usuario en la BD
             */
            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getRut());
            statement.setString(3, usuario.getCorreo());
            statement.setString(4, usuario.getContrasena());
            statement.setString(5, usuario.getRol().name().toLowerCase()); // Se obtiene el nombre del rol del Usuario en minúsculas (BIBLIOTECARIO, ESTUDIANTE)

            /**
             * Se ejecuta la instrucción y se crea el usuario en la BD
             */
            statement.executeUpdate();
        }
    }

    /**
     * Metodo para leer todos los usuarios registrados en la BD
     */
    public List<Usuario> readAll() throws SQLException{
        /**
         * Se crea una lista de usuarios
         */
        List<Usuario> usuarios = new ArrayList<>();
        /**
         * Instrucción sql para obtener todos los usuarios registrados
         */
        String sql  = "SELECT * FROM usuarios";

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
                 * Se obtienen todos los datos de la tabla usuarios en la BD
                 */
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String rut =  rs.getString("rut");
                String correo =  rs.getString("correo");
                String contrasena =  rs.getString("contraseña");
                Rol rol = Rol.valueOf(rs.getString("rol").toUpperCase()); // Se obtiene rol de la BD como String en mayúsculas y se convierte a ENUM

                /**
                 * Se crea un usuario
                 */
                Usuario usuario = new Usuario(id, nombre, rut, correo, contrasena, rol);
                /**
                 * Se agrega a la lista
                 */
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }

    /**
     * Metodo para modificar un Usuario
     */
    public void update(Usuario usuario) throws SQLException{
        /**
         * Instrucción que actualiza los datos de la tabla usuarios según el "id" indicado
         */
        String sql = "UPDATE usuarios SET nombre = ?, rut = ?, correo = ?, contraseña = ?, rol = ? WHERE id = ?";

        try(PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {

            /**
             * Se obtienen los datos y se asigna una posición a cada dato en la tabla
             */
            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getRut());
            statement.setString(3, usuario.getCorreo());
            statement.setString(4, usuario.getContrasena());
            statement.setString(5, usuario.getRol().name().toLowerCase()); // Se obtiene rol como nombre en minúsculas
            statement.setInt(6, usuario.getId());

            /**
             * Se ejecuta la instrucción y se actualiza el usuario en la BD
             */
            statement.executeUpdate();
        }
    }

    /**
     * Metodo para eliminar un Usuario de la BD
     * Recibe un "id"
     */
    public void delete(int idUsuario) throws SQLException{
        /**
         * Instrucción que elimina un Usuario de la tabla según su id
         */
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try(PreparedStatement statement = conexion.obtenerConexion().prepareStatement(sql)) {

            /**
             * Se le asigna la posición a idUsuario
             */
            statement.setInt(1, idUsuario);

            /**
             * Se ejecuta la instrucción y se elimina el usuario de la BD
             */
            statement.executeUpdate();
        }
    }
}
