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

public class EditaUsuarios extends AppCompatActivity {

    private EditText campo1, campo2, campo3, campo4, campo5, campo6;
    private Spinner spinner;
    private Button btnEliminaUsu, btnActualizaUsu;
    private ArrayList<String> rol;
    private SQLiteDatabase objBaseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edita_usuarios);

        campo1 = (EditText) findViewById(R.id.camp_correo_edita);
        campo2 = (EditText) findViewById(R.id.camp_nombre_edita);
        campo3 = (EditText) findViewById(R.id.camp_apellido_edita);
        campo4 = (EditText) findViewById(R.id.camp_usuario_edita);
        campo5 = (EditText) findViewById(R.id.camp_pass_edita);
        campo6 = (EditText) findViewById(R.id.camp_con_pass_edita);
        spinner = (Spinner) findViewById(R.id.spinner_edita);
        btnEliminaUsu = (Button) findViewById(R.id.btn_elimina_usu);
        btnActualizaUsu = (Button) findViewById(R.id.btn_actualiza_usu);

        objBaseDatos = conecta();

        //LLeno los datos para el Spinner
        try{
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

            Toast.makeText(this, "", Toast.LENGTH_LONG).show();

        }

        //Recibo los parametros de ListaUsuarios
        if(getIntent().getStringExtra("correo") != null){
            String correo = getIntent().getStringExtra("correo");
            campo1.setText(correo);
            //hacemos el campo no editable
            campo1.setKeyListener(null);
            //Hago la consulta a la base de datos
            String[] campos = new String[]{Utilidades.CAMP_NOMBRE_TBL_USUARIO, Utilidades.CAMP_APELLIDO_TBL_USUARIO, Utilidades.CAMP_ROL_TBL_USUARIO, Utilidades.CAMP_USUARIO_TBL_USUARIO};
            String[] args = new String[]{correo};
            Cursor objCursor = objBaseDatos.query(Utilidades.NOM_TBL_USUARIO, campos, "" + Utilidades.CAMP_CORREO_TBL_USUARIO + " = ?", args, null, null, null);
            //si obtiene registros
            if(objCursor.moveToFirst()){
                do{
                    String nombreResult = objCursor.getString(0);
                    String apellidoResult = objCursor.getString(1);
                    String rolResult = objCursor.getString(2);
                    String usuarioResult = objCursor.getString(3);

                    campo2.setText(nombreResult);
                    campo3.setText(apellidoResult);
                    campo4.setText(usuarioResult);

                    //comparo los valores del spinner con el valor rol devuelto en la consulta
                    for(int i=0; i<rol.size(); i++){
                        if(rolResult.equals(rol.get(i))){
                            spinner.setSelection(i);
                        }
                    }
                }while(objCursor.moveToNext());
            }
        }

        btnEliminaUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    SQLiteDatabase objBaseDatos = conecta();
                    //rescato los datos de los campos
                    String correo = campo1.getText().toString();
                    //Elimino los registros de la base de datos
                    String[] campos = new String[]{correo};
                    long result = objBaseDatos.delete(Utilidades.NOM_TBL_USUARIO, "" + Utilidades.CAMP_CORREO_TBL_USUARIO + " = ?", campos);
                    //si hubieron filas afectadas
                    if(result != 0){
                        Toast.makeText(getApplicationContext(), "Usuario eliminado.", Toast.LENGTH_SHORT).show();
                        cambiaListaUsuarios();
                    }else{
                        Toast.makeText(getApplicationContext(), "Error al eliminar.", Toast.LENGTH_SHORT).show();
                        cambiaListaUsuarios();
                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnActualizaUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //rescato los datos
                    String usuNewCorreo = campo1.getText().toString();
                    String usuNewNombre = campo2.getText().toString();
                    String usuNewApellido = campo3.getText().toString();
                    String usuNewUsuario = campo4.getText().toString();
                    String usuNewPass = campo5.getText().toString();
                    String usuNewConPass = campo6.getText().toString();
                    String usuNewRol = spinner.getSelectedItem().toString();

                    //si los campos estan llenos
                    if(!usuNewNombre.isEmpty() && !usuNewApellido.isEmpty() && !usuNewUsuario.isEmpty()){
                        //si la opcion del spinner es seleccionar
                        if(!usuNewRol.equals("Seleccionar")){
                            SQLiteDatabase objBaseDatos = conecta();
                            //si los campos de las contraseñas estan vacios
                            if (usuNewPass.isEmpty() && usuNewConPass.isEmpty()) {
                                //listo los registros a actualizar
                                ContentValues objContent = new ContentValues();
                                objContent.put(Utilidades.CAMP_CORREO_TBL_USUARIO, usuNewCorreo);
                                objContent.put(Utilidades.CAMP_NOMBRE_TBL_USUARIO, usuNewNombre);
                                objContent.put(Utilidades.CAMP_APELLIDO_TBL_USUARIO, usuNewApellido);
                                objContent.put(Utilidades.CAMP_ROL_TBL_USUARIO, usuNewRol);
                                objContent.put(Utilidades.CAMP_USUARIO_TBL_USUARIO, usuNewUsuario);
                                actualizaUsu(objContent, usuNewCorreo);
                            } else if (!usuNewPass.isEmpty() && !usuNewConPass.isEmpty()) {
                                //si las contraseñas coinciden
                                if (usuNewPass.equals(usuNewConPass)) {
                                    //listo los registros a actualizar
                                    ContentValues objContent = new ContentValues();
                                    objContent.put(Utilidades.CAMP_CORREO_TBL_USUARIO, usuNewCorreo);
                                    objContent.put(Utilidades.CAMP_NOMBRE_TBL_USUARIO, usuNewNombre);
                                    objContent.put(Utilidades.CAMP_APELLIDO_TBL_USUARIO, usuNewApellido);
                                    objContent.put(Utilidades.CAMP_ROL_TBL_USUARIO, usuNewRol);
                                    objContent.put(Utilidades.CAMP_USUARIO_TBL_USUARIO, usuNewUsuario);
                                    objContent.put(Utilidades.CAMP_PASSWORD_TBL_USUARIO, usuNewPass);
                                    actualizaUsu(objContent, usuNewCorreo);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Seleccione un Rol.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Los campos Nombre, Apellido, Rol y Usuario son obligatorios.", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void actualizaUsu(ContentValues objContent, String usuNewCorreo){
        try{
            //actualizo los datos en la base de datos
            String[] args = new String[]{usuNewCorreo};
            long update = objBaseDatos.update(Utilidades.NOM_TBL_USUARIO, objContent, "" + Utilidades.CAMP_CORREO_TBL_USUARIO + " = ?", args);
            //si hubieron registros afectados
            if (update != 0) {
                Toast.makeText(getApplicationContext(), "Usuario actualizado.", Toast.LENGTH_SHORT).show();
                cambiaListaUsuarios();
            } else {
                Toast.makeText(getApplicationContext(), "Error al actualizar.", Toast.LENGTH_SHORT).show();
                cambiaListaUsuarios();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //metodo que conecta con la base de datos
    public SQLiteDatabase conecta(){
        //Conecto con la base de datos
        OpenHelper objConecta = new OpenHelper(this, "DB1", null, 1);
        SQLiteDatabase objBaseDatos = objConecta.getWritableDatabase();
        return objBaseDatos;
    }

    //Metodo para cambiar a ListaUsuarios
    public void cambiaListaUsuarios(){
        Intent objIntent = new Intent(this, ListaUsuarios.class);
        startActivity(objIntent);
    }
}
