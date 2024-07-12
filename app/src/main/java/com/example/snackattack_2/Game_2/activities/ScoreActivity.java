package com.example.snackattack_2.Game_2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.snackattack_2.Game_2.data.ScoreRecord;
import com.example.snackattack_2.R;
import com.example.snackattack_2.Game_2.data.DataManager;
import com.example.snackattack_2.Game_2.data.ListOfScoreRecords;
import com.example.snackattack_2.Game_2.fragments.Fragment_List;
import com.example.snackattack_2.Game_2.fragments.Fragment_Maps;
import com.example.snackattack_2.Game_2.init.MySP;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

public class ScoreActivity extends AppCompatActivity {

    private MaterialButton score_BTN_close;
    private MaterialButton score_BTN_clear_scores;
    private AppCompatImageView score_IMG_background;
    private Fragment_List fragment_list;
    private Fragment_Maps fragment_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        findViews();
        initViews();
        initFragments();
        loadFragments();
    }

    private void initFragments() {
        fragment_map = new Fragment_Maps();
        fragment_map.setCallback_map(callback_map);

        fragment_list = new Fragment_List();
        fragment_list.setCallback_list(callback_list);
    }

    private final Callback_List callback_list = new Callback_List() {
        @Override
        public ListOfScoreRecords getTopTenScoreRecords() {
            return DataManager.getTopTenScoreRecords();
        }
    };

    public interface Callback_List {
        ListOfScoreRecords getTopTenScoreRecords();
    }

    private void loadFragments() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.score_LAY_list, fragment_list)
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.score_LAY_map, fragment_map)
                .commit();
    }

    private void findViews() {
        score_BTN_close = findViewById(R.id.score_BTN_close);
        score_BTN_clear_scores = findViewById(R.id.score_BTN_clear);
        score_IMG_background = findViewById(R.id.score_IMG_background);
    }

    private void initViews() {
        score_BTN_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        score_BTN_clear_scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearScores();
            }
        });
    }

    private void clearScores() {
        MySP.getInstance().putString("records", "");
        fragment_list.updateList(); // Update the list to reflect the cleared scores
    }

    public interface Callback_Map {
        void setMarkers(GoogleMap map);
    }

    public void zoomToLocation(LatLng location) {
        fragment_map.zoomToLocation(location);
    }

    private final Callback_Map callback_map = new Callback_Map() {
        @Override
        public void setMarkers(GoogleMap map) {
            map.clear();
            ListOfScoreRecords topTenScoreRecords = DataManager.getTopTenScoreRecords();
            if (topTenScoreRecords != null) {
                for (int i = 0; i < topTenScoreRecords.size(); i++) {
                    ScoreRecord result = topTenScoreRecords.get(i);
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(
                                    result.getLatitude(),
                                    result.getLongitude()))
                            .title("" + i));
                }
            }
        }
    };
}
