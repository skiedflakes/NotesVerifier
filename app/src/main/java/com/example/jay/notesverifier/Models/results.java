package com.example.jay.notesverifier.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jay on 05/02/2018.
 */

public class results {

    @SerializedName("status")
    private String status;

    @SerializedName("doc_type")
    private String doc_type;

    @SerializedName("doc_number")
    private String doc_number;

    @SerializedName("amount")
    private String amount;

    @SerializedName("check_num")
    private String check_num;

    @SerializedName("check_date")
    private String check_date;

    public String getStatus() {
        return status;
    }

    public String getDoc_number() {
        return doc_number;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public String getAmount() {
        return amount;
    }

    public String getCheck_date() {
        return check_date;
    }
    public String getCheck_num() {
        return check_num;
    }
}
