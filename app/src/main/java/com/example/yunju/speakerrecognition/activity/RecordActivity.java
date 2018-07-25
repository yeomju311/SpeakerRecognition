package com.example.yunju.speakerrecognition.activity;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yunju.speakerrecognition.EnrollmentResponse;
import com.example.yunju.speakerrecognition.IdProfileData;
import com.example.yunju.speakerrecognition.IdentificationProfile;
import com.example.yunju.speakerrecognition.R;
import com.example.yunju.speakerrecognition.application.ApplicationController;
import com.example.yunju.speakerrecognition.application.WavAudioRecorder;
import com.example.yunju.speakerrecognition.network.NetworkService;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.framed.ErrorCode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordActivity extends AppCompatActivity {

    NetworkService service;

    String speakerId;

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

        final String[] result = {null};

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdProfileData data = new IdProfileData("en-US");
                Call<IdentificationProfile> createProfile = service.createProfile(data);

                createProfile.enqueue(new Callback<IdentificationProfile>() {
                    @Override
                    public void onResponse(Call<IdentificationProfile> call, Response<IdentificationProfile> response) {
                        if(response.isSuccessful()) {
                            // Response 가 성공한 상태
                            speakerId = response.body().identificationProfileId;
                            txtStatus.setText(response.body().identificationProfileId);
                        }
                        else {
                            // Response 가 실패한 상태
                            // 서버와의 통신에 성공하였지만, 서버 내부 동작 중에서 잘못된 점이 확인되어,
                            // 통신에는 성공한 상태로 설정하고, Body 에 실패한 정보를 추가
                            // ex) 서버에서 잘못된 params 를 체크하여 잘못된 정보가 있다고 return
                            //Log.i("yunju-profile", response.errorBody().string());
                            try {
                                result[0] = response.errorBody().string();
                                Gson gson = new Gson();
                                ErrorCode errorCode = gson.fromJson(result[0], ErrorCode.class);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            txtStatus.setText(result[0]);

                        }
                    }

                    @Override
                    public void onFailure(Call<IdentificationProfile> call, Throwable t) {
                        // Request 가 실패한 상태 ( 통신 자체, 서버의 구현 이외의 에러 발생 )
                        // ex) 통신이 불가, 서버와의 연결 실패 등
                        Log.i("yunju-profile", t.toString());
                        Log.i("yunju-profile", t.getMessage());
                        txtStatus.setText("failed - onFailure");
                    }
                });
            }
        });

        //service.profileId("identificationProfiles/"+speakerId+"/enroll");

        /*
        File audioFile = new File(fileDirectory + "my_voice.wav");
RequestBody requestAudioFile = RequestBody.create(MediaType.parse("application/octet-stream"), audioFile);
Call<EnrollmentResponse> call = apiService.createEnrollment(PROFILE_ID_TEST,"audio/wav; samplerate=1600",API_KEY,requestAudioFile);
call.enqueue(new Callback<EnrollmentResponse>() {
    @Override
    public void onResponse(Call<EnrollmentResponse> call, Response<EnrollmentResponse> response) {
        Log.d(TAG,"Upload success");
        RequestError error = ErrorUtils.parseError(response);
        Log.d("error message", error.toString());
        Log.d(TAG,"Response: " + response.body().toString());
    }

    @Override
    public void onFailure(Call<EnrollmentResponse> call, Throwable t) {
        Log.d(TAG,"Upload error: " + t.getMessage());
    }
});
         */



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
                    //txtRecordLabel.setText("Recording Stopped...");

                    File audioFile = new File(mRcordFilePath);
                    RequestBody requestAudioFile = RequestBody.create(MediaType.parse("multipart/form-data"), audioFile);
                    Call<EnrollmentResponse> call = service.createEnrollment("identificationProfiles/"+speakerId+"/enroll", requestAudioFile);
                    call.enqueue(new Callback<EnrollmentResponse>() {
                        @Override
                        public void onResponse(Call<EnrollmentResponse> call, Response<EnrollmentResponse> response) {
                            if(response.isSuccessful()) {

                            }
                            else {

                                try {
                                    result[0] = response.errorBody().string();
                                    Gson gson = new Gson();
                                    ErrorCode errorCode = gson.fromJson(result[0], ErrorCode.class);
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                                txtRecordLabel.setText(result[0]);
                            }
                        }

                        @Override
                        public void onFailure(Call<EnrollmentResponse> call, Throwable t) {
                            Log.i("yunju-enrollment", t.toString());
                            Log.i("yunju-enrollment", t.getMessage());                        }
                    });
                }
                return false;
            }
        });
    }
}
