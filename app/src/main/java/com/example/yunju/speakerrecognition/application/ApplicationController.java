package com.example.yunju.speakerrecognition.application;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.example.yunju.speakerrecognition.network.NetworkService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationController extends Application{

    private static ApplicationController instance;
    private static String baseUrl = "https://westus.api.cognitive.microsoft.com/spid/v1.0/";
    private NetworkService networkService;

    public static ApplicationController getInstance() {
        return instance;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        ApplicationController.instance = this;

        // 어플리케이션 초기 실행 시, retrofit을 사전에 build한다.
        buildService();
    }

    public void buildService() {
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        networkService = retrofit.create(NetworkService.class);

    }
}
