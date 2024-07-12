package com.example.snackattack_2.Game_2.data;

import java.util.ArrayList;

public class ListOfScoreRecords {
    private final ArrayList<ScoreRecord> listOfScoreRecords;

    public ListOfScoreRecords(){
        listOfScoreRecords = new ArrayList<>();
    }

    public ArrayList<ScoreRecord> getListOfScoreRecords(){
        return listOfScoreRecords;
    }

    public int size() {
        return listOfScoreRecords.size();
    }

    public void add(ScoreRecord result){
        listOfScoreRecords.add(result);
    }

    public ScoreRecord get(int index) {
        return listOfScoreRecords.get(index);
    }
}
