package kr.soen.termproject;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

    public DataBase(Context context){
        super(context,"avg.db",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE avg ( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +" location TEXT , time TEXT, record TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL("DROP TABLE IF EXISTS db_table");
        onCreate(db);
    }
}