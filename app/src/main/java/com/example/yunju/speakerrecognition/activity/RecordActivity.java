package com.example.yunju.speakerrecognition.activity;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yunju.speakerrecognition.R;
import com.example.yunju.speakerrecognition.application.WavAudioRecorder;

public class RecordActivity extends AppCompatActivity {

    private Button btnRecord;
    private TextView txtRecordLabel;

    private WavAudioRecorder mRecorder;
    private static final String mRcordFilePath = Environment.getExternalStorageDirectory() + "/testwave.wav";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        btnRecord = (Button)findViewById(R.id.btnRecord);
        txtRecordLabel = (TextView)findViewById(R.id.txtRecordLabel);

        mRecorder = WavAudioRecorder.getInstanse();
        mRecorder.setOutputFile(mRcordFilePath);

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
