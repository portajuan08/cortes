package com.proyects.juan.mypeluqueria.Activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.proyects.juan.mypeluqueria.Alerta.AlarmInstante;
import com.proyects.juan.mypeluqueria.BaseDato.Cliente.Cliente;
import com.proyects.juan.mypeluqueria.BaseDato.Corte.Corte;
import com.proyects.juan.mypeluqueria.BaseDato.DBHelper;
import com.proyects.juan.mypeluqueria.R;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.proyects.juan.mypeluqueria.Util.SIConstantes.ACCION;
import static com.proyects.juan.mypeluqueria.Util.SIConstantes.ACCION_ELEJIR_CLIENTE;
import static com.proyects.juan.mypeluqueria.Util.SIConstantes.PARAMETRO_CLIENTE;
import static com.proyects.juan.mypeluqueria.Util.SIConstantes.PARAMETRO_CORTE;

public class AgregarCorte extends AppCompatActivity {

    private static final String TAG = "APP_PELU";
    private static final int CODIGO_ACTIVIDAD_AGREGAR_CLIENTE = 1;
    private Cliente cliente;
    private DBHelper mDBHelper;
    private TextView mDateDisplay;
    private TextView mTimeDisplay;
    private TextView tvNombreCliente;
    private EditText txtObservacion;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHora;
    private int mMinutos;
    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    private PendingIntent pendingIntent;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_corte);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarCorteBD(view);
            }
        });

        mDateDisplay = (TextView)findViewById(R.id.tvFecha);
        mTimeDisplay = (TextView)findViewById(R.id.tvHora);
        tvNombreCliente = (TextView)findViewById(R.id.tvNombreCliente);
        txtObservacion = (EditText)findViewById(R.id.txtObservacion);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHora = c.get(Calendar.HOUR_OF_DAY);
        mMinutos = c.get(Calendar.MINUTE);
        cliente = null;
        updateDisplay();
    }

    public void seleccionarDia(View v) {
        showDialog(DATE_DIALOG_ID);
    }

    public void seleccionarHora(View v) {
        showDialog(TIME_DIALOG_ID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                cerrar_corte();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateDisplay(){
        Calendar c = Calendar.getInstance();
        c.set(mYear,mMonth,mDay,mHora,mMinutos,0);
        SimpleDateFormat format = new SimpleDateFormat("EEE dd,MMM yyyy");
        mDateDisplay.setText(format.format(c.getTime()));
        //new StringBuilder()
                //.append(mDay).append("-")
                //.append(mMonth).append("-")
                //.append(mYear).append(" "));

        format = new SimpleDateFormat("hh:mm");
        mTimeDisplay.setText(format.format(c.getTime()));
                /*new StringBuilder()
                .append(mHora).append(":")
                .append(mMinutos).append(" "));*/
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener(){
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth){
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener(){
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHora = hourOfDay;
                    mMinutos = minute;
                    updateDisplay();
                }
            };

    @Override
    protected Dialog onCreateDialog(int id){
        switch (id){
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear,mMonth,mDay);
            case TIME_DIALOG_ID:
                return new TimePickerDialog (this,
                        mTimeSetListener,
                        mHora,mMinutos,true);


        }
        return null;
    }

    private DBHelper getHelper() {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return mDBHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDBHelper != null) {
            OpenHelperManager.releaseHelper();
            mDBHelper = null;
        }
    }

    public void cerrar_corte() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void agregarCorteBD(View view){
        if (cliente != null) {
            Dao dao;
            try {
                dao = getHelper().getCorteDao();
                Corte corte = new Corte();
                corte.setCliente(cliente);
                Date dFecha = _armarFecha();
                corte.setHora(dFecha);
                corte.setObservacion(txtObservacion.getText().toString());
                dao.create(corte);

                Intent myIntentAfter = new Intent(this, AlarmInstante.class);
                myIntentAfter.putExtra(PARAMETRO_CLIENTE, cliente);
                myIntentAfter.putExtra(PARAMETRO_CORTE, corte);
                pendingIntent = PendingIntent.getBroadcast(this, 0, myIntentAfter, 0);
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC, dFecha.getTime(), pendingIntent);

                Intent myIntentNow = new Intent(this, AlarmInstante.class);
                myIntentNow.putExtra(PARAMETRO_CLIENTE, cliente);
                myIntentNow.putExtra(PARAMETRO_CORTE, corte);
                pendingIntent = PendingIntent.getBroadcast(this, 0, myIntentNow, 0);
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC, dFecha.getTime() - 900000, pendingIntent);

                setResult(RESULT_OK);
                finish();
            } catch (SQLException e) {
                Log.e(TAG, "Error creando usuario");
            }
        }else{
            Toast toast = Toast.makeText(this, "Falto elejir el cliente", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private Date _armarFecha(){
        Calendar c = Calendar.getInstance();
        c.set(mYear,mMonth,mDay,mHora,mMinutos,0);
        return c.getTime();
    }

    public void aniadirCliente(View v){
        Intent i = new Intent(this, ListaClientes.class );
        i.putExtra(ACCION, ACCION_ELEJIR_CLIENTE);
        i.putExtra(PARAMETRO_CLIENTE,cliente);
        startActivityForResult(i, CODIGO_ACTIVIDAD_AGREGAR_CLIENTE);
    }

    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case CODIGO_ACTIVIDAD_AGREGAR_CLIENTE:
                if (resultCode == Activity.RESULT_OK) {
                    cliente = (Cliente) data.getExtras().getSerializable(PARAMETRO_CLIENTE);
                    tvNombreCliente.setText(cliente.getNombre());
                }
                break;
        }
    }

}
