package com.example.adiputra.sewainbali.apiHelper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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
    @FormUrlEncoded
    @POST("update.php")
    Call<ResponseBody> uptRequest(@Field("nama") String nama,
                                     @Field("email") String email,
                                     @Field("birthdate") String birthdate,
                                     @Field("phone") String phone,
                                     @Field("address") String address);

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
}
