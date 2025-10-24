package com.example.chattranslation;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chattranslation.model.FeedbackModel;
import com.example.chattranslation.utils.FirebaseUtil;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;

import java.util.*;

public class TranslateFragment extends Fragment {
    private Spinner languageSpinner;
    private Spinner themeSpinner;
    private boolean isSpinnerInitialized = false;

    private EditText feedbackText;
    private Button feedbackButton;
    FirebaseFirestore db;
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
        themeSpinner = view.findViewById(R.id.theme_spinner);

        setupLanguageMap();
        setupSpinner();
        setupThemes();
        setupFeedbackSystem();
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

        languageSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
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

    private void setupThemes() {
        List<String> themeNames = new ArrayList<>();
        themeNames.add("Red");
        themeNames.add("Orange");
        themeNames.add("Yellow");
        themeNames.add("Green");
        themeNames.add("Blue");
        themeNames.add("Purple");

        Map<Integer, String> positionToTheme = new HashMap<>();
        positionToTheme.put(0, "red");
        positionToTheme.put(1, "orange");
        positionToTheme.put(2, "yellow");
        positionToTheme.put(3, "green");
        positionToTheme.put(4, "blue");
        positionToTheme.put(5, "purple");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                themeNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(adapter);

        SharedPreferences prefs = requireContext().getSharedPreferences("theme_prefs", getContext().MODE_PRIVATE);
        String currentTheme = prefs.getString("theme", "red"); // default to red
        int position = 0; // default
        switch (currentTheme) {
            case "red": position = 0; break;
            case "orange": position = 1; break;
            case "yellow": position = 2; break;
            case "green": position = 3; break;
            case "blue": position = 4; break;
            case "purple": position = 5; break;
        }
        themeSpinner.setSelection(position);

        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (!isSpinnerInitialized) {
                    isSpinnerInitialized = true; // ignore first automatic call
                    return;
                }

                String selectedTheme = positionToTheme.getOrDefault(pos, "red");
                String savedTheme = prefs.getString("theme", "red");

                if (!selectedTheme.equals(savedTheme)) {
                    prefs.edit().putString("theme", selectedTheme).apply();

                    // safely recreate
                    requireActivity().getWindow().getDecorView().post(() -> {
                        if (isAdded()) requireActivity().recreate();
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    private void setupFeedbackSystem(){
        db = FirebaseFirestore.getInstance();
        feedbackText = getView().findViewById(R.id.feedback_text);
        feedbackButton = getView().findViewById(R.id.feedback_submit);

        feedbackButton.setOnClickListener(v -> {
            String feedback = feedbackText.getText().toString().trim();

            if (feedback.isEmpty()) {
                Toast.makeText(getContext(), "Please enter feedback", Toast.LENGTH_SHORT).show();
            } else {
                FeedbackModel f = new FeedbackModel(Timestamp.now(), FirebaseUtil.currentUserId(), feedback);
                db.collection("feedback")
                        .add(f)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(getContext(), "Feedback submitted!", Toast.LENGTH_SHORT).show();
                            feedbackText.setText("");
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}