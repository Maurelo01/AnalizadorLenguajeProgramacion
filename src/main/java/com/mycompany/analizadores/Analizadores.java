package com.mycompany.analizadores;

import java.io.StringReader;
import javax.swing.SwingUtilities;

public class Analizadores 
{
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> // Abre la aplicacion principal
        {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
