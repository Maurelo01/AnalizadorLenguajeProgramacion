package com.mycompany.analizadores;
public class VentanaPrincipal extends javax.swing.JFrame 
{    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());
    public VentanaPrincipal() 
    {
        initComponents();
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
        jMenu1.add(itemAbrir);

        itemGuardar.setText("Guardar");
        jMenu1.add(itemGuardar);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ejecutar");

        itemAnalizar.setText("Analizar");
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
