package com.mycompany.analizadores;

import java.io.StringReader;
import java.util.ArrayList;

public class ControladorAnalizador 
{
    // Listas para guardar los resultados del analisis
    private final ArrayList<Token> listaTokens;
    private final ArrayList<ErrorLexico> listaErrores;

    public ControladorAnalizador() 
    {
        this.listaTokens = new ArrayList<>();
        this.listaErrores = new ArrayList<>();
    }
    
    // Obtine el codigo y lo analiza
    public void analizar(String textoFuente)
    {
        // Limpiar de resultador anteriores
        this.listaTokens.clear();
        this.listaErrores.clear();
        // se crea el analizador lexico
        AnalizadorLexico lexer = new AnalizadorLexico(new StringReader(textoFuente));
        try
        {
            Token tokenActual;
            do
            {
                tokenActual = lexer.siguienteToken();
                if(tokenActual == null || tokenActual.getTipo() == TipoToken.FIN)
                {
                    if (tokenActual != null)
                    {
                        this.listaTokens.add(tokenActual);
                    }
                    break;
                }
                
                // Clasifica el token
                if (tokenActual.getTipo() == TipoToken.ERROR) // Si es error se añade a la lista de errores
                {
                    String desc = "Simbolo no reconocido por el lenguaje.";
                    // Logica de descripcion de error
                    if (tokenActual.getLexema().startsWith("."))
                    {
                        desc = "Decimal mal escrito (falta la parte entera).";
                    }
                    else if (tokenActual.getLexema().matches("\\d+[a-zA-Z].*"))
                    {
                        desc = "numero mal escrito (contiene caracteres).";
                    }
                    else if (tokenActual.getLexema().equals("Cadena no cerrada"))
                    {
                        desc = "La cadena no se cerro antes del fin de archivo.";
                    }
                    else if (tokenActual.getLexema().equals("Cadena no cerrada")) 
                    {
                        desc = "La cadena no se cerró antes del fin de archivo.";
                    } 
                    else if (tokenActual.getLexema().equals("Comentario de bloque no cerrado")) 
                    {
                        desc = "El comentario de bloque no se cerró antes del fin de archivo.";
                    }
                    this.listaErrores.add(new ErrorLexico(tokenActual.getLexema(), tokenActual.getLinea(), tokenActual.getColumna(), desc));
                }
                else // si es valido se añade a la lista de tokens
                {
                    this.listaTokens.add(tokenActual);
                }
            }
            while (true);
        }
        catch (Exception e)
        {
            System.err.println("Error no recuperado");
            e.printStackTrace();
        }
    }
    
    public ArrayList<Token> getListaTokens() 
    {
        return listaTokens;
    }

    public ArrayList<ErrorLexico> getListaErrores() 
    {
        return listaErrores;
    }
}
