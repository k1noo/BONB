package k1noo.bonb;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "factsDB";
    public static final String TABLE_FACTS = "factsTab";

    public static final String FACT_ID = "_id";
    public static final String FACT = "fact";
    public static final String VERACITY = "veracity";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_FACTS + "(" + FACT_ID +
                " integer primary key, " + FACT + " text, " + VERACITY + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_FACTS);

        onCreate(db);
    }
}
