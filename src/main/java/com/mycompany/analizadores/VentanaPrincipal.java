package com.mycompany.analizadores;

import com.mycompany.analizadores.sintactico.ErrorSintactico;
import com.mycompany.analizadores.sintactico.TablaDeSimbolos;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class VentanaPrincipal extends javax.swing.JFrame 
{    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());
    private final ControladorArchivos controladorArchivos;
    private final ControladorAnalizador controladorAnalizador;
    // Para el colorizado
    private final StyledDocument docEstilizado;
    private final DocumentListener oyenteDocumento;
    // Estilos de color
    private final AttributeSet estiloDefault;
    private final AttributeSet estiloReservada;
    private final AttributeSet estiloPuntuacion;
    private final AttributeSet estiloIdentificador;
    private final AttributeSet estiloNumero;
    private final AttributeSet estiloDecimal;
    private final AttributeSet estiloComentario;
    private final AttributeSet estiloOperador;
    private final AttributeSet estiloAgrupacion;
    private final AttributeSet estiloError;
    private final AttributeSet estiloCadena;
    
    private final Highlighter.HighlightPainter resaltador; // Resaltador para la busqueda
    
    public VentanaPrincipal() 
    {
        initComponents();
        this.controladorArchivos = new ControladorArchivos();
        this.controladorAnalizador = new ControladorAnalizador();
        this.setLocationRelativeTo(null);
        this.docEstilizado = this.areaDeTextoPrincipal.getStyledDocument();
        StyleContext contexto = StyleContext.getDefaultStyleContext(); // Define los colores
        estiloDefault = contexto.addAttribute(contexto.getEmptySet(), StyleConstants.Foreground, Color.BLACK); // negro para el estilo por default
        estiloReservada = contexto.addAttribute(contexto.getEmptySet(), StyleConstants.Foreground, new Color(0, 0, 204)); // Azul para palabras reservadas
        estiloPuntuacion = contexto.addAttribute(contexto.getEmptySet(), StyleConstants.Foreground, new Color(255, 0, 128)); // Rosado para palabras puntuacion
        estiloIdentificador = contexto.addAttribute(contexto.getEmptySet(), StyleConstants.Foreground, new Color(139, 69, 19)); // Cafes para identificadores
        estiloNumero = contexto.addAttribute(contexto.getEmptySet(), StyleConstants.Foreground, Color.GREEN); // Verde para numeros
        estiloDecimal = contexto.addAttribute(contexto.getEmptySet(), StyleConstants.Foreground, Color.BLACK); // Verde para decimales 
        estiloComentario = contexto.addAttribute(contexto.getEmptySet(), StyleConstants.Foreground, new Color(0, 100, 0)); // Verde oscuro para Comentarios
        estiloOperador = contexto.addAttribute(contexto.getEmptySet(), StyleConstants.Foreground, new Color(255, 140, 0)); // Naranja para operador
        estiloAgrupacion = contexto.addAttribute(contexto.getEmptySet(), StyleConstants.Foreground, new Color(128, 0, 128)); // Morado para agrupacion
        estiloError = contexto.addAttribute(contexto.getEmptySet(), StyleConstants.Foreground, Color.RED); // Rojo para Errores 
        estiloCadena = contexto.addAttribute(contexto.getEmptySet(), StyleConstants.Foreground, new Color(0, 100, 0));// Verde oscuro para Cadenas
        resaltador = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW); // Color amarillo para busqueda
        
        this.oyenteDocumento = new DocumentListener() // Oyente para los cambios del documento
        {
            @Override
            public void insertUpdate(DocumentEvent e) 
            {
                // Se disparó porque el usuario escribió algo
                colorearDocumento();
            }
            @Override
            public void removeUpdate(DocumentEvent e) 
            {
                // Se disparó porque el usuario borró algo
                colorearDocumento();
            }
            @Override
            public void changedUpdate(DocumentEvent e) 
            {
                // (Generalmente para cambios de estilo, no de texto)
            }
        };
        this.docEstilizado.addDocumentListener(oyenteDocumento);
    }
    
    private AttributeSet obtenerEstiloParaToken(TipoToken tipo) 
    {
        switch (tipo) 
        {
            case PALABRA_RESERVADA:
                return estiloReservada;
            case PUNTUACION:
                return estiloPuntuacion;
            case IDENTIFICADOR:
                return estiloIdentificador;
            case NUMERO:
                return estiloNumero;
            case DECIMAL:
                return estiloDecimal;
            case CADENA:
                return estiloCadena; 
            case COMENTARIO_LINEA:
            case COMENTARIO_BLOQUE:
                return estiloComentario;
            case OPERADOR:
                return estiloOperador;
            case AGRUPACION:
                return estiloAgrupacion;
            case ERROR:
            case ERROR_NO_CERRADO:
                return estiloError;
            default:
                return estiloDefault; 
        }
    }
    
    private void colorearDocumento() 
    {
        SwingUtilities.invokeLater(() -> 
        {
            removerResaltadosBusqueda();
            try 
            {
                int posCursor = areaDeTextoPrincipal.getCaretPosition(); // Guardar la posición del cursor
                String texto = docEstilizado.getText(0, docEstilizado.getLength()); // Obtener todo el texto del editor
                Element base = docEstilizado.getDefaultRootElement(); // Obtener la estructura de linea
                // ENviar el texto para el analisis
                controladorAnalizador.analizar(texto);
                ArrayList<Token> tokensValidos = controladorAnalizador.getListaTokens();
                ArrayList<ErrorLexico> tokensError = controladorAnalizador.getListaErrores();
                ArrayList<Token> todosLosTokens = new ArrayList<>(tokensValidos); 
                
                for (ErrorLexico error : tokensError) // Convierte los erreres lexicos a token para ser pintados
                {
                    todosLosTokens.add(new Token(TipoToken.ERROR, error.getLexema(), error.getLinea(), error.getColumna()));
                }
                
                docEstilizado.removeDocumentListener(oyenteDocumento); // se desactiva el oyente temporalmente para evitar bucle infinito
                docEstilizado.setCharacterAttributes(0, texto.length(), estiloDefault, true); // resetea todos los colores a default
                
                for (Token token : todosLosTokens) // pasar por los tokens y aplicar el color
                {
                    if (token.getTipo() == TipoToken.FIN) continue; // Ignora el token EOF
                    // calcula la posicion exacta del token
                    Element lineaEl = base.getElement(token.getLinea() - 1); // Línea
                    int inicioDeLinea = lineaEl.getStartOffset();
                    int inicioToken = inicioDeLinea + token.getColumna() - 1; // Columna
                    int longitudToken = token.getLexema().length();
                    AttributeSet estilo = obtenerEstiloParaToken(token.getTipo()); // Obteniene el color para este token
                    docEstilizado.setCharacterAttributes(inicioToken, longitudToken, estilo, true); // Aplica el color
                }
                docEstilizado.addDocumentListener(oyenteDocumento); // se reactiva el oyente
                areaDeTextoPrincipal.setCaretPosition(posCursor); // restaura la posicion del cursor
            } 
            catch (BadLocationException ex) 
            {
                System.err.println("Error al colorear el documento: " + ex.getMessage());
            } 
            catch (Exception ex) 
            {
                System.err.println("Error inesperado en colorearDocumento: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
    
    private void actualizarTablaErroresSintacticos(ArrayList<ErrorSintactico> errores) // LLena la tabla de errores sintacticos
    {
        String[] columnas = {"Descripción", "Línea", "Columna"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };
        for (ErrorSintactico error : errores) 
        {
            modelo.addRow(error.toObjectRow());
        }
        this.tablaErroresSintacticos.setModel(modelo);
    }
    
    private void actualizarTablaDeSimbolos(TablaDeSimbolos tabla) // Llena la tabla de simbolos
    {
        String[] columnas = {"Nombre", "Tipo", "Valor"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };
        ArrayList<Object[]> datos = tabla.getDatosParaTabla();
        for (Object[] fila : datos) 
        {
            modelo.addRow(fila);
        }
        this.tablaDeSimbolosUI.setModel(modelo);
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
    
    private void actualizarTablaErrores(ArrayList<ErrorLexico> errores) // Llena la tabla de errors
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
    
    private void actualizarTablaRecuento(Map<String, ControladorAnalizador.ConteoLexema> conteo) // LLena la tabla de lexemas 
    {
        String[] columnas = {"Lexema", "Tipo", "Cantidad"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };
        for (Map.Entry<String, ControladorAnalizador.ConteoLexema> entrada : conteo.entrySet()) 
        {
            String lexema = entrada.getKey();
            ControladorAnalizador.ConteoLexema datos = entrada.getValue();
            modelo.addRow(new Object[] { lexema, datos.getTipo().toString(), datos.getConteo() });
        }

        this.tablaRecuentoLexemas.setModel(modelo);
    }
    
    private void removerResaltadosBusqueda() 
    {
        Highlighter highlighter = areaDeTextoPrincipal.getHighlighter();
        Highlighter.Highlight[] highlights = highlighter.getHighlights();

        for (Highlighter.Highlight h : highlights) 
        {
            // Solo remueve los resaltados que sean del resaltador de busqueda
            if (h.getPainter() == resaltador) 
            {
                highlighter.removeHighlight(h);
            }
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitConsolaReportes = new javax.swing.JSplitPane();
        scrollAreaDeTexto = new javax.swing.JScrollPane();
        areaDeTextoPrincipal = new javax.swing.JTextPane();
        tabbedDeReportes = new javax.swing.JTabbedPane();
        scrollConsola = new javax.swing.JScrollPane();
        areaDeConsola = new javax.swing.JTextArea();
        scrollErrores = new javax.swing.JScrollPane();
        tablaDeErrores = new javax.swing.JTable();
        scrollTokens = new javax.swing.JScrollPane();
        tablaDeTokens = new javax.swing.JTable();
        scrollErroresSintacticos = new javax.swing.JScrollPane();
        tablaErroresSintacticos = new javax.swing.JTable();
        scrollSimbolos = new javax.swing.JScrollPane();
        tablaDeSimbolosUI = new javax.swing.JTable();
        scrollRecuentoLexemas = new javax.swing.JScrollPane();
        tablaRecuentoLexemas = new javax.swing.JTable();
        lblEstado = new javax.swing.JLabel();
        lblBusqueda = new javax.swing.JLabel();
        menuBarOpciones = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        itemAbrir = new javax.swing.JMenuItem();
        itemGuardar = new javax.swing.JMenuItem();
        menuAnalizar = new javax.swing.JMenu();
        itemAnalizar = new javax.swing.JMenuItem();
        menuBuscar = new javax.swing.JMenu();
        itemBuscar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        splitConsolaReportes.setResizeWeight(0.6);

        areaDeTextoPrincipal.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                areaDeTextoPrincipalCaretUpdate(evt);
            }
        });
        scrollAreaDeTexto.setViewportView(areaDeTextoPrincipal);

        splitConsolaReportes.setLeftComponent(scrollAreaDeTexto);

        areaDeConsola.setColumns(20);
        areaDeConsola.setRows(5);
        scrollConsola.setViewportView(areaDeConsola);

        tabbedDeReportes.addTab("Consola", scrollConsola);

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
        scrollErrores.setViewportView(tablaDeErrores);

        tabbedDeReportes.addTab("Reporte de Errores", scrollErrores);

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
        scrollTokens.setViewportView(tablaDeTokens);

        tabbedDeReportes.addTab("Reporte de Tokens", scrollTokens);

        tablaErroresSintacticos.setModel(new javax.swing.table.DefaultTableModel(
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
        scrollErroresSintacticos.setViewportView(tablaErroresSintacticos);

        tabbedDeReportes.addTab("Errores Sintácticos", scrollErroresSintacticos);

        tablaDeSimbolosUI.setModel(new javax.swing.table.DefaultTableModel(
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
        scrollSimbolos.setViewportView(tablaDeSimbolosUI);

        tabbedDeReportes.addTab("Tabla de Variables", scrollSimbolos);

        tablaRecuentoLexemas.setModel(new javax.swing.table.DefaultTableModel(
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
        scrollRecuentoLexemas.setViewportView(tablaRecuentoLexemas);

        tabbedDeReportes.addTab("Recuento de Lexemas", scrollRecuentoLexemas);

        splitConsolaReportes.setRightComponent(tabbedDeReportes);

        lblEstado.setText("Linea: 1, Columna: 1");

        menuArchivo.setText("Archivo");

        itemAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itemAbrir.setText("Abrir");
        itemAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAbrirActionPerformed(evt);
            }
        });
        menuArchivo.add(itemAbrir);

        itemGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itemGuardar.setText("Guardar");
        itemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGuardarActionPerformed(evt);
            }
        });
        menuArchivo.add(itemGuardar);

        menuBarOpciones.add(menuArchivo);

        menuAnalizar.setText("Ejecutar");

        itemAnalizar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        itemAnalizar.setText("Analizar");
        itemAnalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAnalizarActionPerformed(evt);
            }
        });
        menuAnalizar.add(itemAnalizar);

        menuBarOpciones.add(menuAnalizar);

        menuBuscar.setText("Buscar");

        itemBuscar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itemBuscar.setText("Buscar Palabra");
        itemBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemBuscarActionPerformed(evt);
            }
        });
        menuBuscar.add(itemBuscar);

        menuBarOpciones.add(menuBuscar);

        setJMenuBar(menuBarOpciones);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(splitConsolaReportes, javax.swing.GroupLayout.DEFAULT_SIZE, 1338, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEstado)
                            .addComponent(lblBusqueda))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitConsolaReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEstado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(lblBusqueda)
                .addContainerGap())
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
        // Pedir los resultados lexicos
        ArrayList<Token> tokensEncontrados = controladorAnalizador.getListaTokens();
        ArrayList<ErrorLexico> erroresEncontrados = controladorAnalizador.getListaErrores();
        // Pedir los resultados sintáctico
        ArrayList<ErrorSintactico> erroresSintacticos = controladorAnalizador.getListaErroresSintacticos();
        ArrayList<String> salidaConsola = controladorAnalizador.getSalidaConsola();
        // Mostrar los resultados en las tablas
        actualizarTablaTokens(tokensEncontrados);
        actualizarTablaErrores(erroresEncontrados);
        actualizarTablaErroresSintacticos(erroresSintacticos);
        actualizarTablaDeSimbolos(controladorAnalizador.getTablaDeSimbolos());
        actualizarTablaRecuento(controladorAnalizador.getRecuentoLexemas());
        
        areaDeConsola.setText(""); // Limpiar consola anterior
        for (String linea : salidaConsola) 
        {
            areaDeConsola.append(linea + "\n");
        }
        
        // Actualizar la barra de estado
        lblEstado.setText("Análisis completado. Errores: " + erroresEncontrados.size());

        // Cambiar automáticamente a la pestaña de Errores si hay errores
        if (!erroresEncontrados.isEmpty()) 
        {
            tabbedDeReportes.setSelectedComponent(scrollErrores); 
        } 
        else if (!erroresSintacticos.isEmpty()) 
        {
            tabbedDeReportes.setSelectedComponent(scrollErroresSintacticos); 
        }
        else 
        {
            tabbedDeReportes.setSelectedComponent(scrollConsola);
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

    private void itemBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemBuscarActionPerformed
        removerResaltadosBusqueda();
        String palabraBuscar = JOptionPane.showInputDialog(this, "Ingrese el texto que desea buscar:", "Buscar Patron", JOptionPane.PLAIN_MESSAGE); // Pide al usuario la palabra a buscar
        if (palabraBuscar == null || palabraBuscar.isEmpty()) // Cancela si no escribe nada
        {
            return;
        }
        try 
        {
            Highlighter highlighter = areaDeTextoPrincipal.getHighlighter();
            String textoCompleto = areaDeTextoPrincipal.getDocument().getText(0, areaDeTextoPrincipal.getDocument().getLength());
            // Bucle de busqueda
            int index = -1;
            int count = 0;
            while ((index = textoCompleto.indexOf(palabraBuscar, index + 1)) != -1) 
            {
                highlighter.addHighlight(index, index + palabraBuscar.length(), resaltador); // Aplicar el resaltado
                count++;
            }
            // Mostrar al usuario
            if (count == 0) 
            {
                lblBusqueda.setText("No se encontraron coincidencias para '" + palabraBuscar + "'.");
            } 
            else 
            {
                lblBusqueda.setText("Se encontraron " + count + " coincidencias.");
            }

        } 
        catch (BadLocationException ex) 
        {
            System.err.println("Error al resaltar busqueda: " + ex.getMessage());
        }
    }//GEN-LAST:event_itemBuscarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaDeConsola;
    private javax.swing.JTextPane areaDeTextoPrincipal;
    private javax.swing.JMenuItem itemAbrir;
    private javax.swing.JMenuItem itemAnalizar;
    private javax.swing.JMenuItem itemBuscar;
    private javax.swing.JMenuItem itemGuardar;
    private javax.swing.JLabel lblBusqueda;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JMenu menuAnalizar;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenuBar menuBarOpciones;
    private javax.swing.JMenu menuBuscar;
    private javax.swing.JScrollPane scrollAreaDeTexto;
    private javax.swing.JScrollPane scrollConsola;
    private javax.swing.JScrollPane scrollErrores;
    private javax.swing.JScrollPane scrollErroresSintacticos;
    private javax.swing.JScrollPane scrollRecuentoLexemas;
    private javax.swing.JScrollPane scrollSimbolos;
    private javax.swing.JScrollPane scrollTokens;
    private javax.swing.JSplitPane splitConsolaReportes;
    private javax.swing.JTabbedPane tabbedDeReportes;
    private javax.swing.JTable tablaDeErrores;
    private javax.swing.JTable tablaDeSimbolosUI;
    private javax.swing.JTable tablaDeTokens;
    private javax.swing.JTable tablaErroresSintacticos;
    private javax.swing.JTable tablaRecuentoLexemas;
    // End of variables declaration//GEN-END:variables
}
