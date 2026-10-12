package com.bibliotecaescolar.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca" + "?useSSL=false" + "&allowPublicKeyRetrieval=true" + "&serverTimezone=UTC" + "&characterEncoding=UTF-8";
    private static final DatabaseConnection INSTANCIA = new DatabaseConnection();
    private Connection conexion;

    private DatabaseConnection() {
    }

    public static DatabaseConnection getInstancia() {
        return INSTANCIA;
    }

    public Connection obtenerConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            String user = "root";
            String password = "12345";

            conexion = DriverManager.getConnection(URL, user, password);
        }
        return conexion;
    }
}
