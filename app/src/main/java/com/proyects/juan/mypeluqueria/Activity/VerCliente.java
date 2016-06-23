package com.proyects.juan.mypeluqueria.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.proyects.juan.mypeluqueria.BaseDato.Cliente.Cliente;
import com.proyects.juan.mypeluqueria.BaseDato.Corte.Corte;
import com.proyects.juan.mypeluqueria.BaseDato.DBHelper;
import com.proyects.juan.mypeluqueria.R;
import com.proyects.juan.mypeluqueria.Util.ListaArrayAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.proyects.juan.mypeluqueria.Util.SIConstantes.PARAMETRO_CLIENTE;
import static com.proyects.juan.mypeluqueria.Util.SIConstantes.PARAMETRO_CORTE;

public class VerCliente extends AppCompatActivity {

    final Context context = this;
    private TextView tvDireccion,tvTelefono;
    private DBHelper mDBHelper;
    ListView listaCortes;
    ArrayAdapter adaptador;
    Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cliente = (Cliente) getIntent().getExtras().getSerializable(PARAMETRO_CLIENTE);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(cliente.getNombre());
        }
        tvDireccion = (TextView)findViewById(R.id.tvDireccion);
        tvTelefono = (TextView)findViewById(R.id.tvTelefono);
        tvDireccion.setText(cliente.getDireccion());
        tvTelefono.setText(cliente.getTelefono());
        //Instancia del ListView
        listaCortes = (ListView)findViewById(R.id.listaCortes);

        //Inicializar el adaptador con la fuente de datos
        adaptador = new ListaArrayAdapter<Corte>(
                this,
                getCortesCliente());

        //Relacionando la lista con el adaptador
        listaCortes.setAdapter(adaptador);

        listaCortes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                Corte corte = (Corte) listaCortes.getItemAtPosition(posicion);
                Intent i = new Intent(context,VerCorte.class);
                i.putExtra(PARAMETRO_CLIENTE, cliente);
                i.putExtra(PARAMETRO_CORTE, corte);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private List<Corte> getCortesCliente() {
        List CortesCliente = new ArrayList<Corte>();
        Dao dao;
        try {

            dao = getHelper().getCorteDao();

            List cortes = dao.queryBuilder().orderBy(Corte.HORA,true).where().eq(Corte.CLIENTE,cliente.getId()).query();
            CortesCliente.addAll(cortes);

        }catch (SQLException e) {
            Log.e("PELU", "Error obteniendo cortes " + e.getErrorCode());
            Log.e("PELU", "Error obteniendo cortes " + e.getMessage());
        }

        return CortesCliente;
    }
}
