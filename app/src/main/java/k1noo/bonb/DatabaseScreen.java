package k1noo.bonb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DatabaseScreen extends AppCompatActivity {

    Button addButton, readButton, clearButton;
    EditText editFact, editVeracity;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_screen);

        addButton = (Button) findViewById(R.id.add_button);
        readButton = (Button) findViewById(R.id.readButton);
        clearButton = (Button) findViewById(R.id.clearButton);

        editVeracity = (EditText) findViewById(R.id.editVeracity);
        editFact = (EditText) findViewById(R.id.editFact);

        dbHelper = new DBHelper(this);

        View.OnClickListener onClickAnswerListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fact = editFact.getText().toString();
                String veracity = editVeracity.getText().toString();

                SQLiteDatabase database = dbHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();

                switch (view.getId()) {
                    case R.id.add_button:
                        Log.d("mLog", "ADD PRESSED");
                        contentValues.put(DBHelper.FACT, fact);
                        contentValues.put(DBHelper.VERACITY, veracity);

                        database.insert(DBHelper.TABLE_FACTS, null, contentValues);
                        break;
                    case R.id.readButton:
                        Log.d("mLog", "READ PRESSED");

                        Cursor cursor = database.query(DBHelper.TABLE_FACTS, null, null, null, null, null, null);

                        if (cursor.moveToFirst()) {
                            int idIndex = cursor.getColumnIndex(DBHelper.FACT_ID);
                            int factID = cursor.getColumnIndex(DBHelper.FACT);
                            int veracityID = cursor.getColumnIndex(DBHelper.VERACITY);
                            do {
                                Log.d("mLog", "ID = " + cursor.getInt(idIndex) + " Fact: " + cursor.getString(factID)
                                        + " Veracity: " + cursor.getString(veracityID));
                            } while (cursor.moveToNext());
                        } else {
                            Log.d("mLog", "0 items");
                        }
                        cursor.close();
                        break;
                    case R.id.clearButton:
                        Log.d("mLog", "CLEAR PRESSED");
                        database.delete(DBHelper.TABLE_FACTS, null, null);
                        break;
                }
                dbHelper.close();
            }

        };

        addButton.setOnClickListener(onClickAnswerListener);
        readButton.setOnClickListener(onClickAnswerListener);
        clearButton.setOnClickListener(onClickAnswerListener);

    }
}
