package com.example.examen1.tablas;

public class Contactos
{
    private Integer id_cont;
    private String nombre;
    private String telefono;
    private String nota;
    private byte[] foto;

    public Contactos()
    {
        //Constructor vacio
    }

    public Contactos(Integer id_cont, String nombre, String telefono, String nota, byte[] foto)
    {
        this.id_cont = id_cont;
        this.nombre = nombre;
        this.telefono = telefono;
        this.nota = nota;
        this.foto = foto;
    }

    public Integer getId_cont() {
        return id_cont;
    }

    public void setId_cont(Integer id_cont) {
        this.id_cont = id_cont;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}
