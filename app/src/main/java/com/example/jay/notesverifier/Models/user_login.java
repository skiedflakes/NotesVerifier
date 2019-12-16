package com.example.jay.notesverifier.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jay on 03/02/2018.
 */

public class user_login {

    @SerializedName("response_login")
    private String response_login;

    @SerializedName("company_id")
    private String company_id;

    @SerializedName("status")
    private String status;

    @SerializedName("company_code")
    private String company_code;

    @SerializedName("company_name")
    private String company_name;

    public String getCompany_name() {
        return company_name;
    }

    public String getStatus() {
        return status;
    }

    public String getCompany_id() {
        return company_id;
    }

    public String getCompany_code() {
        return company_code;
    }
}
