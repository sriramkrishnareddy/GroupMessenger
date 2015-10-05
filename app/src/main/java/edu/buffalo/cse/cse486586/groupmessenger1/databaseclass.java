package edu.buffalo.cse.cse486586.groupmessenger1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sriram on 2/19/15.
 */
public class databaseclass extends SQLiteOpenHelper {
    public static final int db_version=1;
    public static final String db_name="PA2";
    public static final String key = "key";
    public static final String value = "value";
    public static final String table_name="MSGTABLE";
    public static final String CreateTable = "create table "+table_name+" ("+key+" text PRIMARY KEY ON CONFLICT REPLACE, "+value+" text);";
    public static final String DropTable = "DROP TABLE IF EXISTS "+table_name;
    public databaseclass(Context context){
        super(context,db_name,null,db_version);
       // System.out.println("this is fired constructor");

    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //System.out.println("this is fired table creation");

        db.execSQL(CreateTable);
        //System.out.println("This is executed create table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int  arg1, int arg2){
        //db.execSQL(DropTable);
    }
}
