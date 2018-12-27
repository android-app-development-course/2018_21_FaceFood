package com.example.chenyuyang.notebook.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class mybdHelper extends SQLiteOpenHelper {
    public static final String CREATE="create table note(id Integer primary key autoincrement,title varchar(255),content varchar(255),date date);";
    private Context mContext;
    public mybdHelper(Context c, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(c,name,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldversion,int newversion){
        ;
    }
}
