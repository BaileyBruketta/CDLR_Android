package com.ramstrenconsultingllc.cdlr;

import com.google.gson.annotations.SerializedName;

public class LoginResult {

    //private String name;

    //private String email;

    private String token;
    private String UserEmailAddressConfirmed;

    public String getToken(){
        return token;
    }

    public String getUserEmailAddressConfirmed()
    {
        return UserEmailAddressConfirmed;
    }

    //public String getName() {
    //    return name;
    //}

    //public String getEmail() {
    //    return email;
    //}
}
