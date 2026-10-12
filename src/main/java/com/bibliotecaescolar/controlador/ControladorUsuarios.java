package com.bibliotecaescolar.controlador;

import com.bibliotecaescolar.dao.UsuarioDAO;
import com.bibliotecaescolar.modelo.Usuario;

import java.sql.SQLException;

public class ControladorUsuarios {
    private UsuarioDAO usuarioDAO;

    public ControladorUsuarios(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Metodo que valída correo y contraseña ingresados
     * @param correo
     * @param contrasena
     * @return
     * @throws SQLException
     */
    public Usuario autenticar(String correo, String contrasena) throws SQLException {
        /**
         * Se comprueba que correo y contraseña se hayan ingresado
         */
        if (correo == null || correo.trim().isEmpty() || contrasena == null || contrasena.isEmpty()) {
            return null;
        }
        /**
         * Se delega la búsqueda de usuarios al DAO, que consulta en la BD si el usuario existe o no
         */
        return usuarioDAO.autenticarUsuario(correo, contrasena);
    }
}
