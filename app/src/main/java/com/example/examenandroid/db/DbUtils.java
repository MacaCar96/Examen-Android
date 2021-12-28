package com.example.examenandroid.db;

public class DbUtils {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "peliculas.db";

    /**
     * CONSTRUCIÓN DE LAS VARIABLES
     * - TABLA
     * - CAMPOS
     * */
    public static final String TABLE_PELICULAS = "tbl_peliculas";
    public static final String T_COLUMN_ID_PELICULAS = "id";
    public static final String T_COLUMN_BACKDROP_PATH_PELICULAS = "backdrop_path";
    public static final String T_COLUMN_ORIGINAL_TITLE_PELICULAS = "original_title";
    public static final String T_COLUMN_OVERVIEW_PELICULAS = "overview";
    public static final String T_COLUMN_RELEASE_DATE_PELICULAS = "release_date";
    public static final String T_COLUMN_TITLE_PELICULAS = "title";


    public static int getDbVersion() { return DB_VERSION; } // Número de versión DB
    public static String getDbName() { return DB_NAME; } // Nombre de la DB

    /**
     * CONSTRUCIÓN DE LAS TABLAS
     * */
    // CONSTRUCIÓN DE LA TABLE PELÍCULAS
    public static String getTablePeliculasCreate() {
        return "CREATE TABLE " + TABLE_PELICULAS + " (" +
                T_COLUMN_ID_PELICULAS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                T_COLUMN_BACKDROP_PATH_PELICULAS + " TEXT NULL, " +
                T_COLUMN_ORIGINAL_TITLE_PELICULAS + " TEXT NOT NULL, " +
                T_COLUMN_OVERVIEW_PELICULAS + " TEXT NOT NULL, " +
                T_COLUMN_RELEASE_DATE_PELICULAS + " TEXT NOT NULL, " +
                T_COLUMN_TITLE_PELICULAS + " TEXT NOT NULL)";
    }

    /**
     * ELIMINACIÓN DE LAS TABLAS
     * */

    public  static String getTablePeliculasDelete() {
        return "DROP TABLE " + TABLE_PELICULAS;
    }

}
