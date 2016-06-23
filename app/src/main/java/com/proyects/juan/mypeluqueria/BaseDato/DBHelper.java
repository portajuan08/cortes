package com.proyects.juan.mypeluqueria.BaseDato;

/**
 * Created by juan on 22/12/15.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.proyects.juan.mypeluqueria.BaseDato.Cliente.Cliente;
import com.proyects.juan.mypeluqueria.BaseDato.Corte.Corte;

import java.sql.SQLException;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "peluqueria.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Corte, Integer> corteDao;
    private Dao<Cliente, Integer> clienteDao;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Cliente.class);
            TableUtils.createTable(connectionSource, Corte.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        onCreate(db, connectionSource);
    }

    public Dao<Corte, Integer> getCorteDao() throws SQLException {
        if (corteDao == null) {
            corteDao = getDao(Corte.class);
        }
        return corteDao;
    }

    public Dao<Cliente, Integer> getClienteDao() throws SQLException {
        if (clienteDao == null) {
            clienteDao = getDao(Cliente.class);
        }
        return clienteDao;
    }

    @Override
    public void close() {
        super.close();
        corteDao = null;
        clienteDao = null;
    }

}
