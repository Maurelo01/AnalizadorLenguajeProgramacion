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
       programa();
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
    
    private void programa() 
    {
        listaInstrucciones();
        comparar(TipoToken.FIN); // Al final de todo se espera el token de Fin de Archivo
    }
    
    private void instruccion() 
    {
        // Aplicar una regla basado en el token actual
        if (esPalabraReservada("DEFINIR")) 
        {
            definirVariable(); 
        } 
        else if (tokenActual.getTipo() == TipoToken.IDENTIFICADOR) 
        {
            asignacion(); 
        } 
        else if (esPalabraReservada("ESCRIBIR")) 
        {
            escritura(); 
        } 
        else 
        {
            // Si no es ninguna de las anteriores es error
            reportarError("Se esperaba 'DEFINIR', 'ESCRIBIR' o un IDENTIFICADOR.");
            consumirToken();
        }
    }
    
    private void listaInstrucciones()
    {
        if (esTokenInstruccion()) // Para decidir que regla usar se visualiza el token actual
        {
            instruccion();
            listaInstrucciones(); // Llamada recursiva para seguir analizando
        }
        else
        {
            // Se aplica la regla 3 epsilon o sea nada xd
        }
    }
    
    private void escritura() 
    {
        // Para despues xd
    }
    
    private void asignacion() 
    {
        // Para despues xd
    }
    
    private void definirVariable()
    {
        // Para despues xd
    }
    
    private boolean esPalabraReservada(String lexema) 
    {
        return tokenActual.getTipo() == TipoToken.PALABRA_RESERVADA && tokenActual.getLexema().equalsIgnoreCase(lexema);
    }
    
    private boolean esTokenInstruccion() 
    {
        return esPalabraReservada("DEFINIR") || esPalabraReservada("ESCRIBIR") || tokenActual.getTipo() == TipoToken.IDENTIFICADOR;
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
