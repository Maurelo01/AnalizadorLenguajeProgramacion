package com.mycompany.analizadores.sintactico;

import java.util.HashMap;

public class TablaDeSimbolos 
{
    // Para guardar las Variables
    private HashMap<String, Object> tabla;
    public TablaDeSimbolos() 
    {
        this.tabla = new HashMap<>();
    }
    
    public boolean definirVariable(String nombre) // Verdadero: la variable se añadio | False: la variable ya existia
    {
        if (tabla.containsKey(nombre)) 
        {
            return false; // Error de variable ya declarada
        }
        tabla.put(nombre, null); // Se añade con valor nulo por defecto
        return true;
    }
    
    public boolean verificarExistencia(String nombre) // Verifica si una variable ya fue declarada
    {
        return tabla.containsKey(nombre);
    }
}
