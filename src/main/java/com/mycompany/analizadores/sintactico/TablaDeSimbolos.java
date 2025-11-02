package com.mycompany.analizadores.sintactico;

import java.util.ArrayList;
import java.util.HashMap;

public class TablaDeSimbolos 
{
    // Para guardar las Variables
    private HashMap<String, Simbolo> tabla;
    
     private class Simbolo  // para guardar la informacion de cada variable
    {
        TipoDeDato tipo;
        Object valor;

        Simbolo(TipoDeDato tipo, Object valor) 
        {
            this.tipo = tipo;
            this.valor = valor;
        }
    }
    
    public enum TipoDeDato 
    {
        ENTERO,
        NUMERO,
        CADENA
    } 
     
    public TablaDeSimbolos() 
    {
        this.tabla = new HashMap<String, Simbolo>();
    }
    
    public boolean definirVariable(String nombre, TipoDeDato tipo) // Verdadero: la variable se añadio | False: la variable ya existia
    {
        if (tabla.containsKey(nombre)) 
        {
            return false; // Error de variable ya declarada
        }
        tabla.put(nombre, new Simbolo(tipo, null)); // Se añade con valor nulo por defecto
        return true;
    }
    
    public boolean verificarExistencia(String nombre) // Verifica si una variable ya fue declarada
    {
        return tabla.containsKey(nombre);
    }
    
    public boolean asignarValor(String nombre, Object valor) // Asigna un valor a una variable declarada
    {
        if (!tabla.containsKey(nombre)) 
        {
            return false; // Error de variable no declarada
        }
        Simbolo s = tabla.get(nombre);
        TipoDeDato tipoEsperado = s.tipo;
        if (valor instanceof Integer) 
        {
            if (tipoEsperado == TipoDeDato.ENTERO) 
            {
                s.valor = valor;
                return true;
            }
            if (tipoEsperado == TipoDeDato.NUMERO) 
            {
                s.valor = ((Integer) valor).doubleValue();
                return true;
            }
            else 
            {
                return false; 
            }
        }
        else if (valor instanceof Double) 
        {
            if (tipoEsperado == TipoDeDato.NUMERO) 
            {
                s.valor = valor;
                return true;
            }
            else 
            {
                return false; 
            }
        }
        else if (valor instanceof String) 
        {
            if (tipoEsperado == TipoDeDato.CADENA) // El valor calculado es una CADENA
            {
                s.valor = valor;
                return true;
            }
            else 
            {
                return false;// Error: intenta asignar un string a un numero
            }
        }
        return false; // Tipos incompatibles
    }
   
    public Object obtenerValor(String nombre) // Obtiene el valor de una variable
    {
        if (!tabla.containsKey(nombre)) 
        {
            return null; // Error de variable no declarada
        }
        return tabla.get(nombre).valor;
    }
    
    public TipoDeDato obtenerTipo(String nombre) // Obtiene el tipo de una variable
    {
        if (!tabla.containsKey(nombre)) 
        {
            return null; // Error de variable no declarada
        }
        return tabla.get(nombre).tipo;
    }
    
    public ArrayList<Object[]> getDatosParaTabla() 
    {
        ArrayList<Object[]> datos = new ArrayList<>();
        for (String nombre : tabla.keySet()) 
        {
            Simbolo s = tabla.get(nombre);
            Object valor = (s.valor != null) ? s.valor : "null"; // Asegura de que el valor no sea nulo para mostrarlo
            datos.add(new Object[] { nombre, s.tipo.toString(), valor });
        }
        return datos;
    }
}