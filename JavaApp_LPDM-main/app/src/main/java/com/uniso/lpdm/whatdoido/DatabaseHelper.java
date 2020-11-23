package com.uniso.lpdm.whatdoido;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

/* Para instanciar o banco:
 *  SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
 *  SQLiteDatabase = databaseHelper.getReadableDatabase();
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "WhatDoIDo.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_query = "CREATE TABLE TASK (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "type TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "reminder_hour TEXT,"+
                "reminder_day TEXT,"+
                "isCompleted BOOLEAN"+
                ");";
        db.execSQL(create_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS TASK");
            onCreate(db);
        }
    }

    public long AddTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("title",task.Titulo);
        cv.put("type",task.Tipo);
        cv.put("description",task.Descricao);
        cv.put("reminder_hour",task.Lembrete);
        cv.put("isCompleted",0);

        long res = db.insert("TASK",null,cv);

        if (res < 1){
            Toast.makeText(context,"Erro ao criar uma tarefa", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Tarefa criada com sucesso!", Toast.LENGTH_SHORT).show();
        }

        return res;
    }
    public Cursor GetAllTasks(){
        String query = "SELECT * FROM TASK;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if (db != null)
            cursor = db.rawQuery(query,null);

        return cursor;
    }

    public Cursor GetOneTask(int id){
        String query = "SELECT * FROM TASK WHERE _id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if (db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public void UpdateTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title",task.Titulo);
        cv.put("type",task.Tipo);
        cv.put("description",task.Descricao);
        cv.put("reminder_hour",task.Lembrete);

        long result = db.update("TASK",cv,"_id = ?", new String[]{Integer.toString(task.Codigo)});
        if (result == -1)
            Toast.makeText(context,"Erro na atualização da tarefa",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,"Tarefa atualizada com sucesso",Toast.LENGTH_SHORT).show();
    }

    public void DeleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete("TASK","_id = ?", new String[]{Integer.toString(id)});
        if(result == -1)
            Toast.makeText(context, "Erro na remoção da tarefa", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,"Tarefa removida com sucesso!",Toast.LENGTH_SHORT).show();
    }

    public void CompleteTask(int id, int complete){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("isCompleted",complete);

        long result = db.update("TASK",cv,"_id = ?", new String[]{Integer.toString(id)});
        if (result == -1)
            Toast.makeText(context,"Erro na atualização da tarefa",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,"Tarefa completada",Toast.LENGTH_SHORT).show();
    }
}
