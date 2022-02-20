package com.example.examen1.tablas;

public class Paises
{
    private Integer id_pais;
    private String pais;
    private String prefijo;

    public Paises()
    {
        //Constructor vacio
    }

    public Paises(Integer id_pais, String pais, String prefijo) {
        this.id_pais = id_pais;
        this.pais = pais;
        this.prefijo = prefijo;
    }

    public Integer getId_pais() {
        return id_pais;
    }

    public void setId_pais(Integer id_pais) {
        this.id_pais = id_pais;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }
}
