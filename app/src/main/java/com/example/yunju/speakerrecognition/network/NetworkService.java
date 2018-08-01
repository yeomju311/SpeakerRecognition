package com.example.yunju.speakerrecognition.network;

import com.example.yunju.speakerrecognition.IdProfileData;
import com.example.yunju.speakerrecognition.IdentificationProfile;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {

    // Create Profile
    @Headers({"Content-Type: application/json",
            "Ocp-Apim-Subscription-Key: <key>"})
    @POST("identificationProfiles")
    Call<IdentificationProfile> createProfile(@Body IdProfileData data);

    /*
    // Create Enrollment
    @Multipart
    @Headers({"Content-Type:multipart/form-data",
            "Ocp-Apim-Subscription-Key: <key>"})
    @POST("identificationProfiles/{identificationProfileId}/enroll")
    Call<ResponseBody> createEnrollment(@Path("identificationProfileId") String profileId,
                                              @Part MultipartBody.Part audioFile);
    */

    @Headers({
            "Content-Type: multipart/form-data",
            "Ocp-Apim-Subscription-Key: <key>"
    })
    @POST("identificationProfiles/{identificationProfileId}/enroll")
    Call<ResponseBody> enroll(@Path("identificationProfileId") String id,
                              @Query("shortAudio") boolean shortAudio,
                              @Body RequestBody body);





}
