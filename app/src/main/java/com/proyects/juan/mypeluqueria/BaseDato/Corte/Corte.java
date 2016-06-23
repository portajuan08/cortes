package com.proyects.juan.mypeluqueria.BaseDato.Corte;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.proyects.juan.mypeluqueria.BaseDato.Cliente.Cliente;
import com.proyects.juan.mypeluqueria.BaseDato.ObjetoConImagen;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by juan on 22/12/15.
 */
@DatabaseTable
public class Corte extends ObjetoConImagen implements Serializable {
    public static final String ID = "_id";
    public static final String OBSERVACION = "observacion";
    public static final String HORA = "hora";
    public static final String CLIENTE = "cliente";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(columnName = OBSERVACION)
    private String observacion;
    @DatabaseField(columnName = HORA, dataType = DataType.DATE_LONG)
    private Date hora;
    @DatabaseField(foreign = true, columnName = CLIENTE,foreignAutoRefresh = true)
    private Cliente cliente;

    public Cliente getCliente(){return cliente;}
    public void setCliente(Cliente oCliente){cliente = oCliente;}

    public Date getHora(){return hora;}
    public void setHora(Date oHora){hora = oHora;}

    public String getObservacion() {return observacion;}
    public void setObservacion(String sObservacion) {observacion = sObservacion;}

    public int getId(){return id;}
    public void setId(int iId){id = iId;}

    @Override
    public String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm", Locale.getDefault());
        String sHora = sdf.format(hora);
        return cliente.getNombre() +","+ sHora;
    }

    @Override
    public int getIdImage() {
        return cliente.getIdImage();
    }
}
