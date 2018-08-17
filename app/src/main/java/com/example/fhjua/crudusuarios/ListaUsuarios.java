package com.example.fhjua.crudusuarios;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fhjua.crudusuarios.BD.OpenHelper;
import com.example.fhjua.crudusuarios.BD.Utilidades;

import java.util.ArrayList;

public class ListaUsuarios extends AppCompatActivity{

    private EditText campo1;
    private TextView totalusuarios;
    private Spinner spinner1;
    private ListView lista;
    private Button btnAgregaUsu;
    private SQLiteDatabase objBaseDatos;
    private ArrayList<String> roles;
    private ArrayList<String> listaUsuarios;
    private ArrayList<String> listaCorreos;
    private int totalusu;
    private TextWatcher text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);

        campo1 = (EditText) findViewById(R.id.camp_busca_usu);
        totalusuarios = (TextView) findViewById(R.id.view_total_usu);
        spinner1 = (Spinner) findViewById(R.id.spinner2);
        btnAgregaUsu = (Button) findViewById(R.id.btn_agrega_usu);
        lista = (ListView) findViewById(R.id.lista);
        objBaseDatos = conecta();

        btnAgregaUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(getApplicationContext(), RegistraUsuarios.class);
                startActivity(objIntent);
            }
        });

        text = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String campoBusqueda= campo1.getText().toString();
                campoBuscaUsuario(campoBusqueda);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        campo1.addTextChangedListener(text);

        //Mostramos los datos en el Spinner
        try{
            //hago la consulta a la base de datos
            String[] campo = new String[]{Utilidades.CAMP_NOMBRE_TBL_ROL};
            Cursor objCursor = objBaseDatos.query(Utilidades.NOM_TBL_ROL, campo, null, null, null, null, null);
            //si obtengo registros
            if(objCursor.moveToFirst()){
                roles = new ArrayList<>();
                roles.add("Seleccionar");
                roles.add("Todos");
                do{
                    roles.add(objCursor.getString(0));
                }while(objCursor.moveToNext());
                ArrayAdapter<String> objAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roles);
                spinner1.setAdapter(objAdapter);
                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position != 0){
                            busquedaRol(position, roles);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }catch(Exception e){
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //Mostramos los datos en la lista
        try{
            //Hago la consulta a la base de datos
            String[] campos = new String[]{Utilidades.CAMP_APELLIDO_TBL_USUARIO, Utilidades.CAMP_NOMBRE_TBL_USUARIO, Utilidades.CAMP_CORREO_TBL_USUARIO};
            final Cursor objCursor = objBaseDatos.query(Utilidades.NOM_TBL_USUARIO, campos, null, null,
                    null, null, "" + Utilidades.CAMP_APELLIDO_TBL_USUARIO + " ASC");
            muestraLista(objCursor);
        }catch(Exception e){
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //metodo que conecta
    public SQLiteDatabase conecta(){
        //Conecto con la base de datos
        OpenHelper objConecta = new OpenHelper(this, "DB1", null, 1);
        SQLiteDatabase objBaseDatos = objConecta.getWritableDatabase();
        return objBaseDatos;
    }

    //Metodo para el boton buscar
    public void campoBuscaUsuario(String busqueda){
        try{
            //conectamos a la base de datos
            SQLiteDatabase objBaseDatos = conecta();
            //listamos los campos que devolvera la consulta
            String[] campos = new String[]{Utilidades.CAMP_APELLIDO_TBL_USUARIO, Utilidades.CAMP_NOMBRE_TBL_USUARIO, Utilidades.CAMP_CORREO_TBL_USUARIO};
            String opcionSpinner = spinner1.getSelectedItem().toString();
            if(opcionSpinner.equals("Coordinador")) {
                Cursor objCursor = objBaseDatos.query(Utilidades.NOM_TBL_USUARIO, campos, "" + Utilidades.CAMP_ROL_TBL_USUARIO +
                                " = 'Coordinador' AND " + campos[0] + " LIKE '" + busqueda + "%' OR " + Utilidades.CAMP_ROL_TBL_USUARIO +
                                " = 'Coordinador' AND " + campos[1] + " LIKE '" + busqueda + "%' OR " + Utilidades.CAMP_ROL_TBL_USUARIO +
                                " = 'Coordinador' AND " + campos[2] + " LIKE '" + busqueda + "%'", null,
                        null, null, "" + campos[0] + " ASC");
                muestraLista(objCursor);
            }else if(opcionSpinner.equals("Administrador")){
                Cursor objCursor = objBaseDatos.query(Utilidades.NOM_TBL_USUARIO, campos, "" + Utilidades.CAMP_ROL_TBL_USUARIO +
                                " = 'Administrador' AND " + campos[0] + " LIKE '" + busqueda + "%' OR " + Utilidades.CAMP_ROL_TBL_USUARIO +
                                " = 'Administrador' AND " + campos[1] + " LIKE '" + busqueda + "%' OR " + Utilidades.CAMP_ROL_TBL_USUARIO +
                                " = 'Administrador' AND " + campos[2] + " LIKE '" + busqueda + "%'", null,
                        null, null, "" + campos[0] + " ASC");
                muestraLista(objCursor);
            }else if(opcionSpinner.equals("Competidor")){
                Cursor objCursor = objBaseDatos.query(Utilidades.NOM_TBL_USUARIO, campos, "" + Utilidades.CAMP_ROL_TBL_USUARIO +
                                " = 'Competidor' AND " + campos[0] + " LIKE '" + busqueda + "%' OR " + Utilidades.CAMP_ROL_TBL_USUARIO +
                                " = 'Competidor' AND " + campos[1] + " LIKE '" + busqueda + "%' OR " + Utilidades.CAMP_ROL_TBL_USUARIO +
                                " = 'Competidor' AND " + campos[2] + " LIKE '" + busqueda + "%'", null,
                        null, null, "" + campos[0] + " ASC");
                muestraLista(objCursor);
            }else if(opcionSpinner.equals("Seleccionar")){
                Cursor objCursor = objBaseDatos.query(Utilidades.NOM_TBL_USUARIO, campos, "" + campos[0] +
                                " LIKE '" + busqueda + "%' OR " + campos[1] +
                                " LIKE '" + busqueda + "%' OR " + campos[2] +
                                " LIKE '" + busqueda + "%'", null,
                        null, null, "" + campos[0] + " ASC");
                muestraLista(objCursor);
            }
        }catch(Exception e){
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //metodo que muestra el filtro de busqueda segun el rol seleccionado
    public void muestraLista(Cursor objCursor){
        //si obtengo regstros
        if (objCursor.moveToFirst()) {
            //almaceno el numero de registros
            totalusu = objCursor.getCount();
            //muestro el total de usuarios
            totalusuarios.setText("Usuarios Totales: " + totalusu);

            if (lista.getVisibility() == View.INVISIBLE || lista.getVisibility() == View.VISIBLE) {
                //mostramos la lista
                lista.setVisibility(View.VISIBLE);
                //recorremos el cursor
                listaUsuarios = new ArrayList<>();
                listaCorreos = new ArrayList<>();
                do {
                    listaUsuarios.add(objCursor.getString(0) + " " + objCursor.getString(1) + " / " + objCursor.getString(2));
                    listaCorreos.add(objCursor.getString(2));
                } while (objCursor.moveToNext());
                //adaptamos el arraylist
                ArrayAdapter<String> objAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaUsuarios);
                lista.setAdapter(objAdapter);
                //asignamos el evento a la lista
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String correo = "";
                        correo = listaCorreos.get(position);
                        enviaDatos(correo);
                    }
                });
            }
        } else {
            totalusuarios.setText("Usuarios Totales: 0");
            if (lista.getVisibility() == View.VISIBLE) {
                lista.setVisibility(View.INVISIBLE);
            }
            Toast.makeText(this, "Ningun usuario.", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo que envia parametros a RegistraUsuarios
    public void enviaDatos(String correo){
        //String correo = listaCorreos.get(0);
        Intent objIntent = new Intent(this, EditaUsuarios.class);
        objIntent.putExtra("correo", correo);
        startActivity(objIntent);
    }

    //metodo que define el evento de acuerdo a la seleccion del spinner
    public void busquedaRol(int i, ArrayList<String> roles){
        String seleccion="";
        if(i == 2 || i == 3 || i == 4){
            String datoCampo = campo1.getText().toString();
            //verifica si hay texto luego de pulsar un item del spinner
            if(datoCampo == null){
                seleccion = roles.get(i);
                //Toast.makeText(getApplicationContext(), "Valor: " + seleccion, Toast.LENGTH_LONG).show();

                //Hago la consulta a la base de datos
                String[] campos = new String[]{Utilidades.CAMP_APELLIDO_TBL_USUARIO, Utilidades.CAMP_NOMBRE_TBL_USUARIO, Utilidades.CAMP_CORREO_TBL_USUARIO};
                final String[] args = new String[]{seleccion};
                Cursor objCursor = objBaseDatos.query(Utilidades.NOM_TBL_USUARIO, campos, "" + Utilidades.CAMP_ROL_TBL_USUARIO + " = ?", args, null, null,
                        "" + Utilidades.CAMP_APELLIDO_TBL_USUARIO + " ASC");
                muestraLista(objCursor);
            }else{
                campoBuscaUsuario(datoCampo);
            }
        }else if(i == 1){
            reiniciarActivity(this);
        }
    }

    //reinicia una Activity
    public static void reiniciarActivity(Activity actividad){
        Intent intent=new Intent();
        intent.setClass(actividad, actividad.getClass());
        //llamamos a la actividad
        actividad.startActivity(intent);
        //finalizamos la actividad actual
        actividad.finish();
    }

    //Definimos los metodos para las opciones del menu
    @Override
    public boolean onCreateOptionsMenu(Menu mimenu){
        //llamamos al menu
        getMenuInflater().inflate(R.menu.menu_lista_usuarios, mimenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        //capturamso el id pulsado por el usuario
        int id = menuItem.getItemId();
        if(id == R.id.login){
            Intent objIntent = new Intent(this, LoginUsuarios.class);
            startActivity(objIntent);
        }
        //le pasamos menuItem al metodo de la clase padre
        return super.onOptionsItemSelected(menuItem);
    }
}
