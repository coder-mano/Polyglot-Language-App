package com.example.zct;

public class Helper {
    String newVal = "";

    public String iso639Handler(String code){
        switch (code) {
            case "cz":
                newVal = "cs";
                break;
            case "be":
                newVal = "nl";
                break;
            case "se":
                newVal = "sv";
                break;
            case "kr":
                newVal = "ko";
                break;
            case "cn":
                newVal = "zh";
                break;
            case "gr":
                newVal = "el";
            case "dk":
                newVal = "da";
                break;
            case "iq":
                newVal = "ar";
                break;
            case "jp":
                newVal = "ja";
                break;
            case "br":
                newVal = "pt";
                break;
            case "vn":
                newVal = "vi";
                break;
            case "ie":
                newVal = "en";
                break;
            case "il":
                newVal = "he";
                break;
            default:
                newVal = code;
                break;
        }
        return newVal;
    }
}
