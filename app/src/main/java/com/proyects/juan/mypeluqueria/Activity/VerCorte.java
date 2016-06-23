package com.proyects.juan.mypeluqueria.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.proyects.juan.mypeluqueria.BaseDato.Cliente.Cliente;
import com.proyects.juan.mypeluqueria.BaseDato.Corte.Corte;
import com.proyects.juan.mypeluqueria.BaseDato.DBHelper;
import com.proyects.juan.mypeluqueria.R;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import static com.proyects.juan.mypeluqueria.Util.SIConstantes.PARAMETRO_CLIENTE;
import static com.proyects.juan.mypeluqueria.Util.SIConstantes.PARAMETRO_CORTE;

public class VerCorte extends AppCompatActivity {

    private TextView tvDireccion,tvTelefono,tvFecha,tvObservacion;
    private DBHelper mDBHelper;
    private Corte corte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_corte);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Cliente cliente = (Cliente) getIntent().getExtras().getSerializable(PARAMETRO_CLIENTE);
        corte = (Corte) getIntent().getExtras().getSerializable(PARAMETRO_CORTE);

        tvDireccion = (TextView)findViewById(R.id.tvDireccion);
        tvTelefono = (TextView)findViewById(R.id.tvTelefono);
        tvFecha = (TextView)findViewById(R.id.tvFecha);
        tvObservacion = (EditText)findViewById(R.id.tvObservacion);
        tvDireccion.setText(cliente.getDireccion());
        tvTelefono.setText(cliente.getTelefono());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm", Locale.getDefault());
        String sHora = sdf.format(corte.getHora());
        tvFecha.setText(sHora);
        tvObservacion.setText(corte.getObservacion());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                actualizarObservacion(corte,tvObservacion.getText().toString());
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void actualizarObservacion(Corte corte,String sObs){
        Dao dao;
        try {
            dao = getHelper().getCorteDao();
            corte.setObservacion(sObs);
            dao.update(corte);
        } catch (SQLException e) {
            Log.e("TAG", "Error modificando corte");
        }
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

}
