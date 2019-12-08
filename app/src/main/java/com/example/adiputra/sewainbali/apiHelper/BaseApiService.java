package com.example.adiputra.sewainbali.apiHelper;

import android.graphics.Bitmap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface BaseApiService {
    // Fungsi ini untuk memanggil API http://10.0.2.2/mahasiswa/login.php
    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> loginRequest(@Field("email") String email,
                                    @Field("password") String password);

    // Fungsi ini untuk memanggil API http://10.0.2.2/mahasiswa/register.php
    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> registerRequest(@Field("nama") String nama,
                                       @Field("email") String email,
                                       @Field("phone") String phone,
                                       @Field("password") String password);

    // Fungsi ini untuk memanggil API http://10.0.2.2/mahasiswa/edit.php
    @FormUrlEncoded
    @POST("edit.php")
    Call<ResponseBody> editRequest(@Field("email") String email);

    // Fungsi ini untuk memanggil API http://10.0.2.2/mahasiswa/update.php
    @Multipart
    @POST("update.php")
    Call<ResponseBody> uptRequest(@Part("nama") String nama,
                                  @Part("email") String email,
                                  @Part("birthdate") String birthdate,
                                  @Part("phone") String phone,
                                  @Part("address") String address,
                                  @Part MultipartBody.Part userimage,
                                  @Part("userimage") RequestBody userimagename,
                                  @Part MultipartBody.Part idcardimage,
                                  @Part("idcardimage") RequestBody idcardimagename);

    // Fungsi ini untuk memanggil API http://10.0.2.2/mahasiswa/getStatus.php
    @FormUrlEncoded
    @POST("getStatus.php")
    Call<ResponseBody> statusRequest(@Field("email") String email);

    // Fungsi ini untuk memanggil API http://10.0.2.2/mahasiswa/changePassword.php
    @FormUrlEncoded
    @POST("changePassword.php")
    Call<ResponseBody> changePasswordRequest(@Field("email") String email,
                                    @Field("oldPassword") String oldPassword,
                                    @Field("newPassword") String newPassword);

    @GET("getMotorMostViewed.php")
    Call<ResponseBody> motorMostViewedRequest(@Query("jumlah") int jumlah,
                                              @Query("tambah") int tambah);
}
