package com.humworld.codeathon;

import android.util.Patterns;

/**
 * Created by ${Sys-7} on 14/10/16.
 * Company Name Humworld
 */

public class ValidationClass {

    public static boolean isValidString(String string){

        return (string!=null && string.trim().length()>0);
    }

    public static boolean isValidMail(String mailId){

        return (mailId != null && Patterns.EMAIL_ADDRESS.matcher(mailId).matches());
    }

    public static boolean isValidMobile(String mobile){

        return (mobile != null && Patterns.PHONE.matcher(mobile).matches());
    }
}
