package k1noo.bonb;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    Button trueButton, falseButton, playButton, restartButton;
    TextView FactView, welcomeText, scoreField;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Cursor cursor;
    int scoreCounter;
    boolean finishFlag;
    String factVeracity;
    MenuItem signOutAction;


    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    SignInButton signInButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        signOutAction = (MenuItem) findViewById(R.id.action_sign_out);

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
                    case R.id.sign_in_button:
                        signIn();
                        break;
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
        signInButton.setOnClickListener(onClickAnswerListener);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("mLog", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            signInButton.setVisibility(View.INVISIBLE);
            playButton.setVisibility(View.VISIBLE);
            signOutAction.setEnabled(true);
            Toast.makeText(MainActivity.this,"Signed In as: " + acct.getDisplayName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Sign In Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut() {
        signOutAction.setEnabled(false);
        playButton.setVisibility(View.INVISIBLE);
        signInButton.setVisibility(View.VISIBLE);
        Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
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
        signOutAction = menu.findItem(R.id.action_sign_out);
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
            case R.id.action_sign_out:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
