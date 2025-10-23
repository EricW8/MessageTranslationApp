package com.example.chattranslation;


import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Task;

import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
public class MyTranslator {


    public Task<String> translateMessage(String originalMessage, String language) {
        LanguageIdentifier languageIdentifier = LanguageIdentification.getClient();

        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();

        languageIdentifier.identifyLanguage(originalMessage)
                .addOnSuccessListener(languageCode -> {
                    if (languageCode.equals("und")) {
                        taskCompletionSource.setException(new Exception("Could not identify language"));
                        return;
                    }

                    String sourceLang = TranslateLanguage.fromLanguageTag(languageCode);
                    String targetLang = TranslateLanguage.fromLanguageTag(language);

                    if (sourceLang == null){
                        sourceLang = TranslateLanguage.ENGLISH;
                    }
                    if (targetLang == null){
                        targetLang = TranslateLanguage.ENGLISH;
                    }
                    TranslatorOptions options = new TranslatorOptions.Builder()
                            .setSourceLanguage(sourceLang)
                            .setTargetLanguage(targetLang)
                            .build();

                    com.google.mlkit.nl.translate.Translator translator = Translation.getClient(options);

                    translator.downloadModelIfNeeded()
                            .addOnSuccessListener(unused -> {
                                translator.translate(originalMessage)
                                        .addOnSuccessListener(taskCompletionSource::setResult)
                                        .addOnFailureListener(taskCompletionSource::setException);
                            })
                            .addOnFailureListener(taskCompletionSource::setException);
                })
                .addOnFailureListener(taskCompletionSource::setException);

        return taskCompletionSource.getTask();
    }
}

