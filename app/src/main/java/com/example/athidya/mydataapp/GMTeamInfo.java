package com.example.athidya.mydataapp;

import java.text.DecimalFormat;

/**
 * Created by s5vignes on 2017-10-02.
 */

public class GMTeamInfo {

    public String teamName = "";
    public String teamKey = "";
    public PlayerInfo[] players;
    public int fieldGoalAttemptsTotal,fieldGoalMadeTotal,
            freeThrowAttemptsTotal,freeThrowMadeTotal,
            threePointsMadeTotal,
            pointsTotal,
            reboundsTotal,
            assistsTotal,
            stealsTotal,
            blocksTotal,
            turnoversTotal;
    public float fieldGoalPercentageTotal,freeThrowPercentageTotal;

    public GMTeamInfo(String teamKey,String teamName,PlayerInfo[] players) {
        this.teamName=teamName;
        this.teamKey = teamKey;
        this.players = players;
        this.fieldGoalMadeTotal=this.freeThrowMadeTotal=0;
        this.threePointsMadeTotal=this.pointsTotal=0;
        this.reboundsTotal=this.assistsTotal=0;
        this.stealsTotal=this.blocksTotal=0;
        this.turnoversTotal=0;
    }

    public void CalculateTotal(){
        fieldGoalMadeTotal=freeThrowMadeTotal=0;
        threePointsMadeTotal=pointsTotal=0;
        reboundsTotal=assistsTotal=0;
        stealsTotal=blocksTotal=0;
        turnoversTotal=0;
        int numOfGamesToPlay =0;

        for (int i = 0; i < players.length; i++) {

            numOfGamesToPlay = players[i].numOfGamesToPlay;

            if(players[i].numOfGamesPlayed!=0) {
                fieldGoalAttemptsTotal+= (players[i].fieldGoalAttempts/players[i].numOfGamesPlayed)*numOfGamesToPlay;
                fieldGoalMadeTotal+= (players[i].fieldGoalMade/players[i].numOfGamesPlayed)*numOfGamesToPlay;
                freeThrowAttemptsTotal+= (players[i].freeThrowAttempts/players[i].numOfGamesPlayed)*numOfGamesToPlay;
                freeThrowMadeTotal+= (players[i].freeThrowMade/players[i].numOfGamesPlayed)*numOfGamesToPlay;
                threePointsMadeTotal+= (players[i].threePointsMade/players[i].numOfGamesPlayed)*numOfGamesToPlay;
                pointsTotal+= (players[i].points/players[i].numOfGamesPlayed)*numOfGamesToPlay;
                reboundsTotal+= (players[i].rebounds/players[i].numOfGamesPlayed)*numOfGamesToPlay;
                assistsTotal+= (players[i].assists/players[i].numOfGamesPlayed)*numOfGamesToPlay;
                stealsTotal+= (players[i].steals/players[i].numOfGamesPlayed)*numOfGamesToPlay;
                blocksTotal+= (players[i].blocks/players[i].numOfGamesPlayed)*numOfGamesToPlay;
                turnoversTotal+= (players[i].turnovers/players[i].numOfGamesPlayed)*numOfGamesToPlay;
            }

        }

        fieldGoalPercentageTotal=(float)fieldGoalMadeTotal/fieldGoalAttemptsTotal;
        freeThrowPercentageTotal=(float)freeThrowMadeTotal/freeThrowAttemptsTotal;

    }

    public int[] TotalScore(){

        int[] stats = {threePointsMadeTotal,
                pointsTotal,
                reboundsTotal,
                assistsTotal,
                stealsTotal,
                blocksTotal,
                turnoversTotal};
        return stats;
    }

    public float[] TotalPercentage(){
        float[] stats   = {fieldGoalPercentageTotal,freeThrowPercentageTotal};
        return stats;
    }

    public String[] getStats() {
        DecimalFormat df = new DecimalFormat("#.##");
        String [] stats = {df.format(fieldGoalPercentageTotal), String.valueOf(threePointsMadeTotal),
                df.format(freeThrowPercentageTotal), String.valueOf(pointsTotal), String.valueOf(reboundsTotal), String.valueOf(assistsTotal), String.valueOf(stealsTotal), String.valueOf(blocksTotal), String.valueOf(turnoversTotal)};

        return stats;
    }
}

