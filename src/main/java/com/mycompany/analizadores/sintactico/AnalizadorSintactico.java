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
    private ArrayList<String> salidaConsola; // Para guardar la salida de ESCRIBIR
    
    public AnalizadorSintactico(ArrayList<Token> tokens) 
    {
        this.tokens = tokens;
        this.i = 0;
        this.tokenActual = tokens.get(i); // Obtiene el primer token
        this.listaErroresSintacticos = new ArrayList<>();
        this.tablaDeSimbolos = new TablaDeSimbolos();
        this.salidaConsola = new ArrayList<>();
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
        // Cuando i llega al tamaño o supera el del tokens.size
    }
    
    private void comparar(TipoToken tipoEsperado) 
    {
        if (tokenActual.getTipo() == tipoEsperado) 
        {
            consumirToken();
        } 
        else 
        {
            // Error de sintaxis
            reportarError("Se esperaba " + tipoEsperado + " pero se encontro " + tokenActual.getTipo());
        }
    }
    
    private void compararPalabraReservada(String lexema) 
    {
        if (esPalabraReservada(lexema)) 
        {
            consumirToken();
        } 
        else 
        {
            reportarError("Se esperaba la palabra reservada '" + lexema + "'.");
        }
    }
    
    private void compararPuntuacion(String lexema) 
    {
        if (tokenActual.getTipo() == TipoToken.PUNTUACION && tokenActual.getLexema().equals(lexema)) 
        {
            consumirToken();
        } 
        else 
        {
            reportarError("Se esperaba '" + lexema + "'.");
        }
    }
    
    private void compararAgrupacion(String lexema) 
    {
        if (tokenActual.getTipo() == TipoToken.AGRUPACION && tokenActual.getLexema().equals(lexema)) 
        {
            consumirToken();
        }
        else 
        {
            reportarError("Se esperaba '" + lexema + "'.");
        }
    }
    
    private void compararOperador(String lexema) 
    {
        if (tokenActual.getTipo() == TipoToken.OPERADOR && tokenActual.getLexema().equals(lexema)) 
        {
            consumirToken();
        } 
        else 
        {
            reportarError("Se esperaba '" + lexema + "'.");
        }
    }
    
    private void reportarError(String descripcion, int linea, int columna) 
    {
        this.listaErroresSintacticos.add(new ErrorSintactico(descripcion, linea, columna));
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
        consumirToken(); // Consumir ESCRIBIR
        compararAgrupacion("(");
        Object valor = expresion();
        if (valor != null) // Guardar salida
        {
            if (valor instanceof Double) // Convierte el valor a string y lo guarda
            {
                // Cambia numeros para que no muestre el .0 si son enteros
                Double num = (Double) valor;
                if (num % 1 == 0) 
                {
                    salidaConsola.add(String.valueOf(num.intValue()));
                } 
                else 
                {
                    salidaConsola.add(String.valueOf(num));
                }
            }
            else 
            {
                salidaConsola.add(valor.toString());
            }
        } 
        else 
        {
            salidaConsola.add("null"); // Imprime null si una variable no estaba inicializada
        }
        compararAgrupacion(")");
        compararPuntuacion(";");
    }
    
    private void asignacion() 
    {
        Token idToken = tokenActual;
        if (!tablaDeSimbolos.verificarExistencia(tokenActual.getLexema())) 
        {
            reportarError("Error: La variable '" + tokenActual.getLexema() + "' no ha sido declarada.", tokenActual.getLinea(), tokenActual.getColumna());
        }
        comparar(TipoToken.IDENTIFICADOR);
        compararOperador("=");
        Object valor = expresion();
        if (valor != null) // Asignar y validar tipo 
        {
            boolean exito = tablaDeSimbolos.asignarValor(idToken.getLexema(), valor);
            if (!exito) 
            {
                reportarError("Error: Tipo incompatible. No se puede asignar un '" + valor.getClass().getSimpleName() + "' a la variable '" + idToken.getLexema() + "'.", idToken.getLinea(), idToken.getColumna());
            }
        }
        compararPuntuacion(";");
    }
    
    private void definirVariable()
    {
        consumirToken();
        Token idToken = tokenActual; 
        comparar(TipoToken.IDENTIFICADOR);
        compararPalabraReservada("COMO");
        TablaDeSimbolos.TipoDeDato tipo = tipoDato();
        if (tipo != null) 
        {
            // Intenta definir la variable
            boolean exito = tablaDeSimbolos.definirVariable(idToken.getLexema(), tipo);
            if (!exito) 
            {
                // Error de variable ya declarada
                reportarError("Error: La variable '" + idToken.getLexema() + "' ya ha sido declarada.", idToken.getLinea(), idToken.getColumna());
            }
        }
        compararPuntuacion(";"); // Si la regla termina con ;
    }
    
    private Object terminoPrima(Object valorHeredado) 
    {
        if (esOperadorMultDiv()) 
        {
            Token operador = tokenActual;
            consumirToken(); // Consumir * o /
            // CALCULAR
            Object valorFactor = factor();Object resultado = realizarOperacion(valorHeredado, operador, valorFactor);
            return terminoPrima(resultado); // Recursividad
        }
        else
        {
            // caso Epsilon no hace nada
            return valorHeredado;
        }
    }
    
    private Object expresion() 
    {
        Object valorTermino = termino();
        return expresionPrima(valorTermino);
    }

    private Object termino() 
    {
        Object valorFactor = factor();
        return terminoPrima(valorFactor);
    }
    
    private Object expresionPrima(Object valorHeredado)
    {
        if (esOperadorSumaResta()) 
        {
            Token operador = tokenActual;
            consumirToken(); // Consumir + o -
            Object valorTermino = termino();
            Object resultado = realizarOperacion(valorHeredado, operador, valorTermino);
            return expresionPrima(resultado);
        }
        else
        {
            // Caso Epsilon no hace nada
            return valorHeredado;
        }
    }
    
    private Object factor() 
    {
        Object valor = null;
        if (esAgrupacion("(")) 
        {
            consumirToken(); // Consumir (
            valor = expresion();
            compararAgrupacion(")");
        }
        else if (tokenActual.getTipo() == TipoToken.IDENTIFICADOR) 
        {
            if (!tablaDeSimbolos.verificarExistencia(tokenActual.getLexema())) // Verifica que la variable exista si se usa en una expresion
            {
                reportarError("Error: La variable '" + tokenActual.getLexema() + "' no ha sido declarada.", tokenActual.getLinea(), tokenActual.getColumna());
            }
            else 
            {
                valor = tablaDeSimbolos.obtenerValor(tokenActual.getLexema());// Obtiene el valor actual de la variable
                if (valor == null) 
                {
                    reportarError("Error: La variable '" + tokenActual.getLexema() + "' no ha sido inicializada.", tokenActual.getLinea(), tokenActual.getColumna());
                }
            }
            consumirToken(); // Consumir identificador
        }
        else if (tokenActual.getTipo() == TipoToken.NUMERO) 
        {
            try // Convierte el lexema a un número double
            {
                valor = Double.parseDouble(tokenActual.getLexema());
            } 
            catch (NumberFormatException e) 
            {
                reportarError("Error: El número '" + tokenActual.getLexema() + "' es invalido.", tokenActual.getLinea(), tokenActual.getColumna());
                valor = 0.0; // Valor por defecto en caso de error
            }
            consumirToken();
        }
        else if (tokenActual.getTipo() == TipoToken.DECIMAL) 
        {
            try // Convierte el lexema a un número double
            {
                valor = Double.parseDouble(tokenActual.getLexema());
            } 
            catch (NumberFormatException e) 
            {
                reportarError("Error: El decimal '" + tokenActual.getLexema() + "' es invalido.", tokenActual.getLinea(), tokenActual.getColumna());
                valor = 0.0;
            }
            consumirToken();
        }
        else if (tokenActual.getTipo() == TipoToken.CADENA) 
        {
            // Quita las comillas del inicio y final
            String lexema = tokenActual.getLexema();
            valor = lexema.substring(1, lexema.length() - 1);
            consumirToken();
        }
        else 
        {
            reportarError("Se esperaba un IDENTIFICADOR, NUMERO, DECIMAL, CADENA o (");
        }
        return valor;
    }
    
    private Object realizarOperacion(Object val1, Token op, Object val2) 
    {
        if (val1 == null || val2 == null) 
        {
            return null; // Si uno de los valores es nulo no se opera
        }

        boolean esNumerico = val1 instanceof Double && val2 instanceof Double;
        boolean esCadena = val1 instanceof String || val2 instanceof String;

        switch (op.getLexema()) 
        {
            case "+":
                if (esNumerico) 
                {
                    return (Double) val1 + (Double) val2;
                }
                else if (esCadena) 
                {
                    return val1.toString() + val2.toString();
                } 
                else 
                {
                    reportarError("Error de Tipos: No se puede sumar '" + val1.getClass().getSimpleName() + "' con '" + val2.getClass().getSimpleName() + "'.", op.getLinea(), op.getColumna());
                    return null;
                }
            case "-":
            case "*":
            case "/":
                if (esNumerico) 
                {
                    if (op.getLexema().equals("-")) return (Double) val1 - (Double) val2;
                    if (op.getLexema().equals("*")) return (Double) val1 * (Double) val2;
                    if (op.getLexema().equals("/")) 
                    {
                        if ((Double) val2 == 0.0) 
                        {
                            reportarError("Error: División por cero.", op.getLinea(), op.getColumna());
                            return 0.0; // Evitar infinitos
                        }
                        return (Double) val1 / (Double) val2;
                    }
                }
                else 
                {
                    reportarError("Error de Tipos: Operador '" + op.getLexema() + "' solo aplica a números.", op.getLinea(), op.getColumna());
                    return null;
                }
            default:
                return null;
        }
    }
    
    private boolean esPalabraReservada(String lexema) 
    {
        return tokenActual.getTipo() == TipoToken.PALABRA_RESERVADA && tokenActual.getLexema().equalsIgnoreCase(lexema);
    }
    
    private boolean esTokenInstruccion() 
    {
        return esPalabraReservada("DEFINIR") || esPalabraReservada("ESCRIBIR") || tokenActual.getTipo() == TipoToken.IDENTIFICADOR;
    }
    
    private boolean esAgrupacion(String lexema) 
    {
        return tokenActual.getTipo() == TipoToken.AGRUPACION && tokenActual.getLexema().equals(lexema);
    }
    
    private boolean esOperadorSumaResta() 
    {
        return tokenActual.getTipo() == TipoToken.OPERADOR && (tokenActual.getLexema().equals("+") || tokenActual.getLexema().equals("-"));
    }
    
    private boolean esOperadorMultDiv() 
    {
        return tokenActual.getTipo() == TipoToken.OPERADOR && (tokenActual.getLexema().equals("*") || tokenActual.getLexema().equals("/"));
    }
    
    private TablaDeSimbolos.TipoDeDato tipoDato() 
    {
        if (esPalabraReservada("entero")) 
        {
            consumirToken();
            return TablaDeSimbolos.TipoDeDato.ENTERO;
        } 
        else if (esPalabraReservada("numero")) 
        {
            consumirToken();
            return TablaDeSimbolos.TipoDeDato.NUMERO;
        } 
        else if (esPalabraReservada("cadena")) 
        {
            consumirToken();
            return TablaDeSimbolos.TipoDeDato.CADENA;
        } 
        else 
        {
            reportarError("Se esperaba un tipo de dato (entero, numero o cadena)."); // Error de tipo de dato no valido
            return null;
        }
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
