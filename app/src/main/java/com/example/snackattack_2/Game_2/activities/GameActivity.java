package com.example.snackattack_2.Game_2.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.snackattack_2.Game_2.data.GameManager;
import com.example.snackattack_2.Game_2.data.Item;
import com.example.snackattack_2.Game_2.init.MyGPS;
import com.example.snackattack_2.Game_2.init.MySignal;
import com.example.snackattack_2.Game_2.utilities.SensorDetector;
import com.example.snackattack_2.Game_2.utilities.TypeItem;
import com.example.snackattack_2.Game_2.utilities.TypeVisibility;
import com.example.snackattack_2.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private AppCompatImageView game_IMG_background;
    private ArrayList<AppCompatImageView> game_IMG_hearts;
    private ArrayList<ArrayList<AppCompatImageView>> game_IMG_broccolies;
    private ArrayList<AppCompatImageView> game_IMG_monster;
    private MaterialButton game_BTN_right;
    private MaterialButton game_BTN_left;
    private final int NUM_OF_HEARTS = 3;
    private Timer timerUpdateUI;
    private boolean isActiveController = true;
    private final int ROWS = 8, COLS = 5;
    private GameManager gameManager;
    private boolean pause = false;
    private SensorDetector sensorDetector;
    private TextView game_LBL_score;
    private TextView game_LBL_scoreNum;
    private int roundsToChocolate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findViews();
        init();
        initView();
        updateUI();
    }

    private void doMove(String direction) {
        if (isActiveController) {
            gameManager.move(direction);
            rendermonster();
        }
    }

    public void updateUI() {
        renderScore();
        renderBroccoliesTable();
        gameManager.setBrocCollision(false);
        gameManager.setChocolateCollision(false);
    }

    private void makeSound(String type) {
        int musicId = 0;
        switch (type) {
            case "clash":
                musicId = R.raw.eww;
                break;
            case "success":
                musicId = R.raw.burp;
                break;
        }
        if (musicId != 0) {
            MediaPlayer player = MediaPlayer.create(GameActivity.this, musicId);
            player.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
        pause = true;
        if (gameManager.isSensorMode()) sensorDetector.stop();
    }

    protected void onStop() {
        super.onStop();
        MyGPS.getInstance().stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pause = false;
        startTimer();
        if (gameManager.isSensorMode()) {
            sensorDetector.start();
        }
        MyGPS.getInstance().start();
    }

    private void startTimer() {
        timerUpdateUI = new Timer();
        TimerTask callback_updateBroccolies = new TimerTask() {
            @Override
            public void run() {
                boolean insertChocolate;
                if (roundsToChocolate == 0) {
                    insertChocolate = true;
                    roundsToChocolate = 3;
                } else {
                    insertChocolate = false;
                    roundsToChocolate--;
                }
                runOnUiThread(() -> updateUIonTime(insertChocolate));
            }
        };
        timerUpdateUI.scheduleAtFixedRate(callback_updateBroccolies, gameManager.getDELAY(), gameManager.getDELAY());
    }

    private void initButtons() {
        game_BTN_left.setOnClickListener(v -> doMove("left"));
        game_BTN_right.setOnClickListener(v -> doMove("right"));
    }

    public void updateUIonTime(boolean insertChocolate) {
        gameManager.updateTable(insertChocolate);
        if (gameManager.isBrocCollision()) {
            renderCollision("clash", gameManager.getSTRING_LOST_1_LIFE(), true);
        } else if (gameManager.isChocolateCollision()) {
            renderCollision("success", gameManager.getSTRING_PLUS_10_COINS(), false);
        }
        updateUI();
        if (gameManager.isGameOver()) {
            updateUIGameOver();
        }
    }

    private void updateUIGameOver() {
        renderGameOver();
        isActiveController = false;
        stopTimer();
    }

    public void rendermonster() {
        ArrayList<Item> monsterItems = gameManager.getMonsterItems();
        for (int i = 0; i < COLS; i++) {
            AppCompatImageView game_IMG_monster = this.game_IMG_monster.get(i);
            TypeVisibility typeVisibility = monsterItems.get(i).getTypeVisibility();
            game_IMG_monster.setVisibility(typeVisibility == TypeVisibility.VISIBLE ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void renderHearts() {
        ArrayList<Item> heartItems = gameManager.getHeartItems();
        for (int i = 0; i < NUM_OF_HEARTS; i++) {
            AppCompatImageView game_IMG_heart = game_IMG_hearts.get(i);
            TypeVisibility typeVisibility = heartItems.get(i).getTypeVisibility();
            game_IMG_heart.setVisibility(typeVisibility == TypeVisibility.VISIBLE ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void renderCollision(String musicType, String toastMessage, boolean doVibrate) {
        renderHearts();
        renderToast(toastMessage);
        if (doVibrate) vibrate();
        makeSound(musicType);
        if (gameManager.isGameOver()) {
            updateUIGameOver();
        }
    }

    private void renderScore() {
        game_LBL_scoreNum.setText(Integer.toString((gameManager.getScore())));
    }

    private void initControls() {
        if (gameManager.isButtonMode()) initButtons();
        else initSensor();
    }

    private void initSensor() {
        sensorDetector = new SensorDetector(this, callback_movement);
    }

    private final SensorDetector.CallBack_Movement callback_movement = new SensorDetector.CallBack_Movement() {
        @Override
        public void moveRight() {
            doMove("right");
        }

        @Override
        public void moveLeft() {
            doMove("left");
        }
    };

    private void initGameManager() {
        gameManager = GameManager.getInstance()
                .setNumOfHearts(NUM_OF_HEARTS)
                .setNumOfRows(ROWS)
                .setNumOfColumns(COLS);
        gameManager.initItems(game_IMG_hearts, "hearts");
        gameManager.initItems(game_IMG_monster, "monster");
        int imagePokemon = R.drawable.ic_chocolate;
        gameManager.initBroccoliesMatrix(game_IMG_broccolies, imagePokemon);
    }

    private void init() {
        initGameManager();
        initControls();
    }

    private void renderGameOver() {
        renderHearts();
        renderScore();
        renderBroccoliesTable();
        gameOver();
    }

    private void renderBroccoliesTable() {
        ArrayList<ArrayList<Item>> matrixBrocItems = gameManager.getMatrixItems();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Item broccoli = matrixBrocItems.get(row).get(col);
                TypeVisibility typeVisibility = broccoli.getTypeVisibility();
                AppCompatImageView imgBroccoli = game_IMG_broccolies.get(row).get(col);
                if (typeVisibility == TypeVisibility.VISIBLE) {
                    int imageResource;
                    if (broccoli.getTypeItem() == TypeItem.BROCCOLI)
                        imageResource = R.drawable.ic_broccoli;
                    else imageResource = R.drawable.ic_chocolate;
                    imgBroccoli.setImageResource(imageResource);
                    imgBroccoli.setVisibility(View.VISIBLE);
                } else {
                    imgBroccoli.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private void initView() {
        renderHearts();
        renderBroccoliesTable();
        rendermonster();
    }

    private void stopTimer() {
        timerUpdateUI.cancel();
    }

    private void findButtons() {
        game_BTN_left = findViewById(R.id.game_BTN_left);
        game_BTN_right = findViewById(R.id.game_BTN_right);
        game_BTN_left.setVisibility(View.VISIBLE);
        game_BTN_right.setVisibility(View.VISIBLE);
    }

    private void findBackground() {
        game_IMG_background = findViewById(R.id.game_IMG_background);
    }

    private void findControl() {
        if (GameManager.getInstance().isButtonMode()) {
            findButtons();
        }
    }

    private void findMonster() {
        game_IMG_monster = new ArrayList<>(COLS);
        game_IMG_monster.add(findViewById(R.id.game_IMG_monster1));
        game_IMG_monster.add(findViewById(R.id.game_IMG_monster2));
        game_IMG_monster.add(findViewById(R.id.game_IMG_monster3));
        game_IMG_monster.add(findViewById(R.id.game_IMG_monster4));
        game_IMG_monster.add(findViewById(R.id.game_IMG_monster5));
    }

    private void findBroccolies() {
        game_IMG_broccolies = new ArrayList<>(ROWS);
        for (int i = 0; i < ROWS; i++) {
            game_IMG_broccolies.add(new ArrayList<>(COLS));
            for (int j = 0; j < COLS; j++) {
                int currentPlace = i * COLS + j + 1;
                int broccoID = getResources().getIdentifier("game_IMG_broccoli" + currentPlace, "id", getPackageName());
                AppCompatImageView currentBrocco = findViewById(broccoID);
                game_IMG_broccolies.get(i).add(currentBrocco);
            }
        }
    }

    private void findScore() {
        game_LBL_score = findViewById(R.id.game_LBL_score);
        game_LBL_scoreNum = findViewById(R.id.game_LBL_score_num);
    }

    private void findHearts() {
        game_IMG_hearts = new ArrayList<>(NUM_OF_HEARTS);
        for (int i = 1; i <= NUM_OF_HEARTS; i++) {
            int heartID = getResources().getIdentifier("game_IMG_heart" + i, "id", getPackageName());
            AppCompatImageView currentHeart = findViewById(heartID);
            game_IMG_hearts.add(currentHeart);
        }
    }

    private void findViews() {
        findHearts();
        findScore();
        findBackground();
        findControl();
        findMonster();
        findBroccolies();
    }

    private void gameOver() {
        pause = true;
        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        startActivity(intent);
        gameManager.gameOver();
        finish();
    }

    private void vibrate() {
        MySignal.getInstance().vibrate();
    }

    public void renderToast(String msg) {
        MySignal.getInstance().toast(msg);
    }
}
