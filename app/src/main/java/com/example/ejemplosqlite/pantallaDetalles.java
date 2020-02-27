package com.example.ejemplosqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class pantallaDetalles  extends AppCompatActivity {

    private static final String TAG_ACTIVITY = pantallaDetalles.class.getSimpleName();
    private TextView texto1;
    private TextView texto2;
    private String info_nom;
    private String info_num;
    EditText  nom, tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_detalle);
        texto1 = (TextView)findViewById(R.id.nom);
        texto2 = (TextView)findViewById(R.id.num);
        nom= (EditText) findViewById(R.id.input_nom);
        tel= (EditText) findViewById(R.id.input_tel);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null) {
            info_nom =(String) b.get("nombre");
            texto1.setText(info_nom);
            info_num =(String) b.get("numero");
            texto2.setText(info_num);
        }
    }


}

