package com.ramstrenconsultingllc.cdlr;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface RetrofitInterface {

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<Void> executeSignup (@Body HashMap<String, String> map);

    @GET("/getshowcards")
    Call<ShowCardGet> executeGetshowcards ();

    @POST("/getcardbyoffernumber")
    Call<ShowCardGet> executeGetCardByOfferNumber (@Body HashMap<String, String> map);

    @POST("/offerservices")
    Call<PostServicesResult> executePostOfferOfServices (@Body HashMap<String, String> map);

}
