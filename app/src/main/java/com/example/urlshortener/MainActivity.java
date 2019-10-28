package com.example.urlshortener;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    ApiRequestManager apiRequestManager;
    Button manageLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manageLinks = findViewById(R.id.btn_manage_links);
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

//        apiRequestManager = new ApiRequestManager();
//        apiRequestManager.createShortenedLink(this,
//                "https://www.youtube.com/");
    }
}
