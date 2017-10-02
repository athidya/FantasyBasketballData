package com.example.athidya.mydataapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SathActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sath);

        //HERE WE INITIALLIZE THE BASIC FRAME WORK
        final int GRID_ROWS = 11;
        final int GRID_COLOUMNS = 3;
        final int TOTAL_BUTTONS = GRID_ROWS * GRID_COLOUMNS;
        final Button[] buttonArray = new Button[TOTAL_BUTTONS];
        final int[] BUTTON_IDS = {
                R.id.button_1_1,
                R.id.button_1_2,
                R.id.button_1_3,
                R.id.button_2_1,
                R.id.button_2_2,
                R.id.button_2_3,
                R.id.button_3_1,
                R.id.button_3_2,
                R.id.button_3_3,
                R.id.button_4_1,
                R.id.button_4_2,
                R.id.button_4_3,
                R.id.button_5_1,
                R.id.button_5_2,
                R.id.button_5_3,
                R.id.button_6_1,
                R.id.button_6_2,
                R.id.button_6_3,
                R.id.button_7_1,
                R.id.button_7_2,
                R.id.button_7_3,
                R.id.button_8_1,
                R.id.button_8_2,
                R.id.button_8_3,
                R.id.button_9_1,
                R.id.button_9_2,
                R.id.button_9_3,
                R.id.button_10_1,
                R.id.button_10_2,
                R.id.button_10_3,
                R.id.button_11_1,
                R.id.button_11_2,
                R.id.button_11_3
        };

        for (int i = 0; i < TOTAL_BUTTONS; i++) {
            buttonArray[i] = (Button) findViewById(BUTTON_IDS[i]);
            buttonArray[i].setText(Integer.toString(i));
        }

        buttonArray[1].setText("WEEKXX");
        buttonArray[2].setText("STATSTYPE");
        buttonArray[4].setText("GM-A");
        buttonArray[5].setText("GM-B");

        final String[] STATS = {
                "FT%",
                "FG%",
                "3PM",
                "Points",
                "Rebounds",
                "Assists",
                "Steals",
                "Blocks ",
                "Turnovers",
        };
        for (int i = 0; i < STATS.length; i++) {
            buttonArray[i*3 + 6].setText(STATS[i]);
        }

        // WE NOW WILL CREATE TEMPORARY PLAYERS WITH RANDOM STATS PER CATEGORY EVENTUALLY REPLACED
        // BY THE ACTUAL CODE

        PlayerInfo[] sathPlayers= new PlayerInfo[13];
        sathPlayers[0] = new PlayerInfo("Lebron","James");
        sathPlayers[1] = new PlayerInfo("Steph","Curry");
        sathPlayers[2] = new PlayerInfo("CP","3");
        sathPlayers[3] = new PlayerInfo("horford","al");
        sathPlayers[4] = new PlayerInfo("boo","fun");
        sathPlayers[5] = new PlayerInfo("cry","day");
        sathPlayers[6] = new PlayerInfo("rivers","austin");
        sathPlayers[7] = new PlayerInfo("cry","same");
        sathPlayers[8] = new PlayerInfo("win","all");
        sathPlayers[9] = new PlayerInfo("games","is the");
        sathPlayers[10] = new PlayerInfo("goal","testing");
        sathPlayers[11] = new PlayerInfo("popavich","lonzo");
        sathPlayers[12] = new PlayerInfo("ball","test");

        final GMTeamInfo SathTeam = new GMTeamInfo("Sath's rookie team",sathPlayers);

        for (int i = 0; i < SathTeam.players.length; i++) {
            SathTeam.players[i].printStats();
        }
    }


}
