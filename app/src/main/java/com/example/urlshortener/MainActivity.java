package com.example.urlshortener;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnDeleteDbListener {

    private ListView listLinks;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button manageLinks = findViewById(R.id.btn_manage_links);
        listLinks = findViewById(R.id.list_links);

        manageLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        ManageLinksActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setLinks();
    }

    private void setLinks() {
        ArrayList<String> hashIds = dbGetHashIds();

        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, hashIds);
        listLinks.setAdapter(adapter);
        listLinks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) listLinks.getItemAtPosition(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(dbGetUrl(clickedItem)));
                startActivity(intent);
            }
        });
    }

    private String dbGetUrl(String hashId) {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String url = null;

        Cursor cursor = db.query(DBHelper.TABLE_LINKS,
                null,
                DBHelper.HASH_ID + " = ?",
                new String[]{hashId},
                null, null, null);
        if (cursor.moveToFirst()) {
            int url_index = cursor.getColumnIndex(DBHelper.URL);
            url = cursor.getString(url_index);
        }
        cursor.close();

        return url;
    }

    @Override
    public void OnDeleteDb() {
        adapter.clear();
        adapter.addAll(dbGetHashIds());
        adapter.notifyDataSetChanged();
        listLinks.invalidateViews();
        listLinks.refreshDrawableState();
    }

    private ArrayList<String> dbGetHashIds() {
        ArrayList<String> hashIds = new ArrayList<>();

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(DBHelper.TABLE_LINKS,
                null, null, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            int hash_id_index = cursor.getColumnIndex(DBHelper.HASH_ID);
            do {
                hashIds.add(cursor.getString(hash_id_index));
            }
            while (cursor.moveToNext());
        } else {
            cursor.close();
        }
        return hashIds;
    }
}
