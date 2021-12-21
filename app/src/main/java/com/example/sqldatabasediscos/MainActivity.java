package com.example.sqldatabasediscos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText edArtista, edAlbum;
    ListView listaDiscos;
    SQLiteDatabase db;
    Button ok, delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    edArtista = findViewById(R.id.etGrupo);
    edAlbum = findViewById(R.id.etDisco);
    listaDiscos = findViewById(R.id.listViewDiscos);
    ok = findViewById(R.id.buttonOk);
    delete = findViewById(R.id.buttonDel);
    ok.setOnClickListener(this::añadir);
    delete.setOnClickListener(this::borrar);

    db = openOrCreateDatabase("MisDiscos", Context.MODE_PRIVATE, null);
    db.execSQL("CREATE TABLE IF NOT EXISTS misDiscos (" +
            "GRUPO VARCHAR, DISCO VARCHAR);");
    listar();
    }

    public void añadir (View v){
        db.execSQL("INSERT INTO MisDiscos VALUES ('" +
                edArtista.getText().toString() + "','" +
                edAlbum.getText().toString() + "')"
        );
        Toast.makeText(this, "Se añadió el disco " + edAlbum.getText().toString(), Toast.LENGTH_SHORT).show();
        listar();
    }

    public void borrar(View v){
        try {
            db.execSQL("DELETE FROM MisDiscos WHERE grupo = '" +
                    edArtista.getText().toString() +"' AND Disco ='" +
                    edAlbum.getText().toString() + "'"
            );
            Toast.makeText(this, "Se borró el disco " + edAlbum.getText().toString(), Toast.LENGTH_SHORT).show();
        } catch (SQLException e){
            Toast.makeText(this, "Error al borrar " + e.toString(), Toast.LENGTH_SHORT).show();
        }
        listar();
    }


    public void listar(){
        ArrayAdapter<String> adapter;
        List<String> lista = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT * FROM MisDiscos", null);

        if (c.getCount() == 0) {
            lista.add("No hay registros");
        } else {
            while (c.moveToNext()){
                lista.add(c.getString(0) + " - " + c.getString(1));
            }
            adapter = new ArrayAdapter<String>(
                    getApplicationContext(), android.R.layout.simple_list_item_1, lista);
            listaDiscos.setAdapter(adapter);
            c.close();
        }
    }


}