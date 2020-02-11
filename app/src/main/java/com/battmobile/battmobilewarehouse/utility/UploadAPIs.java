package com.battmobile.battmobilewarehouse.utility;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadAPIs {

    @Multipart
    @POST("products/api/upload-battery-image")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file, @Part("battery_image") RequestBody requestBody);

    @Multipart
    @POST("users/api/upload-profile-image")
    Call<ResponseBody> uploadProfileImage(@Part MultipartBody.Part file, @Part("profile_image") RequestBody requestBody);
}
