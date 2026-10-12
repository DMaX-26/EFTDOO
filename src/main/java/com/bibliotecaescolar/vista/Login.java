package com.bibliotecaescolar.vista;

import com.bibliotecaescolar.controlador.ControladorUsuarios;
import com.bibliotecaescolar.modelo.Usuario;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Login extends JFrame {
    private JPanel Login;
    private JTextField txtUsuario;
    private JButton btnAcceder;
    private JButton btnCancelar;
    private JLabel lblInicio;
    private JLabel lblUsuario;
    private JLabel lblContrasena;
    private JPasswordField txtContrasena;
    private ControladorUsuarios controladorUsuarios;

    /**
     * Constructor del Login
     * @param controladorUsuarios
     */
    public Login(ControladorUsuarios controladorUsuarios) {
        this.controladorUsuarios = controladorUsuarios;
        setTitle("Sistema Biblioteca Escolar");
        setContentPane(Login);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        /**
         * Al presionar el boton "Acceder" se llama al metodo "AutenticarUsuario"
         */
        btnAcceder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticarUsuario();
            }
        });
        btnCancelar.addActionListener(e -> System.exit(0));
    }

    /**
     * Metodo para validar los datos ingresados en el formulario
     * @throws SQLException
     */
    private void autenticarUsuario() {
        /**
         * Se obtienen correo y contrasenia de las cajas de texto y se guardan como String en sus correspondientes variables
         */
        String correo = txtUsuario.getText().trim();
        String pass = new String(txtContrasena.getPassword());

        /**
         * Try-catch para manejo de excepciones del tipo SQLException
         */
        try {
            /**
             * ControladorUsuarios llama al metodo "autenticar" para validar que el usuario con las credenciales ingresadas existe
             */
            Usuario usuario = controladorUsuarios.autenticar(correo, pass);

            /**
             * Si el usuario existe en la BD, se abre una ventana con un mensaje de bienvenida
             */
            if (usuario != null) {
                JOptionPane.showMessageDialog(this, "Bienvenido, " + usuario.getRol());
                /**
                 * Luego de haber creado PanelPrincipal, creamos esta línea de código que abre la interfaz PanelPrincipal luego de autenticar el usuario.
                 */
                new PanelPrincipal(usuario.getRol()).setVisible(true);
                this.dispose();
            } else {
                /**
                 * Si no, se abre una ventana informando que las credenciales no son válidas
                 */
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
