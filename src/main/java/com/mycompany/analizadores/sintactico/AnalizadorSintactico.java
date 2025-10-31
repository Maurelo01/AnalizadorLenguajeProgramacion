package com.mycompany.analizadores.sintactico;

import com.mycompany.analizadores.TipoToken;
import com.mycompany.analizadores.Token;
import java.util.ArrayList;

public class AnalizadorSintactico 
{
    private ArrayList<Token> tokens;
    private int i; // indica el token actual
    private Token tokenActual;
    // Lista para guardar resultados
    private ArrayList<ErrorSintactico> listaErroresSintacticos;
    private TablaDeSimbolos tablaDeSimbolos;
    
    public AnalizadorSintactico(ArrayList<Token> tokens) 
    {
        this.tokens = tokens;
        this.i = 0;
        this.tokenActual = tokens.get(i); // Obtiene el primer token
        this.listaErroresSintacticos = new ArrayList<>();
        this.tablaDeSimbolos = new TablaDeSimbolos();
    }
    public void analizar() 
    {
       // Implementacion posterior
    }
    
    private void consumirToken() // Avanza al siguiente token
    {
        i++;
        if (i < tokens.size()) 
        {
            tokenActual = tokens.get(i);
        }
        // Cuando i llega al tamaño o supera del tokens.size
    }
    
    private void comparar(TipoToken tipoEsperado) // Compara el tipo de token para ver si es el esperado
    {
        if (tokenActual.getTipo() == tipoEsperado)
        {
            consumirToken();
        } 
        else 
        {
            // Error de sintaxis
            reportarError("Se esperaba " + tipoEsperado + " pero se encontró " + tokenActual.getTipo());
        }
    }
    
    private void reportarError(String descripcion) 
    {
        this.listaErroresSintacticos.add(new ErrorSintactico(descripcion, tokenActual.getLinea(), tokenActual.getColumna()));
    }
    
    public ArrayList<ErrorSintactico> getListaErroresSintacticos() 
    {
        return listaErroresSintacticos;
    }

    public TablaDeSimbolos getTablaDeSimbolos() 
    {
        return tablaDeSimbolos;
    }
}
