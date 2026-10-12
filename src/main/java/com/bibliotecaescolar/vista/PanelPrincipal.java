package com.bibliotecaescolar.vista;

import com.bibliotecaescolar.controlador.ControladorUsuarios;
import com.bibliotecaescolar.dao.UsuarioDAO;
import com.bibliotecaescolar.modelo.Rol;

import javax.swing.*;

public class PanelPrincipal extends JFrame {
    private JPanel PanelGestion;
    private JLabel lblTitulo;
    private JButton btnGestionLibros;
    private JButton btnGestionEstudiantes;
    private JButton btnPrestamos;
    private JButton btnReportes;
    private JButton btnSalir;
    private JButton btnGestionCategorias;

    private final Rol rol;

    public PanelPrincipal(Rol rol) {
        this.rol = rol;
        setTitle("Biblioteca Escolar - Rol: " + rol);
        setContentPane(PanelGestion);
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        configurarPermisos();

        /**
         * btnSalir para salir del PanelPrincipal y regresar al Login
         */
        btnSalir.addActionListener(e -> {
            this.dispose();

            /**
             * Se crea el DAO
             */
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            /**
             * Se crea el controladorUsuarios y se le pasa el DAO
             */
            ControladorUsuarios controlador = new ControladorUsuarios(usuarioDAO);

            /**
             * Se abre el login
             */
            Login login = new Login(controlador);
            login.setVisible(true);
        });

        /**
         * Este botón abre la ventana GestionLibros
         */
        btnGestionLibros.addActionListener(e -> {
            GestionLibros ventana = new GestionLibros();
            ventana.setVisible(true);
        });

        /**
         * Este botón abre la ventana GestionEstudiantes
         */
        btnGestionEstudiantes.addActionListener(e -> {
            GestionEstudiantes ventana = new GestionEstudiantes();
            ventana.setVisible(true);
        });


        /**
         * Este botón abre la ventana GestionPrestamos
         */
        btnPrestamos.addActionListener(e -> {
            GestionPrestamos ventana = new GestionPrestamos();
            ventana.setVisible(true);
        });

        /**
         * Este botón abre la ventana GestionCategorias
         */
        btnGestionCategorias.addActionListener(e -> {
            GestionCategorias ventana = new GestionCategorias();
            ventana.setVisible(true);
        });

        /**
         * Este botón abre la ventana Reportes
         */
        btnReportes.addActionListener(e -> {
            Reportes ventana = new Reportes();
            ventana.setVisible(true);
        });
    }

    /**
     * Metodo que configura los permisos del sistema según el rol del usuario
     */
    public void configurarPermisos(){
        /**
         * Si el rol es Bibliotecario, tiene gestión total
         */
        if (rol == Rol.BIBLIOTECARIO){
            btnGestionLibros.setEnabled(true);
            btnGestionEstudiantes.setEnabled(true);
            btnPrestamos.setEnabled(true);
            btnReportes.setEnabled(true);
            btnSalir.setEnabled(true);
            /**
             * Si el rol es Estudiante, solo se habilitan prestamos, devoluciones y consultas
              */
        } else if (rol == Rol.ESTUDIANTE) {
            btnGestionLibros.setEnabled(false);
            btnGestionEstudiantes.setEnabled(false);
            btnPrestamos.setEnabled(true);
            btnReportes.setEnabled(true);
            btnSalir.setEnabled(true);
        }
    }
}
