package com.example.zct;

public class Helper {
    public String iso639Handler(String code){
        switch (code) {
            case "cz":
                return "cs";
            case "be":
                return "nl";
            case "se":
                return "sv";
            case "kr":
                return "ko";
            case "cn":
                return "zh";
            case "gr":
                return "el";
            case "dk":
                return "da";
            case "iq":
                return "ar";
            case "jp":
                return "ja";
            case "br":
                return "pt";
            case "vn":
                return "vi";
            case "ie":
                return "en";
            case "il":
                return "iw";
            case "id":
                return "in";
            default:
                return code;
        }
    }
}
