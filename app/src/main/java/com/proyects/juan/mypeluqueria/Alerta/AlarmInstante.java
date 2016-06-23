package com.proyects.juan.mypeluqueria.Alerta;

/**
 * Created by juan on 26/12/15.
 */

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.proyects.juan.mypeluqueria.Activity.VerCorte;
import com.proyects.juan.mypeluqueria.BaseDato.Cliente.Cliente;
import com.proyects.juan.mypeluqueria.BaseDato.Corte.Corte;
import com.proyects.juan.mypeluqueria.R;


public class AlarmInstante extends WakefulBroadcastReceiver {


    // Variables de la notificacion
    NotificationManager nm;
    Notification notif;
    static String ns = Context.NOTIFICATION_SERVICE;
    private static final String PARAMETRO_CLIENTE = "CLIENTE";
    private static final String PARAMETRO_CORTE = "CORTE";

    //Defino los iconos de la notificacion en la barra de notificacion
    int icono_r = R.mipmap.ic_launcher;

    @Override
    public void onReceive(final Context context, Intent intent) {
        // Inicio el servicio de notificaciones accediendo al servicio
        nm = (NotificationManager) context.getSystemService(ns);
        Cliente cliente = (Cliente) intent.getExtras().getSerializable(PARAMETRO_CLIENTE);
        Corte corte = (Corte) intent.getExtras().getSerializable(PARAMETRO_CORTE);

        // Definimos la accion de la pulsacion sobre la notificacion (esto es opcional)
        //Context context = getApplicationContext();
        Intent notificationIntent = new Intent(context, VerCorte.class);
        notificationIntent.putExtra(PARAMETRO_CLIENTE, cliente);
        notificationIntent.putExtra(PARAMETRO_CORTE, corte);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notif = new Notification.Builder(context).setContentTitle("Turno de : " + cliente.getNombre()).
                                                      setContentText("Direccion : " + cliente.getDireccion()).
                                                      setSmallIcon(icono_r).
                                                      setContentIntent(contentIntent).
                                                      build();
        }
        // Lanzo la notificacion creada en el paso anterior
        Uri notifRin = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context,notifRin);
        r.play();
        nm.notify(1, notif);
        setResultCode(Activity.RESULT_OK);
    }
}

