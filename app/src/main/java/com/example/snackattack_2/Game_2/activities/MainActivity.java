package com.example.snackattack_2.Game_2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.snackattack_2.Game_2.data.GameManager;
import com.example.snackattack_2.Game_2.init.MyApp;
import com.example.snackattack_2.Game_2.init.MyGPS;
import com.example.snackattack_2.Game_2.init.MySP;
import com.example.snackattack_2.Game_2.init.MySignal;
import com.example.snackattack_2.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    MaterialButton main_BTN_start_game, main_BTN_high_scores;
    AppCompatImageView main_IMG_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
        initGPS();
    }

    private void initGPS() { MyGPS.getInstance().checkPermissions(this); }

    private void findViews() {
        main_IMG_back = findViewById(R.id.main_IMG_back);
        main_BTN_high_scores = findViewById(R.id.main_BTN_high_scores);
        main_BTN_start_game = findViewById(R.id.main_BTN_start_game);
    }

    private void initViews() {
        main_BTN_high_scores.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
            startActivity(intent);
        });
        main_BTN_start_game.setOnClickListener(view -> {
            Intent intent2 = new Intent(MainActivity.this, ModeActivity.class);
            startActivity(intent2);
        });
    }
}
