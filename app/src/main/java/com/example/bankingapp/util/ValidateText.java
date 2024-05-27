package com.example.bankingapp.util;

public class ValidateText {

    public static String trimZero(String text) {
        if (text == null) {
            return null;
        }
        char firstChar = text.charAt(0);
        if (firstChar == '0')
            return trimZero(text.replaceFirst("0", ""));
        else
            return text;
    }
}