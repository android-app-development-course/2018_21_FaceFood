package com.example.chenyuyang.notebook.module;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class note implements  Serializable{
    static SQLiteDatabase db=null;
    public static void setDB(SQLiteDatabase db){
        note.db=db;
        note test = new note("日志1","test",new Date());
        test.storeMe();
        test = new note("日志2","test",new Date());
        test.storeMe();
        test = new note("日志3","test",new Date());
        test.storeMe();
        test = new note("日志4","test",new Date());
        test.storeMe();
    }
    public static ArrayList<note> selectAll(){
        Cursor cursor = db.query("note",null,null,null,null,null,null);
        ArrayList<note> forRet = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                note forInsert = new note(cursor);
                forRet.add(forInsert);
            }while(cursor.moveToNext());
        }
        return forRet;
    }
    public static ArrayList<note> select(String title){
        Cursor cursor=db.query("note",null,"title like ?",new String[]{"%"+title+"%"},null,null,null);
        ArrayList<note> forRet = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                note forInsert =new note(cursor);
                forRet.add(forInsert);
            }while(cursor.moveToNext());
        }
        return forRet;
    }
    private int id;
    public int getId(){
        return id;
    }
    private Date date=null;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String title=null;
    private String content=null;

    public note(String title,String content,Date date){
        this.id=-1;
        this.date=date;
        this.title=title;
        this.content=content;
    }
    public note(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex("id"));
        this.title=cursor.getString(cursor.getColumnIndex("title"));
        this.content=cursor.getString(cursor.getColumnIndex("content"));
        String strDate = cursor.getString(cursor.getColumnIndex("date"));
        this.date = note.str2Date(strDate);
    }

    public String getLongDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年-MM月-dd日 HH时mm分");
        if(date==null){
            return sdf.format(new Date());
        }
        return sdf.format(date);
    }
    static Date str2Date(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年-MM月-dd日 HH时mm分");
        try {
            return sdf.parse(date);
        }catch (Exception e){
            return new Date();
        }
    }
    public String getSimpleDate(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        if(date==null){
            return sdf.format(new Date());
        }
        return sdf.format(date);
    }
    public static List<note> getNotes(){
        return selectAll();
    }
    public void storeMe(){
        ContentValues values = new ContentValues();
        values.put("title", this.title);
        values.put("content", this.content);
        values.put("date", this.getLongDate());
        if(this.id<0) {
            db.insert("note", null, values);
        }else{
            db.update("note",values,"id=?",new String[]{String.valueOf(id)});
        }
    }
    public void removeMe() throws Exception {
        if(id<0){
            throw new Exception("不存在在数据库中");
        }
        db.delete("note","id=?",new String[]{String.valueOf(id)});
    }
}
