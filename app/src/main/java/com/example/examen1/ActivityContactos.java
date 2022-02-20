package com.example.examen1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.examen1.configuraciones.SQLiteConexion;
import com.example.examen1.configuraciones.Transacciones;
import com.example.examen1.tablas.Contactos;

import java.util.ArrayList;
import java.util.List;

public class ActivityContactos extends AppCompatActivity {
    SQLiteConexion conexion;
    Button btn_atras;
    ListView lista;
    ArrayList<Contactos> listaContactos;
    ArrayList<String> ArregloContactos;
    String indice_id, numeroTelefonico;
    int contador = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        btn_atras = (Button) findViewById(R.id.ConBtnAtras);
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Regresar();
            }
        });
        ObtenerListaPersonas();
        lista = (ListView) findViewById(R.id.ConListContactos);
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ArregloContactos);
        lista.setAdapter(adp);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                contador++;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(contador == 1)
                        {
                            indice_id = String.valueOf(i+1);
                            enviarDato();
                        }
                        else if(contador == 2)
                        {
                            indice_id = String.valueOf(i+1);
                            ConsultarNumeroTelefonico();
                        } contador = 0;
                    }
                }, 500);
            }
        });
    }

    private void ConsultarNumeroTelefonico()
    {
        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] params = {indice_id};
        String[] fields = {Transacciones.telefono};
        String WhereCondition = Transacciones.id_cont + "=?";
        try
        {
            Cursor cdata = db.query(Transacciones.tablaContactos, fields, WhereCondition, params, null,null,null);
            cdata.moveToFirst();
            numeroTelefonico = cdata.getString(0);
            llamar();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "No se encontro el numero de telefono", Toast.LENGTH_SHORT).show();
        }
    }

    private void llamar()
    { //ACTION CALL TIRABA ERROR ING. POR ESO PUSE DIAL
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+numeroTelefonico));
        Toast.makeText(this, "Llegue", Toast.LENGTH_SHORT).show();
        startActivity(callIntent);
    }

    private void enviarDato()
    {
        Bundle enviar_id = new Bundle();
        enviar_id.putString("id_cont", indice_id);
        Intent intent = new Intent(getApplicationContext(), ActivityEdicion.class);
        intent.putExtras(enviar_id);
        startActivity(intent);
    }

    private void Regresar()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void ObtenerListaPersonas()
    {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos list_cont = null;
        listaContactos = new ArrayList<Contactos>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+Transacciones.tablaContactos, null);
        while(cursor.moveToNext())
        {
            list_cont = new Contactos();
            list_cont.setId_cont(cursor.getInt(0));
            list_cont.setNombre(cursor.getString(1));
            list_cont.setTelefono(cursor.getString(2));
            list_cont.setNota(cursor.getString(3));
            list_cont.setFoto(cursor.getBlob(4));
            listaContactos.add(list_cont);
        }
        cursor.close();
        llenarlista();
    }

    private void llenarlista()
    {
        ArregloContactos = new ArrayList<String>();
        for(int i=0; i<listaContactos.size(); i++)
        {
            ArregloContactos.add(listaContactos.get(i).getNombre()+" | "+
                    listaContactos.get(i).getTelefono());
        }
    }

}