package com.example.examen1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.examen1.configuraciones.SQLiteConexion;
import com.example.examen1.configuraciones.Transacciones;

public class ActivityPaises extends AppCompatActivity {
    EditText pais, prefijo;
    Button btn_atras, btn_agregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paises);

        pais = (EditText) findViewById(R.id.PaiTxtPais);
        prefijo = (EditText) findViewById(R.id.PaiTxtPrefijo);

        btn_atras = (Button) findViewById(R.id.PaiBtnAtras);
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Regresar();
            }
        });
        btn_agregar = (Button) findViewById(R.id.PaiBtnAgregar);
        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgregarPais();
            }
        });
    }

    private void LimpiarPantalla()
    {
        pais.setText("");
        prefijo.setText("");
    }

    private void AgregarPais()
    {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.pais, pais.getText().toString());
        valores.put(Transacciones.prefijo, prefijo.getText().toString());

        Long resultado = db.insert(Transacciones.tablaPaises, Transacciones.id_pais, valores);
        if (resultado>=1)
        {
            Toast.makeText(getApplicationContext(), "Se registró con éxito! Codigo="+resultado.toString()
                    , Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No se registró", Toast.LENGTH_SHORT).show();
        }
        db.close();
        LimpiarPantalla();
    }

    private void Regresar()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}