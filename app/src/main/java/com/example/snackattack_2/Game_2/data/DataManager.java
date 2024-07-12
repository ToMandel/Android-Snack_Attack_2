package com.example.snackattack_2.Game_2.data;

import com.example.snackattack_2.Game_2.init.MySP;
import com.google.gson.Gson;

public class DataManager {

    private static ListOfScoreRecords getScoreRecords() {
        String recordsJson = MySP.getInstance().getString("records", "");
        return new Gson().fromJson(recordsJson, ListOfScoreRecords.class);
    }

    public static ListOfScoreRecords getTopTenScoreRecords() {
        ListOfScoreRecords listOfResults = getScoreRecords();

        if (listOfResults != null) {
            listOfResults.getListOfScoreRecords().sort((sr1, sr2) -> Integer.compare(sr2.getScore(), sr1.getScore()));
            ListOfScoreRecords topTenResults = new ListOfScoreRecords();

            for (int i = 0; i < listOfResults.getListOfScoreRecords().size() && i < 10; i++) {
                topTenResults.add(listOfResults.getListOfScoreRecords().get(i));
            }

            return topTenResults;
        }
        return null;
    }
}
