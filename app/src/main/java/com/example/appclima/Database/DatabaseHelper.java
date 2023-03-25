package com.example.appclima.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.example.appclima.Dto.Historico;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    //    invoiceAndEstimate.exdb
    public static final String DATABASE_NAME = "BDAppClima.exdb";
    public static final int DATABASE_VERSION = 1;

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {

        sQLiteDatabase.execSQL("CREATE TABLE " + Historico.TABLE_NAME + "("
                + Historico.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Historico.COLUMN_ICONO + " TEXT,"
                + Historico.COLUMN_TEMPERATURA + " TEXT,"
                + Historico.COLUMN_SUMMARY + " TEXT,"
                + Historico.COLUMN_TIME + " TEXT,"
                + Historico.COLUMN_LOCATION + " TEXT,"
                + Historico.COLUMN_COORDENADAS + " TEXT "
                + ")");
    }



    public void deleteItem(int item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Historico.TABLE_NAME, Historico.COLUMN_ID + " = ?",
                new String[]{String.valueOf(item)});
        db.close();
    }


    public long insertHistorico(String  icono, String  temperatura, String  summary, String time, String  location, String coordenadas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Historico.COLUMN_ICONO, icono);
        values.put(Historico.COLUMN_TEMPERATURA, temperatura);
        values.put(Historico.COLUMN_SUMMARY, summary);
        values.put(Historico.COLUMN_TIME, time);
        values.put(Historico.COLUMN_LOCATION, location);
        values.put(Historico.COLUMN_COORDENADAS, coordenadas);
        long id = db.insert(Historico.TABLE_NAME, null, values);
        db.close();
        return id;
    }


    @SuppressLint("Range")
    public List<Historico> getHistoricos() {
        List<Historico> imgs = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+Historico.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Historico img = new Historico();
                img.setId(cursor.getInt(cursor.getColumnIndex(Historico.COLUMN_ID)));
                img.setIcono(cursor.getString(cursor.getColumnIndex(Historico.COLUMN_ICONO)));
                img.setTemperatura(cursor.getString(cursor.getColumnIndex(Historico.COLUMN_TEMPERATURA)));
                img.setSummary(cursor.getString(cursor.getColumnIndex(Historico.COLUMN_SUMMARY)));
                img.setTime(cursor.getString(cursor.getColumnIndex(Historico.COLUMN_TIME)));
                img.setLocation(cursor.getString(cursor.getColumnIndex(Historico.COLUMN_LOCATION)));
                img.setCoordenadas(cursor.getString(cursor.getColumnIndex(Historico.COLUMN_COORDENADAS)));
                imgs.add(img);
            } while (cursor.moveToNext());
        }
        db.close();
        return imgs;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.setForeignKeyConstraintsEnabled(true);
    }
}
