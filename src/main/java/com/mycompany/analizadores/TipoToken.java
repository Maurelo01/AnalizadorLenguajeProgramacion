package com.mycompany.analizadores;
public enum TipoToken 
{
    // Token - numero para citar
    // Palabras reservadas
    PALABRA_RESERVADA,  // 37 al 46
    
    // Tokens Principales
    IDENTIFICADOR,  // 31
    NUMERO, // 32
    DECIMAL,    // 33
    CADENA, // 34
    
    // Simbolos
    OPERADOR,   // 48
    PUNTUACION, // 47
    AGRUPACION,     //49
    
    // Comentarios 
    COMENTARIO_LINEA, // 50
    COMENTARIO_BLOQUE, // 51
    
    ERROR,  // Error - 29
    
    FIN // Fin del archivo
}
