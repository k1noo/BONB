package k1noo.bonb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class UsersListScreen extends AppCompatActivity {

    private UsersDB usersDB;
    private SQLiteDatabase usersdatabase;
    private Cursor usersCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userslist);

        ListView usersList = (ListView) findViewById(R.id.userlist);

        usersDB = new UsersDB(this);
        usersdatabase = usersDB.getReadableDatabase();
        usersCursor = usersdatabase.query(UsersDB.TABLE_USERS, null, null, null, null, null, null);
        usersCursor.moveToFirst();

        int count = usersCursor.getCount();
        String[] users = new String[count];
        for (int i = 0; i < count; ++i) {
            users[i] = usersCursor.getString(usersCursor.getColumnIndex(UsersDB.NAME))
                    + "\nHighscore: " + usersCursor.getInt(usersCursor.getColumnIndex(UsersDB.SCORES));
            usersCursor.moveToNext();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, users);
        usersList.setAdapter(adapter);
    }



}
