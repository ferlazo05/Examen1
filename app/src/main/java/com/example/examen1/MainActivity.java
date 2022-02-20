package com.example.examen1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.examen1.configuraciones.SQLiteConexion;
import com.example.examen1.configuraciones.Transacciones;
import com.example.examen1.tablas.Paises;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText nombre, telefono, nota;
    Button btn_salvar, btn_contactos, btn_paises, btn_foto;
    ImageView ObjImagen;
    byte[] arregloFoto;
    Bitmap imagen;
    String CurrentPhotoPath;
    Spinner sp_contactos;
    ArrayList<String> lista_paises;
    ArrayList<Paises> lista;
    static final int PETICION_ACCESO_CAM = 100;
    static final int TAKE_PIC_REQUEST = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp_contactos = (Spinner) findViewById(R.id.MainSpPais);
        ObtenerListaPaises();
        ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_paises);
        sp_contactos.setAdapter(adp);

        nombre = (EditText) findViewById(R.id.MainTxtNombre);
        telefono = (EditText) findViewById(R.id.MainTxtTelefono);
        nota = (EditText) findViewById(R.id.MainTxtNota);

        btn_salvar = (Button) findViewById(R.id.MainBtnSalvar);
        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarContacto();
            }
        });
        btn_contactos = (Button) findViewById(R.id.MainBtnContactos);
        btn_contactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaContactos();
            }
        });
        btn_paises = (Button) findViewById(R.id.MainBtnPaises);
        btn_paises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaPaises();
            }
        });
        ObjImagen = (ImageView) findViewById(R.id.MainImgFoto);
        btn_foto = (Button) findViewById(R.id.MainBtnFoto);
        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });
    }

    private void LimpiarPantalla()
    {
        nombre.setText("");
        telefono.setText("");
        nota.setText("");
        Bitmap bitmap = null;
        ObjImagen.setImageBitmap(bitmap);
    }

    private void ListaPaises()
    {
        Intent intent = new Intent(getApplicationContext(), ActivityPaises.class);
        startActivity(intent);
    }

    private void ListaContactos()
    {
        Intent intent = new Intent(getApplicationContext(), ActivityContactos.class);
        startActivity(intent);
    }

    private void RegistrarContacto()
    {
        String nom = String.valueOf(nombre.getText());
        String tel = String.valueOf(telefono.getText());
        String not = String.valueOf(nota.getText());
        if(nom.equals(""))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Por favor ingrese un nombre.")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            builder.show();
        }
        else if(tel.equals(""))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Por favor ingrese un telefono.")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            builder.show();
        }
        else if(not.equals(""))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Por favor ingrese una nota.")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            builder.show();
        }
        else
        {
            //Conexion a base de datos
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();
            arreglarFoto();
            ContentValues valores = new ContentValues();
            valores.put(Transacciones.nombre, nombre.getText().toString());
            valores.put(Transacciones.telefono, telefono.getText().toString());
            valores.put(Transacciones.nota, nota.getText().toString());
            valores.put(Transacciones.foto, arregloFoto);

            Long resultado = db.insert(Transacciones.tablaContactos, Transacciones.id_cont, valores);
            if (resultado >= 1) {
                Toast.makeText(getApplicationContext(), "Se registró con éxito! Codigo=" + resultado.toString()
                        , Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "No se registró", Toast.LENGTH_SHORT).show();
            }
            db.close();
            LimpiarPantalla();
        }
    }

    private void ObtenerListaPaises()
    {
        Paises paises = null;
        lista = new ArrayList<Paises>();
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        SQLiteDatabase db = conexion.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+Transacciones.tablaPaises, null);
        while(cursor.moveToNext())
        {
            paises = new Paises();
            paises.setId_pais(cursor.getInt(0));
            paises.setPais(cursor.getString(1));
            paises.setPrefijo(cursor.getString(2));
            lista.add(paises);
        }
        cursor.close();
        llenarSp();
    }

    private void llenarSp()
    {
        lista_paises = new ArrayList<String>();
        for(int i=0; i<lista.size(); i++)
        {
            lista_paises.add(lista.get(i).getPais()+" | "+
                    lista.get(i).getPrefijo());
        }
    }


    private void permisos()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PETICION_ACCESO_CAM);
        }
        else
        {
            tomarfoto();
        }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PETICION_ACCESO_CAM)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                tomarfoto();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Se necesitan permisos de accesso a la camara", Toast.LENGTH_SHORT).show();
        }
    }

    private void tomarfoto()
    {
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takepic.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takepic, TAKE_PIC_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_PIC_REQUEST && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imagen = (Bitmap) extras.get("data");
            ObjImagen.setImageBitmap(imagen);
        }
    }

    private void arreglarFoto()
    {
        Bitmap bmp = imagen;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,100,stream);
        arregloFoto = stream.toByteArray();
        bmp.recycle();
    }
}