package com.proyects.juan.mypeluqueria.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.proyects.juan.mypeluqueria.BaseDato.Cliente.Cliente;
import com.proyects.juan.mypeluqueria.BaseDato.Corte.Corte;
import com.proyects.juan.mypeluqueria.BaseDato.DBHelper;
import com.proyects.juan.mypeluqueria.Util.ListaArrayAdapter;
import com.proyects.juan.mypeluqueria.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.proyects.juan.mypeluqueria.Util.SIConstantes.ACCION;
import static com.proyects.juan.mypeluqueria.Util.SIConstantes.ACCION_VER_CLIENTES;
import static com.proyects.juan.mypeluqueria.Util.SIConstantes.PARAMETRO_CLIENTE;
import static com.proyects.juan.mypeluqueria.Util.SIConstantes.PARAMETRO_CORTE;

public class Inicio extends AppCompatActivity {

    final Context context = this;
    private static final String TAG = "APP_PELU";
    private static final int CODIGO_ACTIVIDAD_CORTE = 1;
    private DBHelper mDBHelper;
    ListView listaCortes;
    ArrayAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarCorteShow();
            }
        });

        //Instancia del ListView
        listaCortes = (ListView)findViewById(R.id.listaCortes);

        //Inicializar el adaptador con la fuente de datos
        adaptador = new ListaArrayAdapter<Corte>(this,getCortesActuales());
        //Relacionando la lista con el adaptador
        listaCortes.setAdapter(adaptador);
        listaCortes.setOnItemClickListener(new onClickListaCortes());
        registerForContextMenu(listaCortes);
    }

    private void actualizarObservacion(Corte corte,String sObs){
        Dao dao;
        try {
            dao = getHelper().getCorteDao();
            corte.setObservacion(sObs);
            dao.update(corte);
        } catch (SQLException e) {
            Log.e(TAG, "Error modificando corte");
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_corte, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Toast toast;
        Intent i;
        int position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
        Corte corte = (Corte)listaCortes.getItemAtPosition(position);
        Cliente cliente = corte.getCliente();
        switch (item.getItemId()) {
            case R.id.action_agregarObservacion:
                agregarObservacion(position);
                return true;
            case R.id.action_verCliente:
                i = new Intent(this, VerCliente.class);
                i.putExtra(PARAMETRO_CLIENTE, cliente);
                startActivity(i);
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_listaCliente) {
            ListaClientesShow();
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

    private void ListaClientesShow(){
        Intent i = new Intent(this, ListaClientes.class );
        i.putExtra(ACCION, ACCION_VER_CLIENTES);
        startActivity(i);
    }

    private void agregarCorteShow(){
        Intent i = new Intent(this, AgregarCorte.class );
        startActivityForResult(i, CODIGO_ACTIVIDAD_CORTE);
    }

    private List getCortesActuales(){
        List CortesActuales = new ArrayList<Corte>();
        Dao dao;
        try {

            dao = getHelper().getCorteDao();
            Calendar c = Calendar.getInstance();
            List cortes = dao.queryBuilder().orderBy(Corte.HORA,true).where().ge(Corte.HORA,c.getTime()).or().isNull(Corte.OBSERVACION).query();

            CortesActuales.addAll(cortes);

        }catch (SQLException e) {
            Log.e(TAG, "Error obteniendo cortes " + e.getErrorCode());
            Log.e(TAG, "Error obteniendo cortes " + e.getMessage());
        }

        return CortesActuales;
    }

    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case CODIGO_ACTIVIDAD_CORTE:
                if (resultCode == Activity.RESULT_OK) {
                    actualizarCortes();
                }
                break;
        }
    }

    public void actualizarCortes(){
        adaptador = new ListaArrayAdapter<Corte>(
                this,
                getCortesActuales());

        //Relacionando la lista con el adaptador
        listaCortes.setAdapter(adaptador);
    }


    private void agregarObservacion(int posicion){
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        final Corte corte = (Corte)listaCortes.getItemAtPosition(posicion);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                actualizarObservacion(corte,userInput.getText().toString());
                                actualizarCortes();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }



    private class onClickListaCortes implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
            Corte corte = (Corte)listaCortes.getItemAtPosition(posicion);
            Cliente cliente = corte.getCliente();
            Intent i = new Intent(context, VerCorte.class);
            i.putExtra(PARAMETRO_CLIENTE, cliente);
            i.putExtra(PARAMETRO_CORTE, corte);
            startActivity(i);

        }
    }
}

