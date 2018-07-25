package com.example.yunju.speakerrecognition.network;

import com.example.yunju.speakerrecognition.EnrollmentResponse;
import com.example.yunju.speakerrecognition.IdProfileData;
import com.example.yunju.speakerrecognition.IdentificationProfile;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface NetworkService {

    // Create Profile
    @Headers({"Content-Type: application/json",
            "Ocp-Apim-Subscription-Key: your key"})
    @POST("identificationProfiles")
    Call<IdentificationProfile> createProfile(@Body IdProfileData data);

    // Create Enrollment
    @Multipart
    @Headers({"Content-Type:multipart/form-data",
            "Ocp-Apim-Subscription-Key: your key"})
    @POST
    Call<EnrollmentResponse> createEnrollment(@Url String url,
                                              @Part("file")RequestBody audioFile);

    /*
    // Create Enrollment
    @Headers({"Content-Type:multipart/form-data",
            "Ocp-Apim-Subscription-Key: d4382e77140d4883bf33365ecf2ceb66"})
    @POST("/identificationProfiles/{identificationProfileId}/enroll")
    Call<> createEnrollment(@Path(value = "identificationProfileId", encoded = true) String identificationProfileId);
    */




}
