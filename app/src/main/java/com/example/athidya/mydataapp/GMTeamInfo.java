package com.example.athidya.mydataapp;

/**
 * Created by s5vignes on 2017-10-02.
 */

public class GMTeamInfo {

    public String teamName = "";
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

    public GMTeamInfo(String teamName,PlayerInfo[] players) {
        this.teamName=teamName;
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

        for (int i = 0; i < players.length; i++) {
            fieldGoalAttemptsTotal+= players[i].fieldGoalAttempts;
            fieldGoalMadeTotal+= players[i].fieldGoalMade;
            freeThrowAttemptsTotal+= players[i].freeThrowAttempts;
            freeThrowMadeTotal+= players[i].freeThrowMade;
            threePointsMadeTotal+= players[i].threePointsMade;
            pointsTotal+= players[i].points;
            reboundsTotal+= players[i].rebounds;
            assistsTotal+= players[i].assists;
            stealsTotal+= players[i].steals;
            blocksTotal+= players[i].blocks;
            turnoversTotal+= players[i].turnovers;
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
}

