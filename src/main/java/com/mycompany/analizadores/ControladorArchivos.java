package com.mycompany.analizadores;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ControladorArchivos 
{
    public String leerArchivo(Path ruta) throws IOException
    {
        return Files.readString(ruta); //Lee un archivo
    }
    
    public void guardarArchivo(Path ruta, String contenido) throws IOException
    {
        Files.writeString(ruta, contenido); // Guarda un archivo
    }
}
