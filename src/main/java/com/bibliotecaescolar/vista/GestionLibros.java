package com.bibliotecaescolar.vista;

import com.bibliotecaescolar.controlador.ControladorLibros;
import com.bibliotecaescolar.dao.CategoriaDAO;
import com.bibliotecaescolar.dao.LibroDAO;
import com.bibliotecaescolar.modelo.Categoria;
import com.bibliotecaescolar.modelo.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

public class GestionLibros extends JFrame {
    private JPanel general;
    private JLabel lblLibro;
    private JTextField txtLibro;
    private JLabel lblAutor;
    private JTextField txtAutor;
    private JLabel lblIsbn;
    private JLabel lblEditorial;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JLabel lblLibros;
    private JScrollPane scrollLibros;
    private JTable tblLibros;
    private JTextField txtEditorial;
    private JLabel lblStock;
    private JTextField txtStock;
    private JLabel lblCategoria;
    private JComboBox cmbCategoria;
    private JTextField txtIsbn;
    private JButton btnSalir;

    private final ControladorLibros controlador;

    public GestionLibros() {
        setTitle("Gestión de Libros");
        setContentPane(general);
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        /**
         * Se crea el libroDAO
         */
        LibroDAO libroDAO = new LibroDAO();
        /**
         * Se crea el controlador y se le pasa libroDAO para conectarlos
         */
        controlador = new ControladorLibros(libroDAO);

        /**
         * Se cargan los libros al inicio
         */
        cargarLibros();
        /**
         * Se cargan las categorías al inicio
         */
        cargarCategorias();

        /**
         * Se agrega una acción a cada botón
         */
        btnAgregar.addActionListener(e -> agregarLibro());
        btnEditar.addActionListener(e -> modificarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnSalir.addActionListener(e -> dispose());

        /**
         * Al seleccionar un libro de la tabla, los datos aparecen en los campos de texto
         */
        tblLibros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados();
            }
        });
    }

    /**
     * Metodo que carga los libros en la tabla.
     */
    private void cargarLibros() {
        try {
            /**
             * Se obtiene la lista de libros desde el controlador
             */
            List<Libro> libros = controlador.listarLibros();

            /**
             * Se crea el modelo que muestra los datos de la tabla
             */
            DefaultTableModel modelo = new DefaultTableModel();

            /**
             * Se agregan las columnas al modelo
             */
            modelo.addColumn("ID");
            modelo.addColumn("Título");
            modelo.addColumn("Autor");
            modelo.addColumn("ISBN");
            modelo.addColumn("Editorial");
            modelo.addColumn("Stock");
            modelo.addColumn("Categoría");

            /**
             * Se recorre la lista de libros
             */
            for (Libro libro : libros) {
                /**
                 * Se agregan los datos obtenidos al modelo
                 */
                modelo.addRow(new Object[]{
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getIsbn(),
                        libro.getEditorial(),
                        libro.getStock(),
                        libro.getCategoria().getNombre()
                });
            }
            /**
             * Se agrega el modelo a la tabla
             */
            tblLibros.setModel(modelo);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los libros: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo para agregar un libro.
     */
    private void agregarLibro() {

        /**
         * Se obtienen los datos de los campos de texto
         */
        String titulo = txtLibro.getText().trim();
        String autor = txtAutor.getText().trim();
        String isbn = txtIsbn.getText().trim();
        String editorial = txtEditorial.getText().trim();
        String stockLibro = txtStock.getText().trim();

        /**
         * Se valída que los campos no queden vacíos
         */
        if (titulo.isEmpty() || autor.isEmpty() || isbn.isEmpty() || editorial.isEmpty() || stockLibro.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes completar todos los campos.");
            return;
        }

        /**
         * Try-catch para manejo de excepciones
         */
        try {
            /**
             * Se convierte stockLibro a int y se guarda en la variable stock
             */
            int stock = Integer.parseInt(stockLibro);

            /**
             * Se valída que el stock sea un número válido
             */
            if (stock < 0) {
                JOptionPane.showMessageDialog(this, "El stock no puede ser negativo.");
                return;
            }

            /**
             * Obtener la categoría seleccionada del combobox
             */
            Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();

            /**
             * Se crea un libro
             */
            Libro libro = new Libro(titulo, autor, isbn, editorial, stock, categoria);

            /**
             * Envía el libro al controlador para que se agregue a la BD
             */
            controlador.agregarLibro(libro);

            JOptionPane.showMessageDialog(this, "Libro agregado correctamente.");

            cargarLibros();
            limpiarCampos();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El stock debe ser un número entero positivo.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar el libro: " + e.getMessage());
        }
    }

    /**
     * Metodo que carga las categorias en el combobox.
     */
    private void cargarCategorias() {
        try {
            /**
             * Se crea el DAO
             */
            CategoriaDAO categoriaDAO = new CategoriaDAO();

            /**
             * El DAO llama obtiene las categorias de la BD y se guardan en una lista
             */
            List<Categoria> categorias = categoriaDAO.readAll();

            /**
             * Se recorre la lista y se cargan en el combobox todas las categorías encontradas
             */
            for (Categoria categoria : categorias) {
                cmbCategoria.addItem(categoria);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las categorias: " + e.getMessage());
        }
    }

    /**
     * Metodo que permite que al seleccionar un libro de la tabla,
     * aparezca su categoría correcta en el combobox.
     */
    private void cargarDatosSeleccionados() {

        /**
         * Se obtiene la fila seleccionada
         */
        int fila = tblLibros.getSelectedRow();

        /**
         * Si hay una fila seleccionada
         */
        if (fila != -1) {
            /**
             * Se cargan los datos del libro en los campos
             */
            txtLibro.setText(tblLibros.getValueAt(fila, 1).toString());
            txtAutor.setText(tblLibros.getValueAt(fila, 2).toString());
            txtIsbn.setText(tblLibros.getValueAt(fila, 3).toString());
            txtEditorial.setText(tblLibros.getValueAt(fila, 4).toString());
            txtStock.setText(tblLibros.getValueAt(fila, 5).toString());

            /**
             * Se obtiene el nombre de la categoría de la tabla
             */
            String nombreCategoria = tblLibros.getValueAt(fila, 6).toString();

            /**
             * Se recorren todas las categorias en el combobox
             */
            for (int i = 0; i < cmbCategoria.getItemCount(); i++) {

                /**
                 * Se obtiene una categoria del combobox
                 */
                Categoria categoria = (Categoria) cmbCategoria.getItemAt(i);

                if (categoria.getNombre().equals(nombreCategoria)) {
                    cmbCategoria.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    /**
     * Metodo para modificar un libro.
     */
    private void modificarLibro() {

        /**
         * Se obtiene la fila seleccionada
         */
        int fila = tblLibros.getSelectedRow();

        /**
         * Se valída que se haya seleccionado una fila
         */
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro de la tabla.");
            return;
        }

        /**
         * Se obtienen los datos de los campos de texto
         */
        String titulo = txtLibro.getText().trim();
        String autor = txtAutor.getText().trim();
        String isbn = txtIsbn.getText().trim();
        String editorial = txtEditorial.getText().trim();
        String stockLibro = txtStock.getText().trim();

        /**
         * Se valídan que se ingresen los campos de texto y que se haya seleccionado un ítem del combobox
         */
        if (titulo.isEmpty() || autor.isEmpty() || isbn.isEmpty() || editorial.isEmpty() || stockLibro.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes completar todos los campos.");
            return;
        }
        if (cmbCategoria.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoría.");
            return;
        }

        /**
         * Try-catch para manejar excepciones
         */
        try {
            /**
             * Se obtiene el "id" del libro seleccionado de la tabla
             */
            int id = Integer.parseInt(tblLibros.getValueAt(fila, 0).toString());
            /**
             * El stockLibro se convierte a int y se guarda en la variable stock
             */
            int stock = Integer.parseInt(stockLibro);

            /**
             * Se valída que el stock sea correcto
             */
            if (stock < 0) {
                JOptionPane.showMessageDialog(this, "El stock no puede ser negativo.");
                return;
            }

            /**
             * Se obtiene la categoria seleccionada del combobox
             */
            Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();

            /**
             * Se crea un Libro y se ingresan los datos
             */
            Libro libro = new Libro(id, titulo, autor, isbn, editorial, stock, categoria);

            /**
             * Envía el libro al controlador para que se actualice en la BD
             */
            controlador.modificarLibro(libro);

            JOptionPane.showMessageDialog(this, "Libro modificado correctamente.");

            /**
             * Se actualizan los libros de la tabla y se limpian los campos
             */
            cargarLibros();
            limpiarCampos();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El stock debe ser un número entero.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar el libro: " + e.getMessage());
        }
    }

    /**
     * Metodo para eliminar un libro.
     */
    private void eliminarLibro() {

        /**
         * Se obtiene la fila seleccionada de la tabla
         */
        int fila = tblLibros.getSelectedRow();

        /**
         * Si no se marca una fila, se abre una ventana con un mensaje
         */
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro de la tabla.");
            return;
        }

        /**
         * Si se marca una fila, se abre un mensaje de confirmación
         */
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este libro?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        /**
         * Si la opción es SÍ
         */
        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                /**
                 * Se obtiene el "id" del libro seleccionado
                 */
                int id = Integer.parseInt(tblLibros.getValueAt(fila, 0).toString());

                /**
                 * Se envía el "id" al controlador para que se elimine la fila de la BD
                 */
                controlador.eliminarLibro(id);

                JOptionPane.showMessageDialog(this, "Libro eliminado correctamente.");

                cargarLibros();
                limpiarCampos();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el libro: " + e.getMessage());
            }
        }
    }

    private void limpiarCampos(){
        txtLibro.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtEditorial.setText("");
        txtStock.setText("");
        cmbCategoria.setSelectedIndex(-1);
        tblLibros.clearSelection();
    }
}
