package com.example.adiputra.sewainbali.apiHelper;

import android.app.Notification;
import android.graphics.Bitmap;

import com.example.adiputra.sewainbali.RequestNotification;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
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
                                              @Query("tambah") int tambah,
                                              @Query("email") String email);

    @GET("lihatHistorySemua.php")
    Call<ResponseBody> historyLobbyRequest(@Query("email") String email);

    @GET("getMotor.php")
    Call<ResponseBody> motorRequest(@Query("jumlah") int jumlah,
                                    @Query("tambah") int tambah,
                                    @Query("kategori") String kategori,
                                    @Query("email") String email);

    @GET("getMotorSingle.php")
    Call<ResponseBody> motorDetailRequest(@Query("id_motor") String id_motor);

    @GET("lihatHistory.php")
    Call<ResponseBody> historyDetailRequest(@Query("id_sewa") String id_sewa);

    @FormUrlEncoded
    @POST("tambahSewa.php")
    Call<ResponseBody> tambahSewaRequest(@Field("email") String email,
                                             @Field("id_motor") String id_motor,
                                             @Field("location") String location,
                                             @Field("days") String days,
                                             @Field("payment") String payment,
                                             @Field("tanggal") String tanggal);

    @Multipart
    @POST("updatePembayaran.php")
    Call<ResponseBody> uploadPPRequest(@Part("id_sewa") String id_sewa,
                                  @Part MultipartBody.Part ppimage,
                                  @Part("ppimage") RequestBody ppimagename);

    @FormUrlEncoded
    @POST("batalSewa.php")
    Call<ResponseBody> cancelOrderRequest(@Field("id_sewa") String id_sewa);

    @GET("getChat.php")
    Call<ResponseBody> chatRequest(@Query("email") String email);

    @Headers({"Authorization: key=f5kSYJPyDPI:APA91bEJWljgJTg4t9_0vqyaJqFk71hUVuOZbOAS0jY1vW75lzFDyJ16c4gAn2B383HJcxR-VqcU-pX0yDI_qGgzwrQrLKSTUlIRonM4BGFniWruNENKw3uBGZUJUYTGDwwnwAtbXJti",
            "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendChatNotification(@Body RequestNotification requestNotificaton);

}
