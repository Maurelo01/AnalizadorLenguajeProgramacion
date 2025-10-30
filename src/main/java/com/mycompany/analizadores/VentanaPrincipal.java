package com.mycompany.analizadores;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Utilities;

public class VentanaPrincipal extends javax.swing.JFrame 
{    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());
    private final ControladorArchivos controladorArchivos;
    private final ControladorAnalizador controladorAnalizador;
    public VentanaPrincipal() 
    {
        initComponents();
        this.controladorArchivos = new ControladorArchivos();
        this.controladorAnalizador = new ControladorAnalizador();
        this.setLocationRelativeTo(null);
    }
    
    private void actualizarTablaTokens(ArrayList<Token> tokens) // Llena la tabla de tokens con los validos
    {
        String[] columnas = {"Lexema", "Tipo", "Linea", "Columna"}; // Define las columnas de la tabla
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false; // Hacer que la tabla no sea editable
            }
        };
        // Llenar el modelo con los datos de la lista
        for (Token token : tokens) 
        {
            Object[] fila = {token.getLexema(), token.getTipo().toString(), token.getLinea(), token.getColumna()};
            modelo.addRow(fila);
        }

        // Asignar el modelo nuevo al JTable
        this.tablaDeTokens.setModel(modelo);
    }
    
    private void actualizarTablaErrores(ArrayList<ErrorLexico> errores) 
    {
        // Definir las columnas
        String[] columnas = {"Lexema", "Línea", "Columna", "Descripción"};

        // Crear la tabla no editable
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };
        // Llenar la tabla
        for (ErrorLexico error : errores) 
        {
            modelo.addRow(error.toObjectRow());
        }

        // Asignar el modelo al JTable
        this.tablaDeErrores.setModel(modelo);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        areaDeTextoPrincipal = new javax.swing.JTextPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        areaDeConsola = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaDeErrores = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaDeTokens = new javax.swing.JTable();
        lblEstado = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        itemAbrir = new javax.swing.JMenuItem();
        itemGuardar = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        itemAnalizar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setResizeWeight(0.6);

        areaDeTextoPrincipal.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                areaDeTextoPrincipalCaretUpdate(evt);
            }
        });
        jScrollPane1.setViewportView(areaDeTextoPrincipal);

        jSplitPane1.setLeftComponent(jScrollPane1);

        areaDeConsola.setColumns(20);
        areaDeConsola.setRows(5);
        jScrollPane2.setViewportView(areaDeConsola);

        jTabbedPane1.addTab("Consola", jScrollPane2);

        tablaDeErrores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tablaDeErrores);

        jTabbedPane1.addTab("Reporte de Errores", jScrollPane3);

        tablaDeTokens.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tablaDeTokens);

        jTabbedPane1.addTab("Reporte de Tokens", jScrollPane4);

        jSplitPane1.setRightComponent(jTabbedPane1);

        lblEstado.setText("Linea: 1, Columna: 1");

        jMenu1.setText("Archivo");

        itemAbrir.setText("Abrir");
        itemAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAbrirActionPerformed(evt);
            }
        });
        jMenu1.add(itemAbrir);

        itemGuardar.setText("Guardar");
        itemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGuardarActionPerformed(evt);
            }
        });
        jMenu1.add(itemGuardar);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ejecutar");

        itemAnalizar.setText("Analizar");
        itemAnalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAnalizarActionPerformed(evt);
            }
        });
        jMenu2.add(itemAnalizar);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblEstado)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEstado)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemAbrirActionPerformed
        // Creacion del dialogo de selector de archivo
        JFileChooser selectorDeArchivo = new JFileChooser();
        selectorDeArchivo.setDialogTitle("Abrir archivo de entrada");
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de texto (.txt)", "txt");
        selectorDeArchivo.setFileFilter(filtro);
        
        int resultado = selectorDeArchivo.showOpenDialog(this);
        
        // Comprobacion de la respuesta del dialogo
        if (resultado == JFileChooser.APPROVE_OPTION)
        {
            Path rutaArchivo = selectorDeArchivo.getSelectedFile().toPath();
            try
            {
                String contenido = controladorArchivos.leerArchivo(rutaArchivo);
                areaDeTextoPrincipal.setText(contenido);
                lblEstado.setText("Archivo '" +rutaArchivo.getFileName()+ "' cargado.");
            }
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo: ", "Error de Apertura", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_itemAbrirActionPerformed

    private void itemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGuardarActionPerformed
        JFileChooser selectorDeArchivo = new JFileChooser();
        selectorDeArchivo.setDialogTitle("Guardar archivo");
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de Texto (.txt)", "txt");
        selectorDeArchivo.setFileFilter(filtro);
        
        int resultado = selectorDeArchivo.showSaveDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) 
        {
            File archivoAGuardar = selectorDeArchivo.getSelectedFile();
            String rutaAbsoluta = archivoAGuardar.getAbsolutePath();
            if (!rutaAbsoluta.endsWith(".txt")) 
            {
                archivoAGuardar = new File(rutaAbsoluta + ".txt");
            }
            Path rutaArchivo = archivoAGuardar.toPath();
            String contenido = areaDeTextoPrincipal.getText();
            try 
            {
                controladorArchivos.guardarArchivo(rutaArchivo, contenido);
                lblEstado.setText("Archivo '" + rutaArchivo.getFileName() + "' guardado.");

            } 
            catch (IOException ex) 
            {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage(), "Error de Guardado", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_itemGuardarActionPerformed

    private void itemAnalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemAnalizarActionPerformed
        
        String textoFuente = areaDeTextoPrincipal.getText(); // Obtener el texto del editor
        controladorAnalizador.analizar(textoFuente); // Enviar el texto para que se analice
        // Pedir los resultados
        ArrayList<Token> tokensEncontrados = controladorAnalizador.getListaTokens();
        ArrayList<ErrorLexico> erroresEncontrados = controladorAnalizador.getListaErrores();
        // Mostrar los resultados en las tablas
        actualizarTablaTokens(tokensEncontrados);
        actualizarTablaErrores(erroresEncontrados);
        // Actualizar la barra de estado
        lblEstado.setText("Análisis completado. Errores: " + erroresEncontrados.size());

        // Cambiar automáticamente a la pestaña de Errores si hay errores
        if (!erroresEncontrados.isEmpty()) 
        {
            jTabbedPane1.setSelectedComponent(jScrollPane3); 
        } 
        else 
        {
            jTabbedPane1.setSelectedComponent(jScrollPane4);
        }
    }//GEN-LAST:event_itemAnalizarActionPerformed

    private void areaDeTextoPrincipalCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_areaDeTextoPrincipalCaretUpdate
        if (areaDeTextoPrincipal == null || lblEstado == null) // Proteccion de error
        {
            return;
        }
        int pos = areaDeTextoPrincipal.getCaretPosition(); // Obtiene la posición exacta del cursor
        Element base = areaDeTextoPrincipal.getDocument().getDefaultRootElement(); // Obtiene el texto del JTextPane
        int linea = base.getElementIndex(pos) + 1; // Obtiene la fila
        Element elementoDeLinea = base.getElement(linea - 1); // La linea en la que esta
        int inicioDeLinea = elementoDeLinea.getStartOffset(); // La posicion donde inicia la linea
        int columna = (pos - inicioDeLinea) + 1; // Calcula la columna
        lblEstado.setText("Línea: " + linea + ", Columna: " + columna); // Actualiza la etiqueta de estado
    }//GEN-LAST:event_areaDeTextoPrincipalCaretUpdate

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaDeConsola;
    private javax.swing.JTextPane areaDeTextoPrincipal;
    private javax.swing.JMenuItem itemAbrir;
    private javax.swing.JMenuItem itemAnalizar;
    private javax.swing.JMenuItem itemGuardar;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JTable tablaDeErrores;
    private javax.swing.JTable tablaDeTokens;
    // End of variables declaration//GEN-END:variables
}
