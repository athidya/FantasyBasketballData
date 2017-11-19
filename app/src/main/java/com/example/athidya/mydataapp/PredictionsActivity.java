package com.example.athidya.mydataapp;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.athidya.mydataapp.R;
/*
 * TODO: call players games for the current week irl
 * TODO: put return button
 */

public class PredictionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Parcelable[] parcel = getIntent().getParcelableArrayExtra("Team Players");
        PlayerInfo[] players = new PlayerInfo[parcel.length];
        System.arraycopy(parcel, 0, players, 0, parcel.length);
        String teamName = getIntent().getStringExtra("Team Name");
        String teamKey = getIntent().getStringExtra("Team Key");
        String leagueID = getIntent().getStringExtra("League ID"); //dont know if leagueID or gameID necesscary to pull player game stats, if not needed remove*TODO
        String gameID = getIntent().getStringExtra("Game ID");
        displayTeam(players, teamName, teamKey, leagueID, gameID);
    }

    private void getPlayersGames(PlayerInfo[] players, String teamName, String teamKey, String leagueID, String gameID, TextView gamesPlayed) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://fantasysports.yahooapis.com/fantasy/v2/player/" + players[0].getPlayerid() + "/games";

        //StringRequest gamesnumReq = new StringRequest()

    }

    public void displayTeam(PlayerInfo[] players, String teamName, String teamKey, String leagueID, String gameID) {

        for (int i = 0; i<=players.length; i++) {
            players[i].CalculateStats();
        }
        TableLayout teamTable = (TableLayout) findViewById(R.id.team_table);
        TableRow players_head = new TableRow(this);
        players_head.setId(View.generateViewId());
        players_head.setBackgroundColor(Color.GRAY);
        players_head.setLayoutParams(new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));

        TextView team_label = new TextView(this);
        team_label.setId(View.generateViewId());
        team_label.setText(teamName);
        team_label.setTextColor(Color.BLACK);
        team_label.setPadding(5, 5, 5, 5);
        players_head.addView(team_label);

        String[] statsnames = {"FG%", "3PM", "FT%", "PTS", "REB", "AST", "ST", "BLK",  "TO"};
        TextView[] statsname_label = new TextView[statsnames.length];
        for (int s = 0; s<statsnames.length; s++) {
            statsname_label[s] = new TextView(this);
            statsname_label[s].setId(View.generateViewId());
            statsname_label[s].setText(statsnames[s]);
            statsname_label[s].setTextColor(Color.BLACK);
            statsname_label[s].setPadding(5, 5, 5, 5);
            players_head.addView(statsname_label[s]);
        }

        teamTable.addView(players_head);
        TextView[][] playersStats = new TextView[players.length][statsnames.length];
        TextView[] playersNames = new TextView[players.length];
        TableRow[] playersList = new TableRow[players.length];

        for(int i = 0; i<players.length; i++) {
            playersList[i] = new TableRow(this);
            playersList[i].setId(View.generateViewId());
            playersList[i].setBackgroundColor(Color.WHITE);
            playersList[i].setLayoutParams(new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            playersNames[i] = new TextView(this);
            playersNames[i].setId(View.generateViewId());
            playersNames[i].setTextColor(Color.BLACK);
            playersNames[i].setPadding(5, 5, 5, 5);
            playersNames[i].setText(players[i].getName());
            playersList[i].addView(playersNames[i]);

            String[] playersStatArray;
            playersStatArray = players[i].getStats();
            for(int j = 0; j<statsnames.length; j++) {
                playersStats[i][j] = new TextView(this);
                playersStats[i][j].setId(View.generateViewId());
                playersStats[i][j].setTextColor(Color.BLACK);
                playersStats[i][j].setText(playersStatArray[j]);
                playersStats[i][j].setPadding(5, 5, 5, 5);
                playersList[i].addView(playersStats[i][j]);

            }
            teamTable.addView(playersList[i]);
        }

    }
}
