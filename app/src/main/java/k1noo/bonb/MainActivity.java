package k1noo.bonb;

import android.content.ContentValues;
import android.content.Intent;
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

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    Button trueButton, falseButton, playButton, restartButton;
    TextView FactView, welcomeText, scoreField;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Cursor cursor;
    int scoreCounter;
    boolean finishFlag;
    String factVeracity;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        trueButton = (Button) findViewById(R.id.true_button);
        falseButton = (Button) findViewById(R.id.false_button);
        playButton = (Button) findViewById(R.id.playButton);
        restartButton = (Button) findViewById(R.id.restartButton);

        FactView = (TextView) findViewById(R.id.Fact);
        welcomeText = (TextView) findViewById(R.id.welcomeText);
        scoreField = (TextView) findViewById(R.id.scoreField);

        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_FACTS, null, null, null, null, null, null);
        if (!cursor.moveToFirst()) {
            FactView.setText("EMPTY DATABASE!");
        }

        scoreCounter = 0;
        finishFlag = false;
        factVeracity = "false";


        View.OnClickListener onClickAnswerListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.playButton:

                        playButton.setVisibility(View.INVISIBLE);
                        welcomeText.setVisibility(View.INVISIBLE);
                        restartButton.setVisibility(View.INVISIBLE);
                        trueButton.setVisibility(View.VISIBLE);
                        falseButton.setVisibility(View.VISIBLE);
                        FactView.setVisibility(View.VISIBLE);
                        scoreField.setVisibility(View.VISIBLE);
                        if (cursor != null) {
                            if(!cursor.moveToFirst()) {
                                finishGame();
                            }
                        }

                        break;
                    case R.id.true_button:
                        if (factVeracity.equals("true")) {
                            Toast.makeText(MainActivity.this, "RIGHT!", Toast.LENGTH_SHORT).show();
                            ++scoreCounter;
                        } else {
                            Toast.makeText(MainActivity.this, "WRONG!", Toast.LENGTH_SHORT).show();
                            --scoreCounter;
                        }
                        Log.d("mLog", "TRUE PRESSED");
                        break;
                    case R.id.false_button:
                        if (factVeracity.equals("false")) {
                            Toast.makeText(MainActivity.this, "RIGHT!", Toast.LENGTH_SHORT).show();
                            ++scoreCounter;
                        } else {
                            Toast.makeText(MainActivity.this, "WRONG!", Toast.LENGTH_SHORT).show();
                            --scoreCounter;
                        }
                        Log.d("mLog", "FALSE PRESSED");
                        break;
                    case R.id.restartButton:
                        restart();
                        break;
                }
                int factID = cursor.getColumnIndex(DBHelper.FACT);
                if(finishFlag) { finishGame(); return;}
                factVeracity = showNextFact(cursor, factID, FactView);

                if (!cursor.moveToNext()) {
                    finishFlag = true;
                }
                scoreField.setText("SCORE: " + scoreCounter);
            }
        };

        FactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FactView.setText(R.string.questiontext);
            }

        });


        trueButton.setOnClickListener(onClickAnswerListener);
        falseButton.setOnClickListener(onClickAnswerListener);
        playButton.setOnClickListener(onClickAnswerListener);
        welcomeText.setOnClickListener(onClickAnswerListener);
        restartButton.setOnClickListener(onClickAnswerListener);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public String showNextFact(Cursor cursor, int factID, TextView textView) {
        Log.d("mLog", "ID = " + cursor.getInt(cursor.getColumnIndex(DBHelper.FACT_ID)) + " Fact: " + cursor.getString(factID)
                + " Veracity: " + cursor.getString(cursor.getColumnIndex(DBHelper.VERACITY)));
        textView.setText(cursor.getString(cursor.getColumnIndex(DBHelper.FACT)));
        return cursor.getString(cursor.getColumnIndex(DBHelper.VERACITY));

    }

    public void finishGame() {
        scoreField.setVisibility(View.INVISIBLE);
        falseButton.setVisibility(View.INVISIBLE);
        trueButton.setVisibility(View.INVISIBLE);
        FactView.setText("GAME ENDED, YOUR SCORE IS: " + scoreCounter);
        restartButton.setVisibility(View.VISIBLE);
    }

    public void restart() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
            case R.id.action_dbEdit:
                Intent intent = new Intent(this, DatabaseScreen.class);
                startActivity(intent);
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
    public void onResume() {
        super.onResume();
        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_FACTS, null, null, null, null, null, null);
        if (!cursor.moveToFirst()) {
            FactView.setText("EMPTY DATABASE!");
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
