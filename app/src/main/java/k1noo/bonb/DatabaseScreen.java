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
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DatabaseScreen extends AppCompatActivity {

    Button addButton, readButton, clearButton, refreshButton, manualEditButton, doneButton;
    EditText editFact, editVeracity;
    DBHelper dbHelper;


    //  ________STUDYX API_______
    private Request request;
    private final String[] subjectsIDs = {"21", "156", "17"};
    private final int SUBJECTSCOUNT = 3;
    private final int FACTSPERSUBJECTS = 3;
    public final String URL = "http://dev.moyuniver.ru/api/php/v03/";
    public final String REQCOMMAND_P1 = "api_questions.php?did=";
    public final String REQCOMMAND_P2 = "&page=1&q=100&memberid=28665485147fa7133b44cb&appid=306&appsgn=d8629af695839ba5481757a519e57fb1&appcode=&os=&ver=&width=&height=";
    public final String ANSSEARCH_P1 = "http://dev.moyuniver.ru/api/php/v03/api_answer.php?qid=";
    public final String ANSSEARCH_P2 = "&memberid=28665485147fa7133b44cb&appid=306&appsgn=d8629af695839ba5481757a519e57fb1&appcode=&os=&ver=&width=&height=";

    String tmpString = "";
    Button postButton;

    OkHttpClient client = new OkHttpClient();

    // __________________________

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_screen);


        addButton = (Button) findViewById(R.id.add_button);
        readButton = (Button) findViewById(R.id.readButton);
        clearButton = (Button) findViewById(R.id.clearButton);
        refreshButton = (Button) findViewById(R.id.refreshButton);
        manualEditButton = (Button) findViewById(R.id.manualEditing);
        doneButton = (Button) findViewById(R.id.doneButton);

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
                    case R.id.refreshButton:
                        database.delete(DBHelper.TABLE_FACTS, null, null);
                        for (int i = 0; i < SUBJECTSCOUNT; ++i) {
                            fillInBase(subjectsIDs[i]);
                        }
                        break;
                    case R.id.manualEditing:
                        editFact.setVisibility(View.VISIBLE);
                        editVeracity.setVisibility(View.VISIBLE);
                        addButton.setVisibility(View.VISIBLE);
                        readButton.setVisibility(View.VISIBLE);
                        clearButton.setVisibility(View.VISIBLE);
                        doneButton.setVisibility(View.VISIBLE);
                        refreshButton.setVisibility(View.INVISIBLE);
                        manualEditButton.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.doneButton:
                        editFact.setVisibility(View.INVISIBLE);
                        editVeracity.setVisibility(View.INVISIBLE);
                        addButton.setVisibility(View.INVISIBLE);
                        readButton.setVisibility(View.INVISIBLE);
                        clearButton.setVisibility(View.INVISIBLE);
                        doneButton.setVisibility(View.INVISIBLE);
                        refreshButton.setVisibility(View.VISIBLE);
                        manualEditButton.setVisibility(View.VISIBLE);
                        break;
                }
                dbHelper.close();
            }

        };

        addButton.setOnClickListener(onClickAnswerListener);
        readButton.setOnClickListener(onClickAnswerListener);
        clearButton.setOnClickListener(onClickAnswerListener);
        refreshButton.setOnClickListener(onClickAnswerListener);
        manualEditButton.setOnClickListener(onClickAnswerListener);
        doneButton.setOnClickListener(onClickAnswerListener);

    }

    private void fillInBase(String subjectsID) {
        String currURL = URL+REQCOMMAND_P1+subjectsID+REQCOMMAND_P2;
        request = new Request.Builder().url(currURL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("mLog", "Request Failed!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d("mLog", response.body().string());
                final String[] result = response.body().string().split(Pattern.quote("{#}"));
                int resultSize = result.length - 1;
                for (int i = 0; i < FACTSPERSUBJECTS; ++i) {
                    Double rand = Math.random() * 1000;
                    final int currFactID = (rand.intValue() % resultSize) / 3 * 3;
                    String ansURL;
                    final String veracity;
                    if ((rand.intValue()) % 2 == 0) {
                        Log.d("mLog", "Fair, question: " + result[currFactID+1]);
                        ansURL = ANSSEARCH_P1+Integer.parseInt(result[currFactID]
                                .replaceAll("[^0-9]", ""))+ANSSEARCH_P2;
                        veracity = "true";
                    } else {
                        Log.d("mLog", "Unfair, question: " + result[(currFactID+4)%resultSize]);
                        ansURL = ANSSEARCH_P1+Integer.parseInt(result[(currFactID+3)%resultSize].replaceAll("[^0-9]", ""))
                                +ANSSEARCH_P2;
                        veracity = "false";
                    }
                    request = new Request.Builder().url(ansURL).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("mLog", "Request Failed!");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String factText = result[currFactID+1] + " " + (response.body().string()
                                    .split(Pattern.quote("#")))[1];
                            Log.d("mLog", "Full fact: " + factText + " " + veracity);
                            SQLiteDatabase databaseRequest = dbHelper.getWritableDatabase();
                            ContentValues cv = new ContentValues();
                            cv.put(DBHelper.FACT, factText);
                            cv.put(DBHelper.VERACITY, veracity);
                            databaseRequest.insert(DBHelper.TABLE_FACTS, null, cv);
                        }
                    });
                }
            }
        });
    }
}
