package com.example.snackattack_2.Game_2.data;

import androidx.annotation.NonNull;

public class ScoreRecord {

      private String name;
    private final int score;
    private final double latitude;
    private final double longitude;

    public ScoreRecord(String name, int score,double latitude,double longitude){
        this.name = name;
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
    }

   public String getName() {return name;}
    public int getScore() {
        return score;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @NonNull
    public String toString(){
        if (name == null) name = "None";
        return "name: "+name+ "\nscore: " +score ;
    }

}
