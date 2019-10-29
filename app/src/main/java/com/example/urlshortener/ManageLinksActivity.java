package com.example.urlshortener;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ManageLinksActivity extends AppCompatActivity {

    EditText link_to_shorten, shortened_link,
            link_to_read, normal_link;
    ApiRequestManager apiRequestManager;
    Button btn_shorten, btn_read;
    OnDeleteDbListener onDeleteDbListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_links);

        onDeleteDbListener = null;

        link_to_shorten = findViewById(R.id.link_to_shorten);
        shortened_link = findViewById(R.id.shortened_link);
        link_to_read = findViewById(R.id.link_to_read);
        normal_link = findViewById(R.id.normalized_link);

        btn_shorten = findViewById(R.id.btn_shorten);
        btn_read = findViewById(R.id.btn_read);

        btn_shorten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shortenLink(link_to_shorten.getText().toString());
            }
        });

        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalizeLink(link_to_read.getText().toString());
            }
        });

        apiRequestManager = new ApiRequestManager();
        apiRequestManager.setOnLinkShortenedListener(
                new ApiRequestManager.onLinkShortenedListener() {
                    @Override
                    public void onLinkShortened(Link link_with_hashId) {
                        setShortenedLink(link_with_hashId);
                    }
                });
        apiRequestManager.setOnLinkReadListener(
                new ApiRequestManager.onLinkReadListener() {
                    @Override
                    public void onLinkRead(Link link_with_url) {
                        setNormalizedLink(link_with_url);
                    }
                });
    }

    private void shortenLink(String url) {
        Link link = new Link(url, null);
        apiRequestManager.createShortenedLink(this, link);
    }

    private void normalizeLink(String hashId) {
        Link link = new Link(null, hashId);
        apiRequestManager.readShortenedLink(this, link);
    }

    private void setShortenedLink(Link link) {
        shortened_link.setText(link.getHashId());

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery(
                "select count(*) from " + DBHelper.TABLE_LINKS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count >= 10) {
            database.execSQL("delete from " + DBHelper.TABLE_LINKS);
            if (onDeleteDbListener != null)
                onDeleteDbListener.OnDeleteDb();
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.URL, link.getUrl());
        contentValues.put(DBHelper.HASH_ID, link.getHashId());

        database.insert(DBHelper.TABLE_LINKS, null, contentValues);
    }

    private void setNormalizedLink(Link link) {
        normal_link.setText(link.getUrl());
    }
}
