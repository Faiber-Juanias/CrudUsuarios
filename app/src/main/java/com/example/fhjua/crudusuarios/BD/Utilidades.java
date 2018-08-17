package com.example.fhjua.crudusuarios.BD;

public class Utilidades{
    //Campos para la tabla Usuario
    public static final String NOM_TBL_USUARIO = "tbl_Usuario";
    public static final String CAMP_CORREO_TBL_USUARIO = "Correo";
    public static final String CAMP_NOMBRE_TBL_USUARIO = "Nombre";
    public static final String CAMP_APELLIDO_TBL_USUARIO = "Apellido";
    public static final String CAMP_ROL_TBL_USUARIO = "Rol";
    public static final String CAMP_USUARIO_TBL_USUARIO = "Usuario";
    public static final String CAMP_PASSWORD_TBL_USUARIO = "Password";
    public static final String CREA_TBL_USUARIO = "CREATE TABLE " +
            NOM_TBL_USUARIO + " (" +
            CAMP_CORREO_TBL_USUARIO + " TEXT PRIMARY KEY, " +
            CAMP_NOMBRE_TBL_USUARIO + " TEXT, " +
            CAMP_APELLIDO_TBL_USUARIO + " TEXT, " +
            CAMP_ROL_TBL_USUARIO + " TEXT, " +
            CAMP_USUARIO_TBL_USUARIO + " TEXT, " +
            CAMP_PASSWORD_TBL_USUARIO + " TEXT)";

    //Campos para la tabla Rol
    public static final String NOM_TBL_ROL = "tbl_Rol";
    public static final String CAMP_ID_TBL_ROL = "Id";
    public static final String CAMP_NOMBRE_TBL_ROL = "Nombre";
    public static final String CREA_TBL_ROL = "CREATE TABLE " +
            NOM_TBL_ROL + " (" +
            CAMP_ID_TBL_ROL + " TEXT PRIMARY KEY, " +
            CAMP_NOMBRE_TBL_ROL + " TEXT)";
}
