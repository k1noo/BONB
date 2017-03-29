package k1noo.bonb;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.converter.StringToIntConverter;

public class MainActivity extends AppCompatActivity {

    Button trueButton, falseButton, addButton;
    TextView QuestionView;
    EditText editTopic, editQuestion;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        trueButton = (Button) findViewById(R.id.true_button);
        falseButton = (Button) findViewById(R.id.false_button);
        QuestionView = (TextView) findViewById(R.id.Question);
        addButton = (Button) findViewById(R.id.add_button);
        editQuestion = (EditText) findViewById(R.id.editQuestion);
        editTopic = (EditText) findViewById(R.id.editTopic);

        dbHelper = new DBHelper(this);

        View.OnClickListener onClickAnswerListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String topic = editTopic.getText().toString();
                String question = editQuestion.getText().toString();

                SQLiteDatabase database = dbHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();

                switch (view.getId()) {
                    case R.id.true_button:
                        QuestionView.setText(R.string.truetext);
                        Toast toastNotifTrue = Toast.makeText(MainActivity.this, "TRUE BUTTON PRESSED", Toast.LENGTH_SHORT);
                        toastNotifTrue.show();
                        break;
                    case R.id.false_button:
                        QuestionView.setText(R.string.falsetext);
                        break;
                    case R.id.add_button:
                        contentValues.put(DBHelper.QUESTION, question);
                        contentValues.put(DBHelper.ANSWER, topic);

                        database.insert(DBHelper.TABLE_QUESTIONS, null, contentValues);
                        break;
                }
            }
        };

        QuestionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionView.setText(R.string.questiontext);
            }

        });

        addButton.setOnClickListener(onClickAnswerListener);
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
