
package com.proyects.juan.mypeluqueria.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.proyects.juan.mypeluqueria.BaseDato.Cliente.Cliente;
import com.proyects.juan.mypeluqueria.BaseDato.DBHelper;
import com.proyects.juan.mypeluqueria.R;

import java.sql.SQLException;

public class AgregarCliente extends AppCompatActivity {

    private static final String TAG = "APP_PELU";
    private DBHelper mDBHelper;
    private EditText txtTelefono,txtNombre,txtDireccion;
    private RadioButton opHombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cliente);

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
                agregarCliente(view);
            }
        });

        txtNombre=(EditText)findViewById(R.id.txtNombre);
        txtTelefono=(EditText)findViewById(R.id.txtTelefono);
        txtDireccion =(EditText)findViewById(R.id.txtDireccion);
        opHombre = (RadioButton)findViewById(R.id.hombre);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                cerrar();
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

    public void cerrar(){
        setResult(RESULT_CANCELED);
        finish();
    }

    public void agregarCliente(View view){
        Dao dao;
        try {
            dao = getHelper().getClienteDao();
            Cliente cliente = new Cliente();
            cliente.setNombre(txtNombre.getText().toString());
            cliente.setTelefono(txtTelefono.getText().toString());
            cliente.setDireccion(txtDireccion.getText().toString());
            if (opHombre.isChecked())
                cliente.setSexo(0);
            else
                cliente.setSexo(1);

            dao.create(cliente);
            setResult(RESULT_OK);
            finish();
        } catch (SQLException e) {
            Log.e(TAG, "Error creando usuario");
        }
    }
}

