package com.proyects.juan.mypeluqueria.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

import static com.proyects.juan.mypeluqueria.Util.SIConstantes.ACCION;
import static com.proyects.juan.mypeluqueria.Util.SIConstantes.ACCION_ELEJIR_CLIENTE;
import static com.proyects.juan.mypeluqueria.Util.SIConstantes.ACCION_VER_CLIENTES;

public class ListaClientes extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String PARAMETRO_CLIENTE = "CLIENTE";
    private static final int CODIGO_ACTIVIDAD_CLIENTE = 1;
    private static final String TAG = "APP_PELU";
    private DBHelper mDBHelper;
    ListView listaClientes;
    ArrayAdapter adaptador;
    private String accion;
    final Context context = this;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        accion = (String) getIntent().getExtras().getString(ACCION);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarClienteShow();
            }
        });

        //Instancia del ListView
        listaClientes = (ListView)findViewById(R.id.listaClientes);

        //Inicializar el adaptador con la fuente de datos
        adaptador = new ListaArrayAdapter<Corte>(
                this,
                getClientesActuales());

        //Relacionando la lista con el adaptador
        listaClientes.setAdapter(adaptador);

        listaClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                Cliente cliente = (Cliente) listaClientes.getItemAtPosition(posicion);
                Intent intent;
                if (accion.equals(ACCION_ELEJIR_CLIENTE)) {
                    intent = new Intent();
                    intent.putExtra(PARAMETRO_CLIENTE, cliente);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else if (accion.equals(ACCION_VER_CLIENTES)) {
                    intent = new Intent(context, VerCliente.class);
                    intent.putExtra(PARAMETRO_CLIENTE, cliente);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista_clientes, menu);
        searchView = (SearchView) menu.findItem(R.id.action_buscarCliente).getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adaptador = new ListaArrayAdapter<Corte>(
                        context,
                        getClientesActuales());

                //Relacionando la lista con el adaptador
                listaClientes.setAdapter(adaptador);
                return false;
            }
        });
        return true;
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

    private void agregarClienteShow(){
        Intent i = new Intent(this, AgregarCliente.class );
        startActivityForResult(i, CODIGO_ACTIVIDAD_CLIENTE);
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
    private List getClientesActuales(){
        List ClientesActuales = new ArrayList<Cliente>();
        Dao dao;
        try {

            dao = getHelper().getClienteDao();

            List clientes = dao.queryBuilder().orderBy(Cliente.NOMBRE,true).query();

            ClientesActuales.addAll(clientes);

        }catch (SQLException e) {
            Log.e(TAG, "Error obteniendo cortes " + e.getErrorCode());
            Log.e(TAG, "Error obteniendo cortes " + e.getMessage());
        }

        return ClientesActuales;
    }

    private List getClientesFiltrados(String sText){
        List ClientesActuales = new ArrayList<Cliente>();
        Dao dao;
        try {

            dao = getHelper().getClienteDao();

            List clientes = dao.queryBuilder().orderBy(Cliente.NOMBRE, true).where().like(Cliente.NOMBRE, "%" + sText + "%").query();

            Log.e(TAG, "Texto.. => " + sText);
            Log.e(TAG, "Clientes.. => " + clientes.size());
            ClientesActuales.addAll(clientes);

        }catch (SQLException e) {
            Log.e(TAG, "Error obteniendo cortes " + e.getErrorCode());
            Log.e(TAG, "Error obteniendo cortes " + e.getMessage());
        }

        return ClientesActuales;
    }

    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case CODIGO_ACTIVIDAD_CLIENTE:
                //if (resultCode == Activity.RESULT_OK) {
                    adaptador = new ListaArrayAdapter<Corte>(
                            this,
                            getClientesActuales());

                    //Relacionando la lista con el adaptador
                    listaClientes.setAdapter(adaptador);
                    searchView.clearFocus();
                //}
                break;
        }
    }

    // The following callbacks are called for the SearchView.OnQueryChangeListener
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    public boolean      onQueryTextSubmit      (String query) {

        adaptador = new ListaArrayAdapter<Corte>(
                this,
                getClientesFiltrados(query));

        //Relacionando la lista con el adaptador
        listaClientes.setAdapter(adaptador);
        return true;
    }
}
