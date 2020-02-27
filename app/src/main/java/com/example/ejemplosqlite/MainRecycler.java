package com.example.ejemplosqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainRecycler extends AppCompatActivity {

    ArrayList<Contactos> listaContactos;
    RecyclerView recycler;
    ConexionSQL conex;
    EditText id, nom, tel;
    public static String TABLA ="usuario";
    public static String ID ="id";
    public static String NOMBRE ="nombre";
    public static String TELEFONO ="telefono";
    public static final String CREAR_TABLA = "CREATE TABLE "+ TABLA +" ("+ ID +" INTEGER, "+ NOMBRE +" TEXT, "+ TELEFONO +" TEXT)";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_recycler);
        id= (EditText) findViewById(R.id.input_id);
        nom= (EditText) findViewById(R.id.input_nom);
        tel= (EditText) findViewById(R.id.input_tel);
        conex =new ConexionSQL(getApplicationContext(),"bd_usuarios",null,1);
        listaContactos =new ArrayList<>();
        recycler =(RecyclerView)findViewById(R.id.recycle);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        listener_recicler();
        boton_fab();
        consultar();
        Adapter adapter=new Adapter(listaContactos);
        recycler.setAdapter(adapter);
    }

    public void listener_recicler(){
        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recycler,new RecyclerItemClickListener.OnItemClickListener() {
                    String nomb="";
                    String nume="";
                    @Override public void onItemClick(View view, int position) {
                        try{
                        SQLiteDatabase db= conex.getReadableDatabase();
                        Contactos contactos =null;
                        Cursor cursor =db.rawQuery("SELECT * FROM "+ TABLA,null);
                        int index=0;
                        while (cursor.moveToNext()){
                            contactos =new Contactos();
                            contactos.setId(cursor.getInt(0));
                            contactos.setNombre(cursor.getString(1));
                            contactos.setTelefono(cursor.getString(2));
                            listaContactos.add(contactos);
                            nomb= listaContactos.get(position).getId()+" - "+listaContactos.get(position).getNombre();
                            nume= listaContactos.get(position).getTelefono();
                            index++;
                        }
                        }catch(Exception e){
                            toastea(("Error al cargar"));
                        }
                        cargar_ventana_detalle(nomb, nume );
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        //eliminar(String.valueOf(position));
                    }
                })
        );
    }

    public void eliminar(View view) {
        SQLiteDatabase db= conex.getWritableDatabase();
        String[] parametros = {id.getText().toString()};
        db.delete(TABLA,ID+"=?",parametros);
        toastea(("Contacto eliminado!"));
        db.close();
        consultar();
        Adapter adapter=new Adapter(listaContactos);
        recycler.setAdapter(adapter);
        id.setText("");
    }

    public void actualizar(View view) {

        SQLiteDatabase db= conex.getWritableDatabase();
        String[] parametros = {id.getText().toString()};
        ContentValues values = new ContentValues();
        values.put(NOMBRE,nom.getText().toString());
        values.put(TELEFONO,tel.getText().toString());
        db.update(TABLA,values,ID+"=?",parametros);
        toastea(("Contacto "+nom.getText().toString()+" actualizado!"));
        db.close();
        consultar();
        Adapter adapter=new Adapter(listaContactos);
        recycler.setAdapter(adapter);
        id.setText("");
        nom.setText("");
        tel.setText("");
    }

    public void boton_fab(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventana_emergente();
            }
        });
    }

    public void ventana_emergente(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añadir contacto:");
        //final EditText input_id = new EditText(this);
        final EditText input_nombre = new EditText(this);
        final EditText input_numero = new EditText(this);
        //input_id.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_PHONE);
        input_nombre.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        input_numero.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_PHONE);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
       // input_id.setHint("Id");
       // layout.addView(input_id);
        input_nombre.setHint("Nombre");
        layout.addView(input_nombre);
        input_numero.setHint("Telefono");
        layout.addView(input_numero);
        builder.setView(layout);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String  m_id = String.valueOf(listaContactos.size()+1);
                String  m_nombre = input_nombre.getText().toString();
                String  m_telefono= input_numero.getText().toString();
                Insertar(m_id, m_nombre,m_telefono);
                toastea(("Contacto "+m_id+" - "+m_nombre+" - "+m_telefono+" añadido!"));
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                toastea(("Contacto no añadido!"));
            }
        });
        builder.show();
    }

    private void Insertar(String campoId, String campoNombre, String campoTelefono) {
        ConexionSQL conn = new ConexionSQL(this, "bd_usuarios", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, campoId);
        values.put(NOMBRE, campoNombre);
        values.put(TELEFONO, campoTelefono);
        Long idResultante = db.insert(TABLA, ID, values);
        toastea(("contacto añadido!"));
        db.close();
        consultar();
        Adapter adapter=new Adapter(listaContactos);
        recycler.setAdapter(adapter);
    }

    public void toastea(String tosta){
        String tostada= tosta;
        Toast toast1 =
                Toast.makeText(this, tostada, Toast.LENGTH_SHORT);
        toast1.show();
    }

    private void consultar() {
        listaContactos =new ArrayList<>();
        SQLiteDatabase db= conex.getReadableDatabase();
        Contactos contactos =null;
        Cursor cursor =db.rawQuery("SELECT * FROM "+ TABLA,null);
        while (cursor.moveToNext()){
            contactos =new Contactos();
            contactos.setId(cursor.getInt(0));
            contactos.setNombre(cursor.getString(1));
            contactos.setTelefono(cursor.getString(2));
            listaContactos.add(contactos);
        }
    }

    public void cargar_ventana_detalle(String nomb, String nume){
        Intent intent = new Intent(this, pantallaDetalles.class);
        String dato=null;
        intent.putExtra("nombre", nomb);
        intent.putExtra("numero", nume);
        this.startActivity(intent);
    }


}
