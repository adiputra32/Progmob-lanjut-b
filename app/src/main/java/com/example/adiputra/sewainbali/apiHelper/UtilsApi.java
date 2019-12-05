package com.example.adiputra.sewainbali.apiHelper;

public class UtilsApi {
    // 10.0.2.2 ini adalah localhost.
//    public static final String BASE_URL_API = "http://10.0.2.2:80/sewain2/";
//    public static final String BASE_URL_API = "http://192.168.43.6:80/sewain2/";
    public static final String BASE_URL_API = "http://sewainbali.000webhostapp.com/";

    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
