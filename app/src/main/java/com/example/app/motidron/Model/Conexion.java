package com.example.app.motidron.Model;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public final class Conexion {



    private static String DRIVER = "com.mysql.jdbc.Driver";
    private static String DATABASE_URL = "jdbc:mysql://35.232.187.42:3306/reconocimiento";
    private static String USER = "eliza";
    private static String PASSWORD = "eliza2431";

    public static Connection generarConexion() throws Exception {
        Connection conexion;
        Class.forName(DRIVER).newInstance();
        conexion = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        return conexion;
    }

}

