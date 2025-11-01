package com.mycompany.analizadores;

import com.mycompany.analizadores.sintactico.*;
import java.io.StringReader;
import java.util.ArrayList;

public class ControladorAnalizador 
{
    // Listas para guardar los resultados del analisis
    private final ArrayList<Token> listaTokens;
    private final ArrayList<ErrorLexico> listaErrores;
    private ArrayList<ErrorSintactico> listaErroresSintacticos;
    private ArrayList<String> salidaConsola;
    private TablaDeSimbolos tablaDeSimbolos;

    public ControladorAnalizador() 
    {
        this.listaTokens = new ArrayList<>();
        this.listaErrores = new ArrayList<>();
        this.listaErroresSintacticos = new ArrayList<>();
        this.tablaDeSimbolos = new TablaDeSimbolos();
        this.salidaConsola = new ArrayList<>();
    }
    
    // Obtine el codigo y lo analiza
    public void analizar(String textoFuente)
    {
        // PARTE DEL ANALIZADOR LEXICO
        
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
                    this.listaErrores.add(new ErrorLexico(tokenActual.getLexema(), tokenActual.getLinea(), tokenActual.getColumna(), desc));
                }
                else if (tokenActual.getTipo() == TipoToken.ERROR_NO_CERRADO)
                {
                    String desc;
                    if (tokenActual.getLexema().startsWith("\"")) 
                    {
                        desc = "La cadena no se cerró antes del fin de archivo.";
                    } 
                    else if (tokenActual.getLexema().startsWith("/*"))
                    {
                        desc = "Comentario de bloque no cerrado.";
                    }
                    else 
                    {
                        desc = "Token no cerrado.";
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
        
        // PARTE DEL ANALIZADOR SINTACTICO
        
        this.listaErroresSintacticos.clear();
        this.tablaDeSimbolos = new TablaDeSimbolos();
        this.salidaConsola.clear();
        
        if (this.listaErrores.isEmpty() && !this.listaTokens.isEmpty()) 
        {
            AnalizadorSintactico parser = new AnalizadorSintactico(this.listaTokens); // Crea el parser con la lista de tokens
            parser.analizar(); // Ejecuta el analisis
            // Recoge los resultados del parser
            this.listaErroresSintacticos = parser.getListaErroresSintacticos();
            this.tablaDeSimbolos = parser.getTablaDeSimbolos();
            this.salidaConsola = parser.getSalidaConsola();
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
    
    public ArrayList<ErrorSintactico> getListaErroresSintacticos() 
    {
        return listaErroresSintacticos;
    }

    public TablaDeSimbolos getTablaDeSimbolos() 
    {
        return tablaDeSimbolos;
    }
    
    public ArrayList<String> getSalidaConsola() 
    {
        return salidaConsola;
    }
}
