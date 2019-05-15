package com.lcypj.animalregister;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

    private final static String tableName = "FavoriteTable";
    private final static String dbName = "FavoriteDB";
    private final static int VERSION = 1;
    private static SQLiteDatabase database;

    public DBhelper(Context context,String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = " create table if not exists " +  tableName + "("+
                " _id integer primary key autoincrement, "+
                " _kind varchar(20), "+
                " _update varchar(20), "+
                " _animalId varchar(10), " +
                " _color varchar(20), " +
                " _age varchar(20), "+
                " _sex varchar(5), " +
                " _shelter varchar(20), " +
                " _address varchar(80), " +
                " _tel varchar(20), " +
                " _remark varchar(150), " +
                "_animailImage varchar(80));";
        db.execSQL(sql);
    }

    public static SQLiteDatabase getDatabase(Context context){
        if(database==null || !database.isOpen()){
            database = new DBhelper(context,dbName,null,VERSION).getWritableDatabase();

        }
        return database;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
