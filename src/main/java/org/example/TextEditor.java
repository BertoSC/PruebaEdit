package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TextEditor {
    private JTextArea ventanaTxt;
    private JFileChooser fileChooser;
    private boolean modificado = false;

    public TextEditor() {
        ventanaTxt = new JTextArea();
        fileChooser = new JFileChooser();
        construirEditor();
    }

    private void construirEditor() {

        // Creación de ventana
        JFrame ventana = new JFrame("Editor de Texto");

        ventana.setSize(600, 600);
        ventana.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        ventana.setLocationRelativeTo(null);

        // Opciones para que salte de línea del texto al llegar al límite y lo haga sin cortar palabras
        ventanaTxt.setLineWrap(true);
        ventanaTxt.setWrapStyleWord(true);

        // Creación de barra de desplazamiento
        JScrollPane scrollPane = new JScrollPane(ventanaTxt);
        ventana.add(scrollPane, BorderLayout.CENTER);

        // Creación del menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem abrir = new JMenuItem("Abrir");
        JMenuItem guardar = new JMenuItem("Guardar");
        JMenuItem salir = new JMenuItem("Salir");

        // Acciones de cada menú
        abrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirArchivo();
            }
        });

        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarArchivo();
            }
        });

        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salirAplicacion(ventana);
            }
        });

        // Agregar los elementos al menú
        menuArchivo.add(abrir);
        menuArchivo.add(guardar);
        menuArchivo.addSeparator();
        menuArchivo.add(salir);
        menuBar.add(menuArchivo);

        ventana.setJMenuBar(menuBar);
        ventana.setVisible(true);

        // Atajos de teclado
        abrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        guardar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        salir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

        // Manejo de cambios en el texto para detectar modificaciones no guardadas
        ventanaTxt.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                modificado = true;
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                modificado = true;
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                modificado = true;
            }
        });

        // Confirmación de salida si hay cambios sin guardar
        ventana.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                salirAplicacion(ventana);
            }
        });
    }

    // Método para abrir archivo
    private void abrirArchivo() {
        int resultado = fileChooser.showOpenDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            if (archivo.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                    ventanaTxt.setText("");
                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        ventanaTxt.append(linea + System.lineSeparator());
                    }
                    modificado = false;
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error al abrir el archivo", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "El archivo no existe", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para guardar archivo
    private void guardarArchivo() {
        int resultado = fileChooser.showSaveDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                writer.write(ventanaTxt.getText());
                modificado = false;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para salir de la aplicación
    private void salirAplicacion(JFrame vent) {
        if (modificado) {
            int opcion = JOptionPane.showConfirmDialog(null, "Hay cambios sin guardar. ¿Salir de todos modos?", "SALIR", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.NO_OPTION) {
                return;
            }
        }
        vent.dispose();
    }


    public static void main(String[] args) {
        new TextEditor();

    }
}
