package com.example.jay.notesverifier.API;

/**
 * Created by jay on 03/02/2018.
 */

import com.example.jay.notesverifier.Models.results;
import com.example.jay.notesverifier.Models.user_login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RegisterAPI {

    @FormUrlEncoded
    @POST("login_verifier2.php")
    Call<user_login>login(

                @Field("username")String username,
                @Field("password")String password
    );

    @FormUrlEncoded
    @POST("check_qr_to_db.php")
    Call<results>check_results(
            @Field("company_id")String company_id,
            @Field("qr_code")String qr_code,
            @Field("company_code")String company_code
    );
}
