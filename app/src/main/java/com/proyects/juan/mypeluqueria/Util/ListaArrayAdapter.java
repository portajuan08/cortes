package com.proyects.juan.mypeluqueria.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyects.juan.mypeluqueria.BaseDato.ObjetoConImagen;
import com.proyects.juan.mypeluqueria.R;

import java.util.List;

public class ListaArrayAdapter<T> extends ArrayAdapter<T> {

    public ListaArrayAdapter(Context context, List<T> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo con two_line_list_item.xml
            listItemView = inflater.inflate(
                    R.layout.item_list,
                    parent,
                    false);
        }

        //Obteniendo instancias de los text views
        TextView titulo = (TextView)listItemView.findViewById(R.id.text1);
        TextView subtitulo = (TextView)listItemView.findViewById(R.id.text2);
        ImageView imgImg = (ImageView) listItemView.findViewById(R.id.category);

        //Obteniendo instancia de la Tarea en la posición actual
        ObjetoConImagen item = (ObjetoConImagen)getItem(position);

        //Dividir la cadena en Nombre y Hora
        String cadenaBruta;
        String subCadenas [];
        String delimitador = ",";

        cadenaBruta = item.toString();
        subCadenas = cadenaBruta.split(delimitador, 2);

        /*REDONDEO IMAGEN*/
        Drawable originalDrawable = getContext().getResources().getDrawable(item.getIdImage());
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();
        //creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(getContext().getResources(), originalBitmap);

        //asignamos el CornerRadius
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());


        imgImg.setImageDrawable(roundedDrawable);
        //imgImg.setImageResource(item.getIdImage());
        titulo.setText(subCadenas[0]);
        subtitulo.setText(subCadenas[1]);

        //titulo.setTextSize(25);
        //subtitulo.setTextSize(20);
        //Devolver al ListView la fila creada
        return listItemView;

    }
}
