package com.example.zct;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.Translation;
import com.ibm.watson.language_translator.v3.model.TranslationResult;

import java.util.List;

public class Translator {
//Initialize watson translator
    Helper helper = new Helper();
    public String translate(String word, String targetLanguage){
        targetLanguage = helper.iso639Handler(targetLanguage);
        if (targetLanguage.equals("iw")){
            targetLanguage = "he";
        }
        IamAuthenticator authenticator = new IamAuthenticator("IKhWJQ2QYOap2GIH5MZijz2udr5B43NO5_rGbXy-PhUG");
        LanguageTranslator languageTranslator = new LanguageTranslator("2018-05-01", authenticator);
        languageTranslator.setServiceUrl("https://api.eu-de.language-translator.watson.cloud.ibm.com/instances/cb36a353-9d1d-40bf-a4a3-7ef300ecedb2");
        TranslateOptions options = new TranslateOptions.Builder().addText(word).modelId("en-"+targetLanguage).build();
        TranslationResult result = languageTranslator.translate(options).execute().getResult();
        List<Translation> translations = result.getTranslations();
        return translations.get(0).getTranslation();
    }

}
