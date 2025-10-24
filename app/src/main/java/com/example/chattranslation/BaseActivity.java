package com.example.chattranslation;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyUserTheme(); // Load theme before UI
        super.onCreate(savedInstanceState);
    }

    private void applyUserTheme() {
        SharedPreferences prefs = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        String theme = prefs.getString("theme", "default");

        switch (theme) {
            case "red":
                setTheme(R.style.Theme_ChatTranslation_Red);
                break;
            case "orange":
                setTheme(R.style.Theme_ChatTranslation_Orange);
                break;
            case "yellow":
                setTheme(R.style.Theme_ChatTranslation_Yellow);
                break;
            case "green":
                setTheme(R.style.Theme_ChatTranslation_Green);
                break;
            case "blue":
                setTheme(R.style.Theme_ChatTranslation_Blue);
                break;
            case "purple":
                setTheme(R.style.Theme_ChatTranslation_Purple);
                break;
            default:
                setTheme(R.style.Base_Theme_ChatTranslation);
                break;
        }
    }
}
