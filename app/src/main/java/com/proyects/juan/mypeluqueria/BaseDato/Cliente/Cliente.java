package com.proyects.juan.mypeluqueria.BaseDato.Cliente;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.proyects.juan.mypeluqueria.BaseDato.ObjetoConImagen;
import com.proyects.juan.mypeluqueria.R;

import java.io.Serializable;

/**
 * Created by juan on 22/12/15.
 */
@DatabaseTable
public class Cliente extends ObjetoConImagen implements Serializable {

    public static final String ID = "_id";
    public static final String NOMBRE = "nombre";
    public static final String TELEFONO = "telefono";
    public static final String DIRECCION = "direccion";
    public static final String SEXO = "sexo";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(columnName = NOMBRE)
    private String nombre;
    @DatabaseField(columnName = TELEFONO)
    private String telefono;
    @DatabaseField(columnName = DIRECCION)
    private String direccion;
    @DatabaseField(columnName = SEXO)
    private int sexo;

    public String getNombre(){return nombre;}
    public void setNombre(String sNombre){nombre = sNombre;}

    public String getTelefono(){return telefono;}
    public void setTelefono(String sTelefono){telefono = sTelefono;}

    public String getDireccion(){return direccion;}
    public void setDireccion(String sDireccion){direccion = sDireccion;}

    public int getSexo(){return sexo;}
    public void setSexo(int iSexo){sexo = iSexo;}

    public int getId(){return id;}
    public void setId(int iId){id = iId;}

    @Override
    public String toString(){
        return getNombre() +","+ getTelefono() + " " + getDireccion();
    }

    @Override
    public int getIdImage() {
        return (getSexo() == 0) ? R.mipmap.ic_men : R.mipmap.ic_women;
    }
}
