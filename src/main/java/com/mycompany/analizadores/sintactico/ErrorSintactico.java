package com.mycompany.analizadores.sintactico;
public class ErrorSintactico 
{
    private final String descripcion;
    private final int linea;
    private final int columna;

    public ErrorSintactico(String descripcion, int linea, int columna) 
    {
        this.descripcion = descripcion;
        this.linea = linea;
        this.columna = columna;
    }

    public String getDescripcion() 
    {
        return descripcion;
    }

    public int getLinea() 
    {
        return linea;
    }

    public int getColumna() 
    {
        return columna;
    }
    
    // Para la tabla de errores
    public Object[] toObjectRow() 
    {
        return new Object[] { descripcion, linea, columna };
    }

    @Override
    public String toString() 
    {
        return "ErrorSintactico{" + "desc=" + descripcion + ", linea=" + linea + ", col=" + columna + '}';
    }
}
