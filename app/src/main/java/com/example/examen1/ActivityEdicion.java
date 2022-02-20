package com.example.examen1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.examen1.configuraciones.SQLiteConexion;
import com.example.examen1.configuraciones.Transacciones;

import java.io.ByteArrayOutputStream;

public class ActivityEdicion extends AppCompatActivity {
    SQLiteConexion conexion;
    EditText nombre, telefono, nota;
    Button btn_atras, btn_compartir, btn_actualizar, btn_eliminar;
    String id_indice;
    ImageView ObjImagen;
    byte[] arregloFoto;
    Bitmap imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion);
        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        nombre = (EditText) findViewById(R.id.EdiTxtNombre);
        telefono = (EditText) findViewById(R.id.EdiTxtTelefono);
        nota = (EditText) findViewById(R.id.EdiTxtNota);
        ObjImagen = (ImageView) findViewById(R.id.EdiImgFoto);

        Bundle recibir_id = getIntent().getExtras();
        id_indice = recibir_id.getString("id_cont");
        ConsultaLlenarDatos();

        btn_atras = (Button) findViewById(R.id.EdiBtnAtras);
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Regresar();
            }
        });
        btn_compartir = (Button) findViewById(R.id.EdiBtnCompartir);
        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compartir();
            }
        });
        btn_actualizar = (Button) findViewById(R.id.EdiBtnActualizar);
        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActualizarContacto();
            }
        });
        btn_eliminar = (Button) findViewById(R.id.EdiBtnEliminar);
        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmarEliminar();
            }
        });
    }

    private void compartir()
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Contacto: "+nombre.getText().toString()
                +" Tel: "+telefono.getText().toString());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void ConfirmarEliminar()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Desea eliminar?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EliminarContacto();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    private void ConsultaLlenarDatos()
    {
        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] params = {id_indice};
        String[] fields = {Transacciones.nombre, Transacciones.telefono, Transacciones.nota, Transacciones.foto};
        String WhereCondition = Transacciones.id_cont + "=?";
        try {
            Cursor cdata = db.query(Transacciones.tablaContactos, fields, WhereCondition, params, null,null,null);
            cdata.moveToFirst();
            nombre.setText(cdata.getString(0));
            telefono.setText(cdata.getString(1));
            nota.setText(cdata.getString(2));
            arregloFoto = cdata.getBlob(3);
            Bitmap bmpNew = BitmapFactory.decodeByteArray(arregloFoto, 0, arregloFoto.length);
            ObjImagen.setImageBitmap(bmpNew);
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "Error, no se encontro el contacto.", Toast.LENGTH_SHORT).show();
        }
    }

    private void EliminarContacto()
    {
        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] params = {id_indice};

        db.delete(Transacciones.tablaContactos, Transacciones.id_cont+"=?", params);
        Toast.makeText(this, "Se elimino correctamente.", Toast.LENGTH_SHORT).show();
        db.close();

        Intent intent = new Intent(getApplicationContext(), ActivityContactos.class);
        startActivity(intent);
    }

    private void ActualizarContacto()
    {
        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] params = {id_indice};
        ContentValues valores = new ContentValues();
        valores.put(Transacciones.nombre, nombre.getText().toString());
        valores.put(Transacciones.telefono, telefono.getText().toString());
        valores.put(Transacciones.nota, nota.getText().toString());

        db.update(Transacciones.tablaContactos, valores, Transacciones.id_cont+"=?", params);
        Toast.makeText(this, "Se actualizo correctamente.", Toast.LENGTH_SHORT).show();
        db.close();

        Intent intent = new Intent(getApplicationContext(), ActivityContactos.class);
        startActivity(intent);
    }

    private void Regresar()
    {
        Intent intent = new Intent(getApplicationContext(), ActivityContactos.class);
        startActivity(intent);
    }
}