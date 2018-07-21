package com.example.yunju.speakerrecognition.activity;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yunju.speakerrecognition.IdProfileData;
import com.example.yunju.speakerrecognition.IdentificationProfile;
import com.example.yunju.speakerrecognition.R;
import com.example.yunju.speakerrecognition.application.ApplicationController;
import com.example.yunju.speakerrecognition.application.WavAudioRecorder;
import com.example.yunju.speakerrecognition.network.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordActivity extends AppCompatActivity {

    NetworkService service;

    private Button btnRecord, btnProfile;
    private TextView txtRecordLabel, txtStatus;

    private WavAudioRecorder mRecorder;
    private static final String mRcordFilePath = Environment.getExternalStorageDirectory() + "/testwave.wav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //미리 retrofit를 bulid 한 것을 가져온다.
        service = ApplicationController.getInstance().getNetworkService();

        btnProfile = (Button)findViewById(R.id.btnProfile);
        txtStatus = (TextView)findViewById(R.id.txtStatus);
        btnRecord = (Button)findViewById(R.id.btnRecord);
        txtRecordLabel = (TextView)findViewById(R.id.txtRecordLabel);

        mRecorder = WavAudioRecorder.getInstanse();
        mRecorder.setOutputFile(mRcordFilePath);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdProfileData data = new IdProfileData("en-US");
                Call<IdentificationProfile> createProfile = service.createProfile(data);

                createProfile.enqueue(new Callback<IdentificationProfile>() {
                    @Override
                    public void onResponse(Call<IdentificationProfile> call, Response<IdentificationProfile> response) {
                        if(response.isSuccessful()) {
                            txtStatus.setText(response.body().identificationProfileId);
                        }
                        else {
                            txtStatus.setText("failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<IdentificationProfile> call, Throwable t) {
                        txtStatus.setText("failed - onFailure");
                    }
                });

            }
        });

        btnRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN && WavAudioRecorder.State.INITIALIZING == mRecorder.getState()){
                    mRecorder.prepare();
                    mRecorder.start();
                    txtRecordLabel.setText("Recording Started...");
                } else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    mRecorder.stop();
                    mRecorder.reset();
                    txtRecordLabel.setText("Recording Stopped...");
                }
                return false;
            }
        });
    }
}
