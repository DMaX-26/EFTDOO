package com.bibliotecaescolar;

import com.bibliotecaescolar.conexion.DatabaseConnection;
import com.bibliotecaescolar.controlador.ControladorUsuarios;
import com.bibliotecaescolar.dao.UsuarioDAO;
import com.bibliotecaescolar.vista.Login;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        ControladorUsuarios controlador = new ControladorUsuarios(usuarioDAO);

        Login login = new Login(controlador);
        login.setVisible(true);

        try {
            Connection conexion = DatabaseConnection.getInstancia().obtenerConexion();

            if (conexion != null && !conexion.isClosed()) {
                System.out.println("✅ Conexión exitosa a la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar con la base de datos:");
            e.printStackTrace();
        }
    }
}