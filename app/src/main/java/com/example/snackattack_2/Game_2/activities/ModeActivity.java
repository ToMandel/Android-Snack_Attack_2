package com.example.snackattack_2.Game_2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.snackattack_2.R;
import com.example.snackattack_2.Game_2.data.GameManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;

public class ModeActivity extends AppCompatActivity {
    private AppCompatImageView game_IMG_background;
    private MaterialButton mode_BTN_buttons;
    private MaterialButton mode_BTN_sensor;
    private MaterialSwitch mode_SWT_fast;
    boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        GameManager.init();
        findViews();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        mode_SWT_fast.setOnCheckedChangeListener((buttonView, isChecked) -> checked = isChecked);

        View.OnClickListener listener = v -> {
            Intent intent;
            int id = v.getId();
            if (id == R.id.mode_BTN_sensor) {
                GameManager.getInstance().setSensorMode(true);
                GameManager.getInstance().setButtonMode(false);
                intent = new Intent(ModeActivity.this, GameActivity.class);
            } else if (id == R.id.mode_BTN_buttons) {
                GameManager.getInstance().setfastMode(checked);
                GameManager.getInstance().setSensorMode(false);
                GameManager.getInstance().setButtonMode(true);
                intent = new Intent(ModeActivity.this, GameActivity.class);
            } else {
                throw new IllegalArgumentException("Invalid view id");
            }
            finish();
            startActivity(intent);
        };

        mode_BTN_sensor.setOnClickListener(listener);
        mode_BTN_buttons.setOnClickListener(listener);
    }

    private void findViews() {
        mode_BTN_buttons = findViewById(R.id.mode_BTN_buttons);
        mode_BTN_sensor = findViewById(R.id.mode_BTN_sensor);
        game_IMG_background = findViewById(R.id.mode_IMG_background);
        mode_SWT_fast = findViewById(R.id.mode_SWT_fast);
    }
}