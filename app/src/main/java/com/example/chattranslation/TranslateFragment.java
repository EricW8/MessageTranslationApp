package com.example.chattranslation;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.*;

public class TranslateFragment extends Fragment {
    private Spinner languageSpinner;
    private String selectedLanguageCode = "en";
    private final Map<String, String> languageMap = new TreeMap<>();

    public TranslateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_translate, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        languageSpinner = view.findViewById(R.id.language_spinner);

        setupLanguageMap();
        setupSpinner();
    }

    private void setupLanguageMap() {
        languageMap.put("Afrikaans", "af");
        languageMap.put("Arabic", "ar");
        languageMap.put("Belarusian", "be");
        languageMap.put("Bengali", "bn");
        languageMap.put("Bosnian", "bs");
        languageMap.put("Bulgarian", "bg");
        languageMap.put("Catalan", "ca");
        languageMap.put("Chinese (Simplified)", "zh");
        languageMap.put("Chinese (Traditional)", "zh-Hant");
        languageMap.put("Croatian", "hr");
        languageMap.put("Czech", "cs");
        languageMap.put("Danish", "da");
        languageMap.put("Dutch", "nl");
        languageMap.put("English", "en");
        languageMap.put("Esperanto", "eo");
        languageMap.put("Estonian", "et");
        languageMap.put("Finnish", "fi");
        languageMap.put("French", "fr");
        languageMap.put("Galician", "gl");
        languageMap.put("Georgian", "ka");
        languageMap.put("German", "de");
        languageMap.put("Greek", "el");
        languageMap.put("Gujarati", "gu");
        languageMap.put("Haitian Creole", "ht");
        languageMap.put("Hebrew", "he");
        languageMap.put("Hindi", "hi");
        languageMap.put("Hungarian", "hu");
        languageMap.put("Icelandic", "is");
        languageMap.put("Indonesian", "id");
        languageMap.put("Irish", "ga");
        languageMap.put("Italian", "it");
        languageMap.put("Japanese", "ja");
        languageMap.put("Kannada", "kn");
        languageMap.put("Kazakh", "kk");
        languageMap.put("Korean", "ko");
        languageMap.put("Kyrgyz", "ky");
        languageMap.put("Latvian", "lv");
        languageMap.put("Lithuanian", "lt");
        languageMap.put("Macedonian", "mk");
        languageMap.put("Malay", "ms");
        languageMap.put("Marathi", "mr");
        languageMap.put("Mongolian", "mn");
        languageMap.put("Nepali", "ne");
        languageMap.put("Norwegian", "no");
        languageMap.put("Persian", "fa");
        languageMap.put("Polish", "pl");
        languageMap.put("Portuguese", "pt");
        languageMap.put("Punjabi", "pa");
        languageMap.put("Romanian", "ro");
        languageMap.put("Russian", "ru");
        languageMap.put("Serbian", "sr");
        languageMap.put("Slovak", "sk");
        languageMap.put("Slovenian", "sl");
        languageMap.put("Spanish", "es");
        languageMap.put("Swahili", "sw");
        languageMap.put("Swedish", "sv");
        languageMap.put("Tagalog", "tl");
        languageMap.put("Tamil", "ta");
        languageMap.put("Telugu", "te");
        languageMap.put("Thai", "th");
        languageMap.put("Turkish", "tr");
        languageMap.put("Ukrainian", "uk");
        languageMap.put("Urdu", "ur");
        languageMap.put("Uzbek", "uz");
        languageMap.put("Vietnamese", "vi");
        languageMap.put("Welsh", "cy");
    }

    private void setupSpinner() {
        List<String> languageNames = new ArrayList<>(languageMap.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                languageNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        languageSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguageName = languageNames.get(position);
                selectedLanguageCode = languageMap.get(selectedLanguageName);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("selected_language_code", selectedLanguageCode);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}