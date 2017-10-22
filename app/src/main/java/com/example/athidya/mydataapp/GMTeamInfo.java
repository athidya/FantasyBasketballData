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

            fieldGoalAttemptsTotal+= players[i].fieldGoalAttempts*numOfGamesToPlay;
            fieldGoalMadeTotal+= players[i].fieldGoalMade*numOfGamesToPlay;
            freeThrowAttemptsTotal+= players[i].freeThrowAttempts*numOfGamesToPlay;
            freeThrowMadeTotal+= players[i].freeThrowMade*numOfGamesToPlay;
            threePointsMadeTotal+= players[i].threePointsMade*numOfGamesToPlay;
            pointsTotal+= players[i].points*numOfGamesToPlay;
            reboundsTotal+= players[i].rebounds*numOfGamesToPlay;
            assistsTotal+= players[i].assists*numOfGamesToPlay;
            stealsTotal+= players[i].steals*numOfGamesToPlay;
            blocksTotal+= players[i].blocks*numOfGamesToPlay;
            turnoversTotal+= players[i].turnovers*numOfGamesToPlay;

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
        String [] stats = {String.valueOf(fieldGoalAttemptsTotal), String.valueOf(fieldGoalMadeTotal), df.format(fieldGoalPercentageTotal), String.valueOf(threePointsMadeTotal), String.valueOf(freeThrowAttemptsTotal),
                String.valueOf(freeThrowMadeTotal), df.format(freeThrowPercentageTotal), String.valueOf(pointsTotal), String.valueOf(reboundsTotal), String.valueOf(assistsTotal), String.valueOf(stealsTotal), String.valueOf(blocksTotal), String.valueOf(turnoversTotal)};

        return stats;
    }
}

