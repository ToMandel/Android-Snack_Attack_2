package com.example.snackattack_2.Game_2.data;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.snackattack_2.Game_2.init.MyGPS;
import com.example.snackattack_2.Game_2.init.MySP;
import com.example.snackattack_2.Game_2.utilities.TypeItem;
import com.example.snackattack_2.Game_2.utilities.TypeVisibility;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

public class GameManager {
    private final int CHOCOLATE_VALUE = 10;
    private final int DEFAULT_VALUE = 1;
    private static GameManager gameManager = null;
    private int numOfLives;
    private int numOfRows, numOfCols;
    private int score = 0;
    private boolean send = true;
    private int DELAY=700;
    private final int fastGame = 400;
    private final int slowGame = 1000;

    private int currentColMonster;
    private final ArrayList<ArrayList<Item>> matrixItems = new ArrayList<>();
    private final ArrayList<Item> monsterItems = new ArrayList<>();
    private final ArrayList<Item> heartItems = new ArrayList<>();
    private boolean isGameOver = false;
    private boolean isSensorMode = false;
    private boolean isButtonMode = false;
    private boolean isBrocCollision = false;
    private boolean isChocolateCollision = false;
    private String playerName;

    private GameManager() {
    }

    public static void init() {
        if (gameManager == null) gameManager = new GameManager();
    }

    public static GameManager getInstance() {
        return gameManager;
    }

    public static void updateGameManager() {
        gameManager = new GameManager();
    }

    public boolean checkIfLastRow(int currentRowIndex, int lastRowIndex) {
        return currentRowIndex == lastRowIndex;
    }

    public boolean isVisible(Item item) {
        return item.getTypeVisibility() == TypeVisibility.VISIBLE;
    }

    public void initBroccoliesMatrix(ArrayList<ArrayList<AppCompatImageView>> game_IMG_broccolies, int game_IMG_chocolate) {
        numOfRows = game_IMG_broccolies.size();
        for (int row = 0; row < numOfRows; row++) {
            numOfCols = game_IMG_broccolies.get(row).size();
            matrixItems.add(new ArrayList<>());

            for (int col = 0; col < numOfCols; col++) {
                AppCompatImageView currentImage = game_IMG_broccolies.get(row).get(col);
                Item current = new Item()
                        .setImageBroccoli(currentImage)
                        .setImageChocolate(game_IMG_chocolate)
                        .setTypeVisibility(TypeVisibility.INVISIBLE);
                matrixItems.get(row).add(current);
            }
        }
    }

    public void initItems(ArrayList<AppCompatImageView> arrayList, String typeOfArray) {
        int numOfItems = arrayList.size();
        int randomIndex = getRandom(numOfItems);

        for (int i = 0; i < numOfItems; i++) {
            AppCompatImageView currentImage = arrayList.get(i);
            Item currentItem = new Item().setImageBroccoli(currentImage);

            switch (typeOfArray) {
                case "monster":
                    currentItem.setTypeItem(TypeItem.MONSTER);
                    currentItem.setTypeVisibility(i == randomIndex ? TypeVisibility.VISIBLE : TypeVisibility.INVISIBLE);
                    monsterItems.add(currentItem);
                    if (i == randomIndex) {
                        currentColMonster = randomIndex;
                    }
                    break;
                case "hearts":
                    currentItem.setTypeItem(TypeItem.HEART);
                    currentItem.setTypeVisibility(TypeVisibility.VISIBLE);
                    heartItems.add(currentItem);
                    break;
                default:
                    break;
            }
        }
    }

    public void move(String direction) {
        int nextColMonster;
        boolean moveAllowed = false;
        int currentColBroccoli = -1;
        for (int i = 0; i < numOfCols; i++) {
            if (isVisible(matrixItems.get(numOfRows - 1).get(i))) {
                currentColBroccoli = i;
                break;

            }
        }
        switch (direction) {
            case "left":
                nextColMonster = currentColMonster - 1;
                moveAllowed = (nextColMonster > -1);
                break;
            case "right":
                nextColMonster = currentColMonster + 1;
                moveAllowed = (nextColMonster < numOfCols);
                break;
            default:
                nextColMonster = currentColMonster;
                break;
        }
        if (moveAllowed) {

            updateMonster(nextColMonster);
            currentColMonster = nextColMonster;



        }
    }

    private void updateMonster(int indexVisibleMonster) {
        for (Item current : monsterItems) {
            current.setTypeVisibility(monsterItems.indexOf(current) == indexVisibleMonster ? TypeVisibility.VISIBLE : TypeVisibility.INVISIBLE);
        }
    }

    private void addScore(int value) {
        score += value;
    }

    private boolean checkHit(int currentColBroccoli) {
        return currentColMonster == currentColBroccoli;
    }

    private void reduceLives() {
        if (numOfLives>0)
            numOfLives -= 1;

        updateHeartsItems(numOfLives);
    }

    private boolean checkGameOver() {
        return numOfLives <= 0;

    }

    private void updateHeartsItems(int numOfLives) {
        for (int i = 0; i < numOfLives; i++) {
            heartItems.get(i).setTypeVisibility(TypeVisibility.VISIBLE);
        }

        for (int i = numOfLives; i < heartItems.size(); i++) {
            heartItems.get(i).setTypeVisibility(TypeVisibility.INVISIBLE);
        }
    }

