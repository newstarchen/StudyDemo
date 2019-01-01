package com.example.convest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private List<Two> twoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(SecondActivity.this, R.layout.second_item, title);
       MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"MusicStore.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Music", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
               // Two two = new Two();
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String Artist = cursor.getString(cursor.getColumnIndex("Artist"));
                Two two = new Two(title, Artist);
                twoList.add(two);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        TwoAdapter adapter = new TwoAdapter(SecondActivity.this, R.layout.second_item, twoList);
        ListView listView = (ListView) findViewById(R.id.second_view);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"MusicStore.db", null, 1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("Music", null, null);
            Toast.makeText(this, "你删除了所有记录", Toast.LENGTH_SHORT).show();

        }
        return true;
    }
}
