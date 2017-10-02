package com.example.athidya.mydataapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Random;

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
            buttonArray[i].setTextSize(12);
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

        PlayerInfo[] athidyaPlayers= new PlayerInfo[13];
        athidyaPlayers[0] = new PlayerInfo("Lebron","James");
        athidyaPlayers[1] = new PlayerInfo("Steph","Curry");
        athidyaPlayers[2] = new PlayerInfo("CP","3");
        athidyaPlayers[3] = new PlayerInfo("horford","al");
        athidyaPlayers[4] = new PlayerInfo("boo","fun");
        athidyaPlayers[5] = new PlayerInfo("cry","day");
        athidyaPlayers[6] = new PlayerInfo("rivers","austin");
        athidyaPlayers[7] = new PlayerInfo("cry","same");
        athidyaPlayers[8] = new PlayerInfo("win","all");
        athidyaPlayers[9] = new PlayerInfo("games","is the");
        athidyaPlayers[10] = new PlayerInfo("goal","testing");
        athidyaPlayers[11] = new PlayerInfo("popavich","lonzo");
        athidyaPlayers[12] = new PlayerInfo("ball","test");

        final GMTeamInfo sathTeam = new GMTeamInfo("Sath's rookie team",sathPlayers);
        final GMTeamInfo athidyaTeam = new GMTeamInfo("Athidya's cool team",sathPlayers);

        Random rand = new Random();

        for (int i = 0; i < sathTeam.players.length; i++) {
            sathTeam.players[i].generateRandomStat();
            athidyaTeam.players[i].generateRandomStat();

            //sathTeam.players[i].printStats();
        }

        //Above is just the creation of the random stats to random players. We have done the above
        // for 2 players

        //Here we will calculate the total of the players in each team and combine and then we will
        //output the values on to the buttons.
        sathTeam.CalculateTotal();
        athidyaTeam.CalculateTotal();

        //Sets the team names
        buttonArray[4].setText(sathTeam.teamName);
        buttonArray[5].setText(athidyaTeam.teamName);

        //Sets each teams stat
        float[] t1_p = sathTeam.TotalPercentage();
        float[] t2_p = athidyaTeam.TotalPercentage();
        int[] t1_s = sathTeam.TotalScore();
        int[] t2_s = athidyaTeam.TotalScore();

        for (int i = 0; i < t1_p.length+t1_s.length; i++) {
            if (i<2){
                buttonArray[i*3+7].setText(String.format("%.1f", t1_p[i]*100)+"%");
                 buttonArray[i*3+8].setText(String.format("%.1f", t2_p[i]*100)+"%");
            }
            else{
                buttonArray[i*3+7].setText(Integer.toString(t1_s[i-2]));
                buttonArray[i*3+8].setText(Integer.toString(t2_s[i-2]));
            }
        }

    }


}
