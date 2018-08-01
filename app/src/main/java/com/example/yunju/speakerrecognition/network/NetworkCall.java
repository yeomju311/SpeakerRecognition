package com.example.yunju.speakerrecognition.network;

import android.os.AsyncTask;
import android.util.Log;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class NetworkCall extends AsyncTask <Call<ResponseBody>, Void, Response<ResponseBody>> {
    @Override
    protected Response<ResponseBody> doInBackground(Call<ResponseBody>... params) {
        try {
            Response<ResponseBody> response = params[0].execute();
            Log.e("CHECK-CODE", String.valueOf(response.code()));
            return response;
        } catch (Exception e) {
            Log.e("TEST", "Failed network call: " + e.toString());
        }
        return null;
    }
}
