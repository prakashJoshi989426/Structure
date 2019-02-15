package com.pra.practical.helper;

import android.util.Patterns;
import android.widget.EditText;

/**
 * this class is used for Validation
 */
public class Validation {

    public static boolean isRequiredField(String strText) {// check for require-filed
        return strText != null && !strText.trim().isEmpty();
    }

    public static boolean isEmail(String strEmailAddress) { // check for entered value is email or not
        return Patterns.EMAIL_ADDRESS.matcher(strEmailAddress).matches();
    }

}
