package com.example.fhjua.crudusuarios;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fhjua.crudusuarios.BD.OpenHelper;
import com.example.fhjua.crudusuarios.BD.Utilidades;

public class LoginUsuarios extends AppCompatActivity {

    private EditText campo1, campo2;
    private Button btnLogin, btnRegistrarUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuarios);

        campo1 = (EditText) findViewById(R.id.camp_usu_login);
        campo2 = (EditText) findViewById(R.id.camp_pass_login);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegistrarUsu = (Button) findViewById(R.id.btn_registrar_usu);

        //Conecto con la base de datos
        OpenHelper objConecta = new OpenHelper(this, "DB1", null, 1);
        SQLiteDatabase objBaseDatos = objConecta.getWritableDatabase();

        campo1.setText("");
        campo2.setText("");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //rescato los datos de los campos
                    String usuarioLogin = campo1.getText().toString();
                    String passLogin = campo2.getText().toString();
                    //Conecto con la base de datos
                    OpenHelper objConecta = new OpenHelper(getApplicationContext(), "DB1", null, 1);
                    SQLiteDatabase objBaseDatos = objConecta.getWritableDatabase();
                    //Si los campos no estan vacios
                    if(!usuarioLogin.isEmpty() && !passLogin.isEmpty()){
                        //Hago una consulta a la base de datos
                        String[] campos = new String[]{Utilidades.CAMP_USUARIO_TBL_USUARIO, Utilidades.CAMP_PASSWORD_TBL_USUARIO};
                        String[] args = new String[]{usuarioLogin, passLogin};
                        Cursor objCursor = objBaseDatos.query(Utilidades.NOM_TBL_USUARIO, campos, "" + Utilidades.CAMP_USUARIO_TBL_USUARIO +
                                " = ? AND " + Utilidades.CAMP_PASSWORD_TBL_USUARIO + " = ?", args, null, null, null);
                        //Si la consulta arroja resultados
                        if(objCursor.moveToFirst()){
                            do{
                                //almaceno los valores devueltos
                                String usuarioResult = objCursor.getString(0);
                                String passResult = objCursor.getString(1);
                                //comparo con los valores devueltos
                                if(usuarioLogin.equals(usuarioResult) && passLogin.equals(passResult)){
                                    cambiaActivity();
                                }
                            }while(objCursor.moveToNext());
                        }else{
                            Toast.makeText(getApplicationContext(), "Usuario y/o Password incorrectos.", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Los campos son obligatorios.", Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRegistrarUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(getApplicationContext(), RegistraUsuarios.class);
                startActivity(objIntent);
            }
        });
    }

    //Metodo para cambiar a ListaUsuarios
    public void cambiaActivity(){
        Intent objIntent = new Intent(this, ListaUsuarios.class);
        startActivity(objIntent);
    }
}
