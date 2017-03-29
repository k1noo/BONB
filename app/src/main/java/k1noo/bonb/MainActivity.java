package k1noo.bonb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    Button trueButton, falseButton, addButton, readButton, clearButton;
    TextView FactView;
    EditText editFact, editVeracity;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        trueButton = (Button) findViewById(R.id.true_button);
        falseButton = (Button) findViewById(R.id.false_button);

        addButton = (Button) findViewById(R.id.add_button);
        readButton = (Button) findViewById(R.id.readButton);
        clearButton = (Button) findViewById(R.id.clearButton);

        FactView = (TextView) findViewById(R.id.Fact);

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
                    case R.id.true_button:
                        FactView.setText(R.string.truetext);
                        Log.d("mLog", "TRUE PRESSED");
                        break;
                    case R.id.false_button:
                        FactView.setText(R.string.falsetext);
                        Log.d("mLog", "FALSE PRESSED");
                        break;
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
                            while (cursor.moveToNext()) {
                                Log.d("mLog", "ID = " + cursor.getInt(idIndex) + " Fact: " + cursor.getString(factID)
                                + " Veracity: " + cursor.getString(veracityID));
                            }
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

        FactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FactView.setText(R.string.questiontext);
            }

        });

        addButton.setOnClickListener(onClickAnswerListener);
        readButton.setOnClickListener(onClickAnswerListener);
        clearButton.setOnClickListener(onClickAnswerListener);

        trueButton.setOnClickListener(onClickAnswerListener);
        falseButton.setOnClickListener(onClickAnswerListener);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, getString(R.string.action_settings), Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_exit:
                Toast.makeText(MainActivity.this, getString(R.string.action_exit), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private GoogleApiClient client;

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
