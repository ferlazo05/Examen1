package com.example.examen1.configuraciones;

public class Transacciones
{
    //Nombre de la base de datos
    public static final String NameDatabase = "PM1E144";
    //Creación de las tablas de la base de datos
    public static final String tablaContactos = "contactos";
    public static final String tablaPaises = "paises";
    /*
        Campos especificos de la tabla contactos
    */
    public static final String id_cont = "id_cont";
    public static final String nombre = "nombre";
    public static final String telefono = "telefono";
    public static final String nota = "nota";
    public static final String foto = "foto";

    /*
        Campos especificos de la tabla paises
    */
    public static final String id_pais = "id_pais";
    public static final String pais = "pais";
    public static final String prefijo = "prefijo";

    /* Transacciones DDL (Data Definition Lenguage) */
    public static final String CreateTableContactos = "CREATE TABLE "+tablaContactos
            + "(id_cont INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "nombre TEXT, telefono TEXT, nota TEXT, foto BLOB)";

    public static final String CreateTablePaises = "CREATE TABLE "+tablaPaises
            + "(id_pais INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "pais TEXT, prefijo TEXT)";

    public static final String DropTableContactos = "DROP TABLE IF EXISTS "+tablaContactos;

    public static final String DropTablePaises = "DROP TABLE IF EXISTS "+tablaPaises;
}