    private void sendItems(boolean insertChocolate) {
        int randColBrocco = getRandom(numOfCols);
        int randColChoco = -1;
        if (insertChocolate) {
            do {
                randColChoco = getRandom(numOfCols);
            }
            while (randColBrocco == randColChoco);
        }
        for (int i = 0; i < numOfCols; i++) {
            Item curr = matrixItems.get(0).get(i);
            if (i == randColBrocco) {
                curr.setTypeItem(TypeItem.BROCCOLI);
                curr.setTypeVisibility(TypeVisibility.VISIBLE);
            } else if (i == randColChoco) {
                curr.setTypeItem(TypeItem.CHOCOLATE);
                curr.setTypeVisibility(TypeVisibility.VISIBLE);
            } else {
                curr.setTypeVisibility(TypeVisibility.INVISIBLE);
            }
        }
    }


    private void updateItemsTable() {
        for (int indexRow = numOfRows - 1; indexRow >= 0; indexRow--) {
            for (int indexColumn = 0; indexColumn < numOfCols; indexColumn++) {
                Item currentItem = matrixItems.get(indexRow).get(indexColumn);
                boolean isLastRow = checkIfLastRow(indexRow, numOfRows - 1);
                boolean isCurrentItemVisible = isVisible(currentItem);

                if (isLastRow && isCurrentItemVisible) {
                    currentItem.setTypeVisibility(TypeVisibility.INVISIBLE);
                    boolean isCollision = checkHit(indexColumn);
                    if (isCollision) {
                        checkTypeHit(currentItem);
                    }
                }

                if (!isLastRow) {
                    Item lowerItem = matrixItems.get(indexRow + 1).get(indexColumn);
                    moveItemDown(lowerItem, currentItem.getTypeVisibility(), currentItem.getTypeItem());
                    currentItem.setTypeItem(TypeItem.NONE);
                }
            }
        }

    }

    private void moveItemDown(Item item, TypeVisibility typeVisibility, TypeItem typeItem) {
        item.setTypeItem(typeItem);
        item.setTypeVisibility(typeVisibility);
    }

    private void checkTypeHit(Item item) {
        switch (item.getTypeItem()) {
            case CHOCOLATE:
                isChocolateCollision = true;
                addScore(CHOCOLATE_VALUE);
                break;
            case BROCCOLI:
                isBrocCollision = true;
                reduceLives();
                isGameOver = checkGameOver();
                break;
            default:
                break;
        }
    }

    public void updateTable(boolean insertPokemon) {
        updateItemsTable();
        addScore(DEFAULT_VALUE);
        sendItems(insertPokemon);
    }

    public ArrayList<ArrayList<Item>> getMatrixItems() {
        return matrixItems;
    }

    public ArrayList<Item> getMonsterItems() {
        return monsterItems;
    }

    public ArrayList<Item> getHeartItems() {
        return heartItems;
    }

    public String getSTRING_LOST_1_LIFE() {
        return "CRASH";
    }

    public String getSTRING_GAME_OVER() {
        return "Game Over";
    }

    public String getSTRING_PLUS_10_COINS() {
        return "+10 points";
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setBrocCollision(boolean brocCollision) {
        isBrocCollision = brocCollision;
    }

    public void setChocolateCollision(boolean chocolateCollision) {
        isChocolateCollision = chocolateCollision;
    }

    public boolean isSensorMode() {
        return isSensorMode;
    }

    public boolean isButtonMode() {
        return isButtonMode;
    }

    public void setSensorMode(boolean sensorMode) {
        isSensorMode = sensorMode;
    }

    public void setButtonMode(boolean buttonMode) {
        isButtonMode = buttonMode;
    }

    public GameManager setNumOfHearts(int numOfLives) {
        this.numOfLives = numOfLives;
        return this;
    }

    public boolean isBrocCollision() {
        return isBrocCollision;
    }

    public boolean isChocolateCollision() {
        return isChocolateCollision;
    }

    public int getScore() {
        return score;
    }

    public GameManager setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
        return this;
    }

    public GameManager setNumOfColumns(int numOfColumns) {
        this.numOfCols = numOfColumns;
        return this;
    }

    public int getDELAY() {
        return DELAY;
    }

    public void setDELAY(int DELAY) {
        this.DELAY = DELAY;
    }


    private int getRandom(int limit) {
        return new Random().nextInt(limit);
    }

    public void saveNewScoreRecord(String name) {
        ScoreRecord newRecord = new ScoreRecord(name, score, MyGPS.getInstance().getLatitude(), MyGPS.getInstance().getLongitude());

        String json = MySP.getInstance().getString("records", "");
        ListOfScoreRecords listOfScoreRecords = new Gson().fromJson(json, ListOfScoreRecords.class);
        if (listOfScoreRecords == null) {
            listOfScoreRecords = new ListOfScoreRecords();
        }
        listOfScoreRecords.getListOfScoreRecords().add(newRecord);
        MySP.getInstance().putString("records", new Gson().toJson(listOfScoreRecords));
        score =0 ;
    }

    public void gameOver() {
        numOfLives = 3;
        isGameOver=false;
        updateHeartsItems(numOfLives);
        for (int i = 0; i<numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                Item currentItem = matrixItems.get(i).get(j);
                currentItem.setTypeVisibility(TypeVisibility.INVISIBLE);
                currentItem.setTypeItem(TypeItem.NONE);
            }
        }
    }

    public void setfastMode(boolean b) {
        if (b) setDELAY(fastGame);
        else setDELAY(slowGame);
    }
}
