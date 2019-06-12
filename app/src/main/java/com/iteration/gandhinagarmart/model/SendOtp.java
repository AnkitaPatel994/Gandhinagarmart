package com.iteration.gandhinagarmart.model;

import com.google.gson.annotations.SerializedName;

public class SendOtp {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("otp")
    private String otp;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
