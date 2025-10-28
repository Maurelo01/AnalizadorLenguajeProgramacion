package com.mycompany.analizadores;

import java.io.StringReader;

public class Analizadores 
{
    public static void main(String[] args) 
    {
        // Pruebas
        String textoEntrada = """
                              DEFINIR nombre COMO cadena;
                              // Esto es un comentario gggggg holi
                              nombre = "HOLA mundo";
                              
                              DEFINIR x COMO numero;
                              x = 5 + 2 * 3;
                              ESCRIBIR(x + 1); /* Esto es un comentario de bloque */
                              4545aw145.15 // es Error Lexico 
                              """;
        System.out.println("Iniciando Analisis Lexico");
        StringReader lector = new StringReader(textoEntrada);
        AnalizadorLexico analizador = new AnalizadorLexico(lector);
        try
        {
            Token tokenActual;
            do
            {
                tokenActual = analizador.siguienteToken();
                System.out.println(tokenActual.toString());
            }
            while(tokenActual.tipo != TipoToken.FIN);
            System.out.println("Fin del analisis Lexico");
        }
        catch (Exception e)
        {
            System.out.println("Ocurrio un error durante el analisis");
            e.printStackTrace();
        }
    }
}
