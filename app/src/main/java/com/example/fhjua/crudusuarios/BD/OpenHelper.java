package com.example.fhjua.crudusuarios.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class OpenHelper extends SQLiteOpenHelper{
    public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creo la tabla Usuario
        db.execSQL(Utilidades.CREA_TBL_USUARIO);
        //Creo la tabla Rol
        db.execSQL(Utilidades.CREA_TBL_ROL);
        //Inserta valores en la tabla Rol
        this.insertaValoresRol(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Metodo para insertar en la tabla Rol
    public void insertaValoresRol(SQLiteDatabase db){
        //Listo los valores a insertar
        ContentValues objContent = new ContentValues();
        objContent.put(Utilidades.CAMP_ID_TBL_ROL, "1");
        objContent.put(Utilidades.CAMP_NOMBRE_TBL_ROL, "Coordinador");
        //Inserto los valores
        db.insert(Utilidades.NOM_TBL_ROL, null, objContent);

        //Listo los valores a insertar
        ContentValues objContent1 = new ContentValues();
        objContent1.put(Utilidades.CAMP_ID_TBL_ROL, "2");
        objContent1.put(Utilidades.CAMP_NOMBRE_TBL_ROL, "Administrador");
        //Inserto los valores
        db.insert(Utilidades.NOM_TBL_ROL, null, objContent1);

        //Listo los valores a insertar
        ContentValues objContent2 = new ContentValues();
        objContent2.put(Utilidades.CAMP_ID_TBL_ROL, "3");
        objContent2.put(Utilidades.CAMP_NOMBRE_TBL_ROL, "Competidor");
        //Inserto los valores
        db.insert(Utilidades.NOM_TBL_ROL, null, objContent2);
    }

    //Metodo para insertar en la tabla Usuario
    public String insertaValoresUsuario(String camposTblUsuario[], SQLiteDatabase db){
        try {
            //Listo los valores a insertar
            ContentValues objContent = new ContentValues();
            objContent.put(Utilidades.CAMP_CORREO_TBL_USUARIO, camposTblUsuario[0]);
            objContent.put(Utilidades.CAMP_NOMBRE_TBL_USUARIO, camposTblUsuario[1]);
            objContent.put(Utilidades.CAMP_APELLIDO_TBL_USUARIO, camposTblUsuario[2]);
            objContent.put(Utilidades.CAMP_ROL_TBL_USUARIO, camposTblUsuario[3]);
            objContent.put(Utilidades.CAMP_USUARIO_TBL_USUARIO, camposTblUsuario[4]);
            objContent.put(Utilidades.CAMP_PASSWORD_TBL_USUARIO, camposTblUsuario[5]);
            //Inserto los valores
            long inserta = db.insert(Utilidades.NOM_TBL_USUARIO, null, objContent);
            if (inserta != 0) {
                return "Usuario Guardado.";
            } else {
                return "Error al Guardar.";
            }
        }catch(Exception e){
            return "Error: " + e.getMessage();
        }
    }
}
