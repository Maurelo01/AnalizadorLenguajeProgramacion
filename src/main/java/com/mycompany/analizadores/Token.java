package com.mycompany.analizadores;
public class Token 
{
    public TipoToken tipo;
    public String lexema;
    public int linea;
    public int columna;
    
    public Token(TipoToken tipo, String lexema, int linea, int columna)
    {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
    }
    
    // Para depurar
    @Override
    public String toString() 
    {
        return "Token {" +
                "tipo=" + tipo +
                ", lexema='" + lexema + '\'' +
                ", linea=" + linea +
                ", columna=" + columna +
                '}';
    }
}
