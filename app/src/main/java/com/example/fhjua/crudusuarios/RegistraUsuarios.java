package com.example.fhjua.crudusuarios;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fhjua.crudusuarios.BD.OpenHelper;
import com.example.fhjua.crudusuarios.BD.Utilidades;

import java.util.ArrayList;

public class RegistraUsuarios extends AppCompatActivity {

    private EditText campo1, campo2, campo3, campo4, campo5, campo6;
    private Spinner spinner;
    private Button btnGuardaUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registra_usuarios);

        campo1 = (EditText) findViewById(R.id.camp_correo);
        campo2 = (EditText) findViewById(R.id.camp_nombre);
        campo3 = (EditText) findViewById(R.id.camp_apellido);
        campo4 = (EditText) findViewById(R.id.camp_usuario);
        campo5 = (EditText) findViewById(R.id.camp_pass);
        campo6 = (EditText) findViewById(R.id.camp_con_pass);
        spinner = (Spinner) findViewById(R.id.spinner);
        btnGuardaUsu = (Button) findViewById(R.id.btn_registra_usu);

        try{
            //Conecto con la base de datos
            OpenHelper objConecta = new OpenHelper(this, "DB1", null, 1);
            SQLiteDatabase objBaseDatos = objConecta.getWritableDatabase();
            //Creo un ArrayList
            ArrayList<String> rol;
            //Hago una consulta a la base de datos
            String[] campos = new String[]{Utilidades.CAMP_NOMBRE_TBL_ROL};
            Cursor objCursor = objBaseDatos.query(Utilidades.NOM_TBL_ROL, campos, null, null, null, null, null);
            //si devuelve registros
            if(objCursor.moveToFirst()){
                rol = new ArrayList<>();
                rol.add("Seleccionar");
                do{
                    rol.add(objCursor.getString(0));
                }while(objCursor.moveToNext());
                //adaptamos el ArrayList
                ArrayAdapter<String> objAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rol);
                spinner.setAdapter(objAdapter);
            }
        }catch(Exception e){
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        btnGuardaUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //rescato los datos
                    String usuCorreo = campo1.getText().toString();
                    String usuNombre = campo2.getText().toString();
                    String usuApellido = campo3.getText().toString();
                    String usuUsuario = campo4.getText().toString();
                    String usuPass = campo5.getText().toString();
                    String usuConPass = campo6.getText().toString();
                    String usuRol = spinner.getSelectedItem().toString();
                    //si los campos no estan vacios
                    if(!usuCorreo.isEmpty() && !usuNombre.isEmpty() && !usuApellido.isEmpty() && !usuUsuario.isEmpty() && !usuPass.isEmpty() && !usuConPass.isEmpty()){
                        //si la opcion del spinner es seleccionar
                        if(!usuRol.equals("Seleccionar")){
                            //si las contraseñas coinciden
                            if (usuConPass.equals(usuPass)) {
                                //Conecto con la base de datos
                                OpenHelper objConecta = new OpenHelper(getApplicationContext(), "DB1", null, 1);
                                SQLiteDatabase db = objConecta.getWritableDatabase();
                                //inserto los valores
                                String[] valores = new String[]{usuCorreo, usuNombre, usuApellido, usuRol, usuUsuario, usuPass};
                                String inserta = objConecta.insertaValoresUsuario(valores, db);
                                Toast.makeText(getApplicationContext(), "" + inserta, Toast.LENGTH_LONG).show();
                                cambiaLoginUsuarios();
                            } else {
                                Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden.", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Seleccione un Rol.", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios.", Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Metodo para cambiar a LoginUsuarios
    public void cambiaLoginUsuarios(){
        Intent objIntent = new Intent(this, ListaUsuarios.class);
        startActivity(objIntent);
    }
}
