package com.bibliotecaescolar.vista;

import com.bibliotecaescolar.controlador.ControladorCategorias;
import com.bibliotecaescolar.dao.CategoriaDAO;
import com.bibliotecaescolar.modelo.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

public class GestionCategorias extends JFrame {
    private JLabel lblNombre;
    private JButton btnRegistrar;
    private JButton btnModificar;
    private JButton btnLimpiar;
    private JLabel lblPrestamos;
    private JScrollPane scrollCategorias;
    private JTable tblCategorias;
    private JButton btnSalir;
    private JTextField txtNombre;
    private JButton btnEliminar;
    private JPanel general;

    private ControladorCategorias controlador;

    public GestionCategorias() {
        setTitle("Gestión de Categorías");
        setContentPane(general);
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        /**
         * Crear el controlador
         */
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        controlador = new ControladorCategorias(categoriaDAO);

        /**
         * Conectar los botones con sus métodos
         */
        btnRegistrar.addActionListener(e -> registrarCategoria());
        btnModificar.addActionListener(e -> modificarCategoria());
        btnEliminar.addActionListener(e -> eliminarCategoria());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnSalir.addActionListener(e -> dispose());

        /**
         * Cargar las categorías al abrir la ventana
         */
        cargarCategorias();

        /**
         * Seleccionar una categoría de la tabla
         */
        tblCategorias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarCategoriaSeleccionada();
            }
        });
    }

    /**
     * Metodo para mostrar las categorías en la tabla
     */
    private void cargarCategorias() {
        try {
            /**
             * Se listan las categorias mediante el controlador y se agregan a una lista
             */
            List<Categoria> categorias = controlador.listarCategorias();

            /**
             * Se crea el modelo que muestra los datos en la tabla
             */
            DefaultTableModel modelo = new DefaultTableModel();

            /**
             * Se agregan columnas al modelo
             */
            modelo.addColumn("ID");
            modelo.addColumn("Nombre");

            /**
             * Se recorre la lista
             */
            for (Categoria categoria : categorias) {
                /**
                 * Se agregan los datos obtenidos al modelo
                 */
                modelo.addRow(new Object[]{
                        categoria.getId(),
                        categoria.getNombre()
                });
            }
            /**
             * Se agrega el modelo con los datos a la tabla
             */
            tblCategorias.setModel(modelo);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las categorías: " + e.getMessage());
        }
    }

    /**
     * Metodo para registrar una categoría
     */
    private void registrarCategoria() {
        /**
         * Se obtiene el nombre del campo de texto
         */
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el nombre de la categoría.");
            return;
        }

        try {
            /**
             * Se crea la categoria con el nombre obtenido
             */
            Categoria categoria = new Categoria(nombre);

            /**
             * Mediante el controlador se agrega la categoria a la BD
             */
            controlador.agregarCategoria(categoria);

            JOptionPane.showMessageDialog(this, "Categoría registrada correctamente.");

            cargarCategorias();
            limpiarCampos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar la categoría: " + e.getMessage());
        }
    }

    /**
     * Metodo para modificar una categoría
     */
    private void modificarCategoria() {
        /**
         * Se obtiene la fila seleccionada de la tabla
         */
        int fila = tblCategorias.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoría de la tabla.");
            return;
        }

        /**
         * Se obtiene el nombre de la categoria
         */
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el nombre de la categoría.");
            return;
        }

        try {
            /**
             * Se obtiene el id de la categoria
             */
            int id = Integer.parseInt(tblCategorias.getValueAt(fila, 0).toString());

            /**
             * Se crea una categoria con el "id "y nombre obtenidos
             */
            Categoria categoria = new Categoria(id, nombre);

            /**
             * Se modifica la categoria en la BD mediante el controlador
             */
            controlador.modificarCategoria(categoria);

            JOptionPane.showMessageDialog(this, "Categoría modificada correctamente.");

            cargarCategorias();
            limpiarCampos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar la categoría: " + e.getMessage());
        }
    }

    /**
     * Metodo para eliminar una categoría
     */
    private void eliminarCategoria() {
        /**
         * Se obtiene la fila seleccionada de la tabla
         */
        int fila = tblCategorias.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoría de la tabla.");
            return;
        }

        /**
         * Se abre una ventana de confirmación
         */
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Deseas eliminar esta categoría?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            /**
             * Se obtiene el "id"
             */
            int id = Integer.parseInt(tblCategorias.getValueAt(fila, 0).toString());

            /**
             * Le pasamos el "id" al controlador para que mediante el metodo eliminarCategoria la elimine de la BD
             */
            controlador.eliminarCategoria(id);

            JOptionPane.showMessageDialog(this, "Categoría eliminada correctamente.");

            cargarCategorias();
            limpiarCampos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar la categoría. " + "Puede tener libros asociados.\n" + e.getMessage());
        }
    }

    /**
     * Metodo para cargar los datos de la categoría seleccionada
     */
    private void cargarCategoriaSeleccionada() {
        int fila = tblCategorias.getSelectedRow();

        if (fila != -1) {
            txtNombre.setText(tblCategorias.getValueAt(fila, 1).toString());
        }
    }

    /**
     * Metodo para limpiar los campos
     */
    private void limpiarCampos() {
        txtNombre.setText("");
        tblCategorias.clearSelection();
        txtNombre.requestFocus();
    }
}
