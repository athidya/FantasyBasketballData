package com.example.athidya.mydataapp;

import java.util.Random;

/**
 * Created by s5vignes on 2017-10-02.
 */

public class PlayerInfo {

    public Random rand = new Random();
    private String playerid;
    private String firstName;
    private String lastName;
    private String[] statsStr = new String[28];
    private Integer[] statsInt = new Integer[28];
    public String[] statnames = {"GP", "GS", "MIN", "FGA", "FGM", "FG%", "FTA", "FTM", "FT%", "3PTA",
                                "3PTM", "3PT%", "PTS", "OREB", "DREB", "REB", "AST", "ST", "BLK","TO",
                                "A/T", "PF", "DISQ", "TECH", "EJCT", "FF", "MPG", "DD", "TD"};
    protected int fieldGoalAttempts,fieldGoalMade,
    freeThrowAttempts,freeThrowMade,
    threePointsMade,
    points,
    rebounds,
    assists,
    steals,
    blocks,
    turnovers;
    protected float fieldGoalPercentage,freeThrowPercentage;

    public String teamName;
    public int numOfGamesToPlay;

    public PlayerInfo(String[] info )
    {
        this.playerid = info[0];
        this.firstName = info[1];
        this.lastName = info[2];
        //this.teamName = teamName;
        this.fieldGoalMade=this.freeThrowMade =0;
        this.threePointsMade=this.points=0;
        this.rebounds=this.assists=0;
        this.steals=this.blocks=0;
        this.turnovers =0;
        this.numOfGamesToPlay = 1;
    }

    //Prints current stats
    public void printStats()
    {
        System.out.println(firstName+" "+lastName);
        System.out.println("=======================================");
        System.out.println("FT% = " +  String.format("%.1f", freeThrowPercentage*100)+"%");
        System.out.println("FG% = " + String.format("%.1f", fieldGoalPercentage*100)+"%");
        System.out.println("3PM = " + Integer.toString( threePointsMade ));
        System.out.println("POINTS = " + Integer.toString( points ));
        System.out.println("REBOUNDS = " + Integer.toString( rebounds ));
        System.out.println("ASSISTS = " + Integer.toString( assists ));
        System.out.println("STEALS = " + Integer.toString( steals ));
        System.out.println("BLOCKS = " + Integer.toString( blocks ));
        System.out.println("TURNOVERS = " + Integer.toString( turnovers ));

    }
    //Generates random stats
    public void generateRandomStat(){
        fieldGoalAttempts = 99;
        fieldGoalMade = rand.nextInt(50) + 1;
        fieldGoalPercentage =(float)fieldGoalMade/fieldGoalAttempts;
        freeThrowAttempts = 99;
        freeThrowMade = rand.nextInt(50) + 1;
        freeThrowPercentage = (float)freeThrowMade/freeThrowAttempts;
        threePointsMade = rand.nextInt(50) + 1;
        points = rand.nextInt(50) + 1;
        rebounds = rand.nextInt(50) + 1;
        assists = rand.nextInt(50) + 1;
        steals = rand.nextInt(50) + 1;
        blocks = rand.nextInt(50) + 1;
        turnovers = rand.nextInt(50) + 1;
    }

    public String toString() {
        String player = "Player ID: " + playerid + ", First Name: " + firstName + ", Last Name: " + lastName;
        return player;
    }

    public void setStatsStr(String[] stats) {
        this.statsStr = stats;
        setStatsInt();
    }
    public String[] getStatsStr() {
        return statsStr;
    }
    public void setStatsInt() {
        for (int i = 0; i<statsStr.length; i++) {
           if(statsStr[i].equals("-")) {
               statsInt[i] = 0;
           }
            else{
               statsInt[i] = Integer.parseInt(statsStr[i]);
           }
        }
    }
    public String getPlayerid() {
        return playerid;
    }

}