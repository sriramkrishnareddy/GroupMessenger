package edu.buffalo.cse.cse486586.groupmessenger1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * GroupMessengerProvider is a key-value table. Once again, please note that we do not implement
 * full support for SQL as a usual ContentProvider does. We re-purpose ContentProvider's interface
 * to use it as a key-value table.
 * 
 * Please read:
 * 
 * http://developer.android.com/guide/topics/providers/content-providers.html
 * http://developer.android.com/reference/android/content/ContentProvider.html
 * 
 * before you start to get yourself familiarized with ContentProvider.
 * 
 * There are two methods you need to implement---insert() and query(). Others are optional and
 * will not be tested.
 * 
 * @author stevko
 *
 */
public class GroupMessengerProvider extends ContentProvider {

    public static final String AUTH ="edu.buffalo.cse.cse486586.groupmessenger1";
    public static final Uri GMP_URI=Uri.parse("content://edu.buffalo.cse.cse486586.groupmessenger1.provider");
    databaseclass dbc;
    final static int com =1;
    SQLiteDatabase db;
   public static final UriMatcher uri_matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
       uri_matcher.addURI(AUTH,databaseclass.table_name,com);
    }

    @Override
    public boolean onCreate() {
        dbc = new databaseclass(getContext());
        // If you need to perform any one-time initialization task, please do it here.
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = dbc.getWritableDatabase();
//        long insert_statement=db.insert(databaseclass.table_name,null,values);
//        Uri url = ContentUris.withAppendedId(uri,insert_statement);
        db.insert(databaseclass.table_name,null,values);
        getContext().getContentResolver().notifyChange(uri,null);
        Log.v("insert", values.toString());
        return uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        /*
         * TODO: You need to implement this method. Note that you need to return a Cursor object
         * with the right format. If the formatting is not correct, then it is not going to work.
         *
         * If you use SQLite, whatever is returned from SQLite is a Cursor object. However, you
         * still need to be careful because the formatting might still be incorrect.
         *
         * If you use a file storage option, then it is your job to build a Cursor * object. I
         * recommend building a MatrixCursor described at:
         * http://developer.android.com/reference/android/database/MatrixCursor.html
         */
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(databaseclass.table_name);


       Cursor cursor;
        //db = dbc.getWritableDatabase();
        //System.out.println("projection is:"+selection);
        selection = "key ='"+selection+"'";
        cursor = qb.query(dbc.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        Log.v("query", selection);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // You do not need to implement this.

        return null;
    }







    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }
}
