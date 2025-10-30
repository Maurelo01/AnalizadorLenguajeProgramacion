package com.mycompany.analizadores;
public class ErrorLexico 
{
    private final String lexema;
    private final int linea;
    private final int columna;
    private final String descripcion;
    
    public ErrorLexico(String lexema, int linea, int columna, String descripcion)
    {
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
        this.descripcion = descripcion;
    }
    
    public String getLexema()
    {
        return lexema;
    }
    
    public int getLinea()
    {
        return linea;
    }
    
    public int getColumna()
    {
        return columna;
    }
    
    public String getDescripcion()
    {
        return descripcion;
    }
    
    // Metodo para facilitar la creacion de filas para el Jtable
    public Object[] toObjectRow()
    {
        return new Object[] {lexema, linea, columna, descripcion};
    }
}
