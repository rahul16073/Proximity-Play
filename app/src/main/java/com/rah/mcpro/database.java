package com.rah.mcpro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;

public class database extends SQLiteOpenHelper {
    public static final String database="database.db";
    public static final String table="username";

    Context context;
    public database(Context context) {
        super(context, database, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
      sqLiteDatabase.execSQL("CREATE TABLE "+table+" (ID INTEGER PRIMARY KEY,user TEXT,phone TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
       sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+table);
       onCreate(sqLiteDatabase);
    }
    public void add(String s,String phone){
        SQLiteDatabase db=this.getWritableDatabase();
            ContentValues c1=new ContentValues();
            String a=Integer.toString(1);
            c1.put("ID",a);
            c1.put("user",s);
            c1.put("phone",phone);
            db.insert(table,null,c1);

    }
    public String select(int i){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.query(table, new String[]{"user"},null,null,null,null,null);
        if(c.moveToPosition(i))
        {String data = c.getString(c.getColumnIndex("user"));
        return data;}
        return "null";
    }
    public boolean update(String id,String user,String phone){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put("user",user);
        c.put("phone",phone);
        db.update(table,c,"ID=?",new String[]{id});
        return true;
    }
    public String selectphone(int i){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.query(table, new String[]{"phone"},null,null,null,null,null);
        if(c.moveToPosition(i))
        {String data = c.getString(c.getColumnIndex("phone"));
        return data;}
        return "null";
    }


}
