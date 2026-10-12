package com.bibliotecaescolar.vista;

import com.bibliotecaescolar.controlador.ControladorLibros;
import com.bibliotecaescolar.controlador.ControladorPrestamos;
import com.bibliotecaescolar.dao.LibroDAO;
import com.bibliotecaescolar.dao.PrestamoDAO;
import com.bibliotecaescolar.modelo.Libro;
import com.bibliotecaescolar.modelo.Prestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Reportes extends JFrame {
    private JPanel general;
    private JButton btnLibrosDisponibles;
    private JButton btnActivos;
    private JButton btnHistorial;
    private JLabel lblReportes;
    private JScrollPane scrollReportes;
    private JTable tblReportes;
    private JButton btnSalir;
    private JButton btnLibrosMasPrestados;
    private JButton btnAtrasados;

    private ControladorLibros controladorLibros;
    private ControladorPrestamos controladorPrestamos;

    public Reportes() {
        setTitle("Gestión de Reportes");
        setContentPane(general);
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        controladorLibros = new ControladorLibros(new LibroDAO());
        controladorPrestamos = new ControladorPrestamos(new PrestamoDAO(new LibroDAO()));

        btnLibrosDisponibles.addActionListener(e -> mostrarLibros());
        btnActivos.addActionListener(e -> mostrarPrestamosActivos());
        btnHistorial.addActionListener(e -> mostrarHistorial());
        btnLibrosMasPrestados.addActionListener(e -> mostrarLibrosMasPrestados());
        btnAtrasados.addActionListener(e -> mostrarPrestamosAtrasados());
        btnSalir.addActionListener(e -> dispose());
    }

    /**
     * Metodo que muestra los libros registrados y su stock.
     */
    private void mostrarLibros() {
        try {
            /**
             *
             */
            List<Libro> libros = controladorLibros.listarLibros();

            /**
             * Se crea el modelo que muestra los datos en la tabla
             */
            DefaultTableModel modelo = new DefaultTableModel();

            /**
             * Se agregan columnas al modelo
             */
            modelo.addColumn("ID");
            modelo.addColumn("Título");
            modelo.addColumn("Autor");
            modelo.addColumn("ISBN");
            modelo.addColumn("Stock");

            for (Libro libro : libros) {
                modelo.addRow(new Object[]{
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getIsbn(),
                        libro.getStock()
                });
            }
            tblReportes.setModel(modelo);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los libros: " + e.getMessage());
        }
    }

    /**
     * Metodo que muestra los préstamos que todavía no han sido devueltos.
     */
    private void mostrarPrestamosActivos() {
        try {
            List<Prestamo> prestamos = controladorPrestamos.listarPrestamos();

            DefaultTableModel modelo = crearModeloPrestamos();

            for (Prestamo p : prestamos) {
                /**
                 * Si el libro no se ha devuelto
                 */
                if (!p.isDevuelto()) {
                    /**
                     * Se agrega a la tabla
                     */
                    agregarPrestamo(modelo, p);
                }
                /**
                 * Si el libro no se ha devuelto y la fecha límite fue superada
                 */
                if (!p.isDevuelto() && java.time.LocalDate.parse(p.getFechaDevolucion()).isBefore(java.time.LocalDate.now())) {
                    agregarPrestamo(modelo, p);
                }
            }
            /**
             * Se agrega el modelo con los datos a la tabla
             */
            tblReportes.setModel(modelo);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los préstamos: " + e.getMessage());
        }
    }

    /**
     * Metodo que muestra el historial completo de préstamos.
     */
    private void mostrarHistorial() {
        try {
            /**
             * Mediante el controlador se obtiene listarPrestamos de la BD
             */
            List<Prestamo> prestamos = controladorPrestamos.listarPrestamos();

            /**
             * Modelo
             */
            DefaultTableModel modelo = crearModeloPrestamos();

            /**
             * Se recorre la lista
             */
            for (Prestamo p : prestamos) {
                /**
                 * Se agrega un prestamo a la tabla
                 */
                agregarPrestamo(modelo, p);
            }
            /**
             * Se agrega el modelo a la tabla
             */
            tblReportes.setModel(modelo);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el historial: " + e.getMessage());
        }
    }

    /**
     * Metodo que muestra los libros mas prestados.
     */
    private void mostrarLibrosMasPrestados() {
        try {
            /**
             * Mediante el controlador se obtiene los libros mas prestados
             */
            ResultSet rs = controladorPrestamos.librosMasPrestados();

            /**
             * Modelo que muestra los datos de la tabla
             */
            DefaultTableModel modelo = new DefaultTableModel();

            /**
             * Se agregan columnas al modelo
             */
            modelo.addColumn("ID");
            modelo.addColumn("Título");
            modelo.addColumn("Autor");
            modelo.addColumn("Total préstamos");

            while (rs.next()) {
                /**
                 * Se agregan los datos obtenidos al modelo
                 */
                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("total")
                });
            }
            /**
             * Se agrega el modelo a la tabla
             */
            tblReportes.setModel(modelo);
            rs.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los libros más prestados: " + e.getMessage());
        }
    }

    /**
     * Metodo que muestra los prestamos atrasados.
     */
    private void mostrarPrestamosAtrasados() {
        try {
            /**
             * Mediante el controlador se obtiene listarPrestamos
             */
            List<Prestamo> prestamos = controladorPrestamos.listarPrestamos();

            /**
             * Modelo
             */
            DefaultTableModel modelo = crearModeloPrestamos();

            for (Prestamo p : prestamos) {
                /**
                 * Si el libro no se ha devuelto y la fecha límite fue superada
                 */
                if (!p.isDevuelto() && java.time.LocalDate.parse(p.getFechaDevolucion()).isBefore(java.time.LocalDate.now())) {
                    /**
                     * Se agrega un prestamo a la tabla
                     */
                    agregarPrestamo(modelo, p);
                }
            }
            /**
             * Se agrega el modelo a la tabla
             */
            tblReportes.setModel(modelo);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los prestamos atrasados: " + e.getMessage());
        }
    }

    /**
     * Metodo que crea las columnas para los reportes de préstamos.
     */
    private DefaultTableModel crearModeloPrestamos() {
        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Estudiante");
        modelo.addColumn("Libro");
        modelo.addColumn("Fecha préstamo");
        modelo.addColumn("Fecha devolución");
        modelo.addColumn("Devuelto");

        return modelo;
    }

    /**
     * Metodo que agrega un préstamo a la tabla.
     */
    private void agregarPrestamo(DefaultTableModel modelo, Prestamo p) {
        modelo.addRow(new Object[]{
                p.getId(),
                p.getEstudiante().getNombre(),
                p.getLibro().getTitulo(),
                p.getFechaPrestamo(),
                p.getFechaDevolucion(),
                p.isDevuelto() ? "Sí" : "No"
        });
    }
}
