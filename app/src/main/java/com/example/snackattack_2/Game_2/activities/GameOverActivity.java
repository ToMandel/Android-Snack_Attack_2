package com.example.snackattack_2.Game_2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.snackattack_2.R;
import com.example.snackattack_2.Game_2.data.GameManager;
import com.google.android.material.button.MaterialButton;

public class GameOverActivity extends AppCompatActivity {
    private AppCompatImageView gameOver_IMG_background;
    private MaterialButton gameOver_BTN_enter;
    private EditText gameover_EDT_name;
    private GameManager gameManager;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        GameManager.init();
        findViews();
        initViews();
    }

    private void findViews() {
        gameOver_IMG_background = findViewById(R.id.gameOver_IMG_background);
        gameover_EDT_name = findViewById(R.id.gameover_EDT_name);
        gameOver_BTN_enter = findViewById(R.id.gameover_BTN_enter);
    }

    private void initViews() {
        gameOver_BTN_enter.setOnClickListener(view -> {
            name = gameover_EDT_name.getText().toString();
            if (!name.equals("")) {
                Intent intent = new Intent(GameOverActivity.this, ScoreActivity.class);
                startActivity(intent);
                saveScoreRecord();
            }
        });
    }

    public void saveScoreRecord() {
        GameManager.getInstance().saveNewScoreRecord(name);
    }
}
