package com.example.athidya.mydataapp;

/**
 * Created by s5vignes on 2017-10-02.
 */

public class GMTeamInfo {

    String teamName = "";
    PlayerInfo[] players;

    public GMTeamInfo(String teamName,PlayerInfo[] players) {
        this.teamName=teamName;
        this.players = players;

    }
}

