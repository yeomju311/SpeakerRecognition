package com.example.yunju.speakerrecognition.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.yunju.speakerrecognition.R;
import com.example.yunju.speakerrecognition.application.ApplicationController;
import com.example.yunju.speakerrecognition.network.NetworkService;

public class MainActivity extends AppCompatActivity {

    Button btnEnrollment, btnIdentification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEnrollment = (Button)findViewById(R.id.btnEnrollment);
        btnIdentification = (Button)findViewById(R.id.btnIdentification);

        btnEnrollment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                startActivity(intent);
            }
        });
    }


}
