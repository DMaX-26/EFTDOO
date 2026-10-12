package com.bibliotecaescolar.vista;

import com.bibliotecaescolar.controlador.ControladorEstudiantes;
import com.bibliotecaescolar.dao.EstudianteDAO;
import com.bibliotecaescolar.modelo.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

public class GestionEstudiantes extends JFrame{
    private JPanel general;
    private JLabel lblNombre;
    private JTextField txtNombre;
    private JLabel lblCurso;
    private JTextField txtCurso;
    private JLabel lblCorreo;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JLabel lblEstudiantes;
    private JScrollPane scrollEstudiantes;
    private JTable tblEstudiantes;
    private JLabel lblRut;
    private JTextField txtRut;
    private JTextField txtCorreo;
    private JButton btnSalir;

    private final ControladorEstudiantes controlador;

    public GestionEstudiantes() {
        setTitle("Gestión de Estudiantes");
        setContentPane(general);
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        EstudianteDAO estudianteDAO = new EstudianteDAO();
        controlador = new ControladorEstudiantes(estudianteDAO);

        /**
         * Se cargan los estudiantes al iniciar la ventana
         */
        cargarEstudiantes();
        /**
         * Se asigna una acción a cada botón
         */
        btnAgregar.addActionListener(e -> agregarEstudiante());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnEditar.addActionListener(e -> modificarEstudiante());
        btnEliminar.addActionListener(e -> eliminarEstudiante());
        btnSalir.addActionListener(e -> dispose());

        tblEstudiantes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados();
            }
        });
    }

    /**
     * Metodo que carga los estudiantes en la tabla.
     */
    private void cargarEstudiantes() {
        try {
            /**
             * Se llama al metodo listarEstudiantes del controlador
             * Se guarda en una lista de estudiantes
             */
            List<Estudiante> estudiantes = controlador.listarEstudiantes();

            /**
             * Se crea el modelo que muestra los datos de la tabla
             */
            DefaultTableModel modelo = new DefaultTableModel();

            /**
             * Se agregan columnas al modelo
             */
            modelo.addColumn("ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("RUT");
            modelo.addColumn("Curso");
            modelo.addColumn("Correo");

            /**
             * Se recorre la lista
             */
            for (Estudiante estudiante : estudiantes) {
                /**
                 * Se agregan los datos obtenidos al modelo
                 */
                modelo.addRow(new Object[]{
                        estudiante.getId(),
                        estudiante.getNombre(),
                        estudiante.getRut(),
                        estudiante.getCurso(),
                        estudiante.getCorreo()
                });
            }
            /**
             * Se agrega el modelo a la tabla
             */
            tblEstudiantes.setModel(modelo);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los estudiantes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo para agregar un estudiante.
     */
    private void agregarEstudiante() {

        /**
         * Se obtienen los datos de los campos de texto
         */
        String nombre = txtNombre.getText().trim();
        String rut = txtRut.getText().trim();
        String curso = txtCurso.getText().trim();
        String correo = txtCorreo.getText().trim();

        /**
         * Se comprueba que los campos no esten vacios
         */
        if (nombre.isEmpty() || rut.isEmpty() || curso.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes completar todos los campos.");
            return;
        }

        try {
            /**
             * Se crea el estudiante con los datos ingresados
             */
            Estudiante estudiante = new Estudiante(nombre, rut, curso, correo);

            /**
             * Se guarda el estudiante en la BD
             */
            controlador.agregarEstudiante(estudiante);

            JOptionPane.showMessageDialog(this, "Estudiante agregado correctamente.");

            /**
             * Se actualizan los datos cargados en la tabla
             */
            cargarEstudiantes();
            /**
             * Se limpian los campos de texto
             */
            limpiarCampos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar el estudiante: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo para modificar un estudiante.
     */
    private void modificarEstudiante() {
        /**
         * Se obtiene la fila seleccionada de la tabla
         */
        int fila = tblEstudiantes.getSelectedRow();

        /**
         * Si no se marca la fila, se abre una ventana con un mensaje
         */
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un estudiante de la tabla.");
            return;
        }

        /**
         * Se obtienen los datos de los campos de texto
         */
        String nombre = txtNombre.getText().trim();
        String rut = txtRut.getText().trim();
        String curso = txtCurso.getText().trim();
        String correo = txtCorreo.getText().trim();

        /**
         * Se evalúa que los campos no estén vacíos
         */
        if (nombre.isEmpty() || rut.isEmpty() || curso.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes completar todos los campos.");
            return;
        }

        try {
            /**
             * Se obtiene el dato de la primera columna de la fila seleccionada como texto, se convierte a int
             */
            int id = Integer.parseInt(tblEstudiantes.getValueAt(fila, 0).toString());

            /**
             * Se crea un estudiante con los datos obtenidos
             */
            Estudiante estudiante = new Estudiante(id, nombre, rut, curso, correo);

            /**
             * Mediante el controlador se le pasa estudiante al metodo que modifica un estudiante de la BD
             */
            controlador.modificarEstudiante(estudiante);

            JOptionPane.showMessageDialog(this, "Estudiante modificado correctamente.");

            cargarEstudiantes();
            limpiarCampos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
        }
    }

    /**
     * Metodo que muestra los datos del estudiante seleccionado de la tabla.
     */
    private void cargarDatosSeleccionados() {

        /**
         * Se obtiene la fila seleccionada de la tabla
         */
        int fila = tblEstudiantes.getSelectedRow();

        /**
         * Si hay una fila marcada
         */
        if (fila != -1) {
            /**
             * Se pasan los datos a cada campo de texto para modificarlos
             */
            txtNombre.setText(tblEstudiantes.getValueAt(fila, 1).toString());
            txtRut.setText(tblEstudiantes.getValueAt(fila, 2).toString());
            txtCurso.setText(tblEstudiantes.getValueAt(fila, 3).toString());
            txtCorreo.setText(tblEstudiantes.getValueAt(fila, 4).toString());
        }
    }

    /**
     * Metodo para eliminar un estudiante.
     */
    private void eliminarEstudiante() {

        /**
         * Se obtiene la fila seleccionada de la tabla
         */
        int fila = tblEstudiantes.getSelectedRow();

        /**
         * Si no se marca la fila, se abre una ventana con un mensaje
         */
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un estudiante de la tabla.");
            return;
        }

        /**
         * Mensaje de confirmación para eliminar un estudiante
         */
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Estas seguro de eliminar este estudiante?", "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION);

        /**
         * Si la respuesta de la ventana emergente es sí
         */
        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                /**
                 * Se obtiene el dato de la primera columna de la fila seleccionada como texto, se convierte a int y se guarda en "id"
                 */
                int id = Integer.parseInt(tblEstudiantes.getValueAt(fila, 0).toString());

                /**
                 * Se le pasa "id" al metodo que elimina un estudiante de la BD
                 */
                controlador.eliminarEstudiante(id);

                JOptionPane.showMessageDialog(this, "Estudiante eliminado correctamente.");

                cargarEstudiantes();
                limpiarCampos();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el estudiante: " + e.getMessage());
            }
        }
    }

    /**
     * Metodo que limpia los campos de texto.
     */
    private void limpiarCampos() {
        txtNombre.setText("");
        txtRut.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");
        tblEstudiantes.clearSelection();
        txtNombre.requestFocus();
    }
}
