package com.example.yunju.speakerrecognition.network;

import com.example.yunju.speakerrecognition.IdProfileData;
import com.example.yunju.speakerrecognition.IdentificationProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NetworkService {

    // Create Profile
    @Headers("Ocp-Apim-Subscription-Key: your key")
    @POST("/identificationProfiles")
    Call<IdentificationProfile> createProfile(@Body IdProfileData data);

    /*
    // Create Enrollment
    @POST("/identificationProfiles/{identificationProfileId}/enroll[?shortAudio]")
    Call<EnrollResult> createEnrollment(@Body IdProfileData data);
    */




}
