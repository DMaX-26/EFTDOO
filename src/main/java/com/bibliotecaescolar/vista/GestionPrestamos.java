package com.bibliotecaescolar.vista;

import com.bibliotecaescolar.controlador.ControladorEstudiantes;
import com.bibliotecaescolar.controlador.ControladorLibros;
import com.bibliotecaescolar.controlador.ControladorPrestamos;
import com.bibliotecaescolar.dao.EstudianteDAO;
import com.bibliotecaescolar.dao.LibroDAO;
import com.bibliotecaescolar.dao.PrestamoDAO;
import com.bibliotecaescolar.modelo.Estudiante;
import com.bibliotecaescolar.modelo.Libro;
import com.bibliotecaescolar.modelo.Prestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GestionPrestamos extends JFrame {
    private JPanel general;
    private JLabel lblEstudiante;
    private JLabel lblFechaPrestamo;
    private JTextField txtFechaPrestamo;
    private JButton btnRegistrar;
    private JButton btnDevolucion;
    private JButton btnLimpiar;
    private JLabel lblPrestamos;
    private JScrollPane scrollPrestamos;
    private JTable tblPrestamos;
    private JLabel lblLibro;
    private JLabel lblFechaVencimiento;
    private JButton btnSalir;
    private JTextField txtFechaVencimiento;
    private JComboBox cmbEstudiantes;
    private JComboBox cmbLibros;

    private EstudianteDAO estudianteDAO;
    private List<Estudiante> estudiantes;
    private LibroDAO libroDAO;
    private List<Libro> libros;

    private ControladorPrestamos controlador;

    public GestionPrestamos() {
        setTitle("Gestión de Préstamos");
        setContentPane(general);
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cmbEstudiantes.setPrototypeDisplayValue("XXXXXXXXXX");
        cmbLibros.setPrototypeDisplayValue("XXXXXXXXXX");

        /**
         * Se crea el DAO para trabajar con los prestamos
         */
        PrestamoDAO prestamoDAO = new PrestamoDAO(new LibroDAO());

        /**
         * Se crea el controlador y se le pasa el DAO
         */
        controlador = new ControladorPrestamos(prestamoDAO);

        /**
         * Se crea el formato de fecha
         */
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        /**
         * Se obtiene la fecha actual
         */
        txtFechaPrestamo.setText(LocalDate.now().format(formato));
        txtFechaVencimiento.setText(LocalDate.now().plusDays(7).format(formato));

        txtFechaVencimiento.setEditable(false);
        txtFechaPrestamo.setEditable(false);

        btnSalir.addActionListener(e -> dispose());
        btnRegistrar.addActionListener(e -> registrarPrestamo());

        cargarEstudiantes();
        cargarLibros();

        /**
         * Se cargan los prestamos después de inicializar el controlador
         */
        cargarPrestamos();

        btnDevolucion.addActionListener(e -> devolverPrestamo());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    /**
     * Metodo que carga los estudiantes en el combobox
     * @throws SQLException
     */
    private void cargarEstudiantes() {
        try {
            EstudianteDAO estudianteDAO = new EstudianteDAO();

            cmbEstudiantes.removeAllItems();
            ControladorEstudiantes controladorEstudiantes = new ControladorEstudiantes(estudianteDAO);
            for (Estudiante e : controladorEstudiantes.listarEstudiantes()) {
                cmbEstudiantes.addItem(e);
            }

            /**
             * Solo selecciona el primer ítem si hay estudiantes disponibles
             */
            if (cmbEstudiantes.getItemCount() > 0) {
                cmbEstudiantes.setSelectedIndex(0);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo que carga los libros en el combobox
     * @throws SQLException
     */
    private void cargarLibros() {
        try {
            LibroDAO libroDAO = new LibroDAO();

            cmbLibros.removeAllItems();
            ControladorLibros controladorLibros = new ControladorLibros(libroDAO);
            for (Libro e : controladorLibros.listarLibros()) {
                cmbLibros.addItem(e);
            }

            // Solo selecciona el primer ítem si hay libros disponibles
            if (cmbLibros.getItemCount() > 0) {
                cmbLibros.setSelectedIndex(0);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo para registrar un préstamo en la BD
     */
    private void registrarPrestamo() {

        /**
         * Obtener el estudiante seleccionado del combobox
         */
        Estudiante estudiante = (Estudiante) cmbEstudiantes.getSelectedItem();

        /**
         * Obtener el libro seleccionado del combobox
         */
        Libro libro = (Libro) cmbLibros.getSelectedItem();

        /**
         * Comprobar que se hayan seleccionado ambos
         */
        if (estudiante == null || libro == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un estudiante y un libro.");
            return;
        }

        /**
         * Comprobar que exista stock disponible
         */
        if (libro.getStock() <= 0) {
            JOptionPane.showMessageDialog(this, "No hay stock disponible de este libro.");
            return;
        }

        try {
            /**
             * Obtener las fechas desde la interfaz
             */
            String fecha = txtFechaPrestamo.getText();
            String fechaDevolucion = txtFechaVencimiento.getText();

            /**
             * Crear el préstamo
             */
            Prestamo prestamo = new Prestamo(estudiante, libro, fecha, fechaDevolucion, false);

            /**
             * Registrar el préstamo mediante el controlador
             */
            controlador.agregarPrestamo(prestamo);

            /**
             * Descontar una unidad del stock
             */
            libro.setStock(libro.getStock() - 1);

            /**
             * Actualizar el stock del libro en la BD
             */
            LibroDAO libroDAO = new LibroDAO();
            libroDAO.update(libro);

            /**
             * Actualizar la tabla de préstamos
             */
            cargarPrestamos();

            /**
             * Actualizar los libros disponibles en el combobox
             */
            cargarLibros();

            JOptionPane.showMessageDialog(this, "Préstamo registrado correctamente.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar el préstamo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo que carga préstamos en la tblPrestamos
     */
    /**
     * Metodo que carga préstamos en la tblPrestamos
     */
    private void cargarPrestamos() {
        try {
            /**
             * Se obtiene la lista de prestamos desde el controlador
             */
            List<Prestamo> prestamos = controlador.listarPrestamos();

            /**
             * Se crea el modelo que muestra los datos de la tabla
             */
            DefaultTableModel modelo = new DefaultTableModel();

            /**
             * Se agregan columnas al modelo
             */
            modelo.addColumn("ID");
            modelo.addColumn("Estudiante");
            modelo.addColumn("Libro");
            modelo.addColumn("Fecha préstamo");
            modelo.addColumn("Fecha devolución");
            modelo.addColumn("Devuelto");

            /**
             * Se recorre la lista de prestamos
             */
            for (Prestamo p : prestamos) {
                /**
                 * Se agregan los datos obtenidos al modelo
                 */
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getEstudiante().getNombre(),
                        p.getLibro().getTitulo(),
                        p.getFechaPrestamo(),
                        p.getFechaDevolucion(),
                        p.isDevuelto() ? "Sí" : "No"
                });
            }

            /**
             * Se agrega el modelo a la tabla
             */
            tblPrestamos.setModel(modelo);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los préstamos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void devolverPrestamo() {
        /**
         * Se obtiene la fila seleccionada de la tabla
         */
        int fila = tblPrestamos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un préstamo de la tabla.");
            return;
        }

        try {
            /**
             * Se obtiene el "id" del Prestamo seleccionado de la tabla
             */
            int id = Integer.parseInt(tblPrestamos.getValueAt(fila, 0).toString());

            /**
             * Se crea el DAO para trabajar con la BD
             */
            PrestamoDAO prestamoDAO = new PrestamoDAO(new LibroDAO());
            /**
             * Busca en la BD el prestamo por su id mediante el controlador
             */
            Prestamo prestamo = controlador.buscarPrestamo(id);

            /**
             * Se comprueba si se devolvió
             */
            if (prestamo.isDevuelto()) {
                JOptionPane.showMessageDialog(this, "Este libro ya fue devuelto.");
                return;
            }

            /**
             * Se guarda la fecha actual como la fecha de devolución
             */
            prestamo.setFechaDevolucion(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            /**
             * cambia devuelto a true
             */
            prestamo.setDevuelto(true);

            /**
             * Modifica el prestamo en la BD mediante el controlador
             */
            controlador.modificarPrestamo(prestamo);

            Libro libro = prestamo.getLibro();
            /**
             * Devuelve un libro al stock luego del registro de devolución
             */
            libro.setStock(libro.getStock() + 1);

            LibroDAO libroDAO = new LibroDAO();
            libroDAO.update(libro);

            JOptionPane.showMessageDialog(this, "Libro devuelto correctamente.");
            /**
             * Se actualizan los prestamos
             */
            cargarPrestamos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar la devolución: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        cmbEstudiantes.setSelectedIndex(-1);
        cmbLibros.setSelectedIndex(-1);
        tblPrestamos.clearSelection();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        txtFechaPrestamo.setText(LocalDate.now().format(formato));
        txtFechaVencimiento.setText(LocalDate.now().plusDays(7).format(formato));
    }
}
