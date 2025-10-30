package com.mycompany.analizadores;

%%

%class AnalizadorLexico
%public
%unicode
%caseless  // ignora mayusculas/minusculas
%line
%column
%char

%function siguienteToken
%type Token

%{
    private StringBuilder stringAcumulado = new StringBuilder(); // Construye cadenas y comentarios
%}

// Declaraciones de estados
//Tokens que ocupan muchas lineas o reglas
%state CADENA
%state COMENTARIO_BLOQUE

// Expresiones regulares
Letra = [a-zA-Z]
Digito = [0-9]
Espacio = [ \t\r\n]+

// Definiciones de tokens
Identificador = {Letra}({Letra}|{Digito})*
Numero = {Digito}+
Decimal = {Digito}+"."{Digito}+

// Fin de Linea para comentarios
FinLinea = \r|\n|\r\n

//Caracteres no permitidos  no comillas por el estado CADENA
ErrorCaracter = [^a-zA-Z0-9\ \t\r\n.,;:\+\-\*\/\%=\(\)\[\]\{\}]

ErrorNumeroCaracter = {Digito}+{Letra}({Letra}|{Digito})*
ErrorDigito = "."{Digito}+ | {Digito}+"."{Digito}+"."{Digito}+

%%

// REGLAS LEXICAS
// Estado inicial

<YYINITIAL>
{
    // Ignorar espacios en blanco
    {Espacio} {/* ignorar */}

    // Comentarios
    "//".*
    {
        return new Token(TipoToken.COMENTARIO_LINEA, yytext(), yyline + 1, yycolumn + 1); 
    }

    "/*" 
    {
        stringAcumulado.setLength(0); // Limpia el buffer
        stringAcumulado.append(yytext()); // Añade el /*
        yybegin(COMENTARIO_BLOQUE); 
    }

    // Palabras reservadas
    ("Si"|"entonces"|"entero"|"numero"|"cadena"|"ESCRIBIR"|"DEFINIR"|"COMO") 
    { 
        return new Token(TipoToken.PALABRA_RESERVADA, yytext(), yyline + 1, yycolumn + 1); 
    }
    
    // Tokens Principales 
    {ErrorNumeroCaracter} 
    {
        return new Token(TipoToken.ERROR, yytext(), yyline + 1, yycolumn + 1);
    }
    {ErrorDigito} 
    {
        return new Token(TipoToken.ERROR, yytext(), yyline + 1, yycolumn + 1);
    }
    {Decimal} 
    { 
        return new Token(TipoToken.DECIMAL, yytext(), yyline + 1, yycolumn + 1); 
    }
    {Numero} 
    { 
        return new Token(TipoToken.NUMERO, yytext(), yyline + 1, yycolumn + 1); 
    }
    {Identificador} 
    { 
        return new Token(TipoToken.IDENTIFICADOR, yytext(), yyline + 1, yycolumn + 1); 
    }

    // Cadena de inicio de estado
    \" 
    {
        stringAcumulado.setLength(0); // Limpia el buffer
        stringAcumulado.append(yytext()); // Añade el /*
        yybegin(CADENA); 
    }
    
    // Simbolos
    // Operadores
    ("+"|"-"|"*"|"/"|"%"|"=") 
    { 
        return new Token(TipoToken.OPERADOR, yytext(), yyline + 1, yycolumn + 1); 
    }

    // Puntuacion
    ("."|","|";"|":") 
    { 
        return new Token(TipoToken.PUNTUACION, yytext(), yyline + 1, yycolumn + 1); 
    }
    
    // Agrupacion
    ("("|")"|"["|"]"|"{"|"}") 
    { 
        return new Token(TipoToken.AGRUPACION, yytext(), yyline + 1, yycolumn + 1); 
    }

    // Errores Lexicos 
    {ErrorCaracter} 
    { 
        return new Token(TipoToken.ERROR, yytext(), yyline + 1, yycolumn + 1); 
    }
    <<EOF>> 
    {
        return new Token(TipoToken.FIN, "EOF", yyline + 1, yycolumn + 1);
    }
}

<COMENTARIO_BLOQUE> 
{
    [^*] | "*"[^/] // Acumula el texto del comentario
    {
        stringAcumulado.append(yytext());
    }
    
    "*/" 
    { 
        stringAcumulado.append(yytext()); // Añade el */
        yybegin(YYINITIAL); 
        return new Token(TipoToken.COMENTARIO_BLOQUE, stringAcumulado.toString(), yyline + 1, yycolumn + 1); 
    }
    <<EOF>>
    {
        yybegin(YYINITIAL);
        return new Token(TipoToken.ERROR, stringAcumulado.toString(), yyline + 1, yycolumn + 1);
    }
}

<CADENA> 
{
    [^\"\n\r]+ // Acumula el texto de la cadena
    {
        stringAcumulado.append(yytext());
    }
    // Fin de cadena
    \" 
    {
        stringAcumulado.append(yytext()); // Añade las "
        yybegin(YYINITIAL); 
        return new Token(TipoToken.CADENA, stringAcumulado.toString(), yyline + 1, yycolumn + 1); 
    }

    // Error de salto de linea en cadena
    {FinLinea} 
    { 
        yybegin(YYINITIAL); 
        return new Token(TipoToken.ERROR, "Salto de linea en cadena", yyline + 1, yycolumn + 1); 
    }
    <<EOF>> 
    {
        yybegin(YYINITIAL);
        return new Token(TipoToken.ERROR, stringAcumulado.toString(), yyline + 1, yycolumn + 1);
    }
}

// Manejo de errores de fin de archivo y no capturados
. { return new Token(TipoToken.ERROR, yytext(), yyline + 1, yycolumn + 1); }