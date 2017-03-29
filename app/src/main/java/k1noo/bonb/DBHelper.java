package k1noo.bonb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "questionsDB";
    public static final String TABLE_QUESTIONS = "questionsTab";

    public static final String QUESTION_ID = "_id";
    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_QUESTIONS + "(" + QUESTION_ID +
                " integer primary key, " + QUESTION + " text, " + ANSWER + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " +  TABLE_QUESTIONS);

        onCreate(db);
    }
}
