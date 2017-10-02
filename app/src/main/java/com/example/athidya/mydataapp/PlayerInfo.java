package com.example.athidya.mydataapp;

import java.util.Random;

/**
 * Created by s5vignes on 2017-10-02.
 */

public class PlayerInfo {

    private Random rand = new Random();
    private String firstName;
    private String lastName;
    public int fieldGoalAttempts,fieldGoalMade,
    freeThrowAttempts,freeThrowMade,
    threePointsMade,
    points,
    rebounds,
    assists,
    steals,
    blocks,
    turnovers;
    public float fieldGoalPercentage,freeThrowPercentage;


    public PlayerInfo(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fieldGoalMade=this.freeThrowMade =0;
        this.threePointsMade=this.points=0;
        this.rebounds=this.assists=0;
        this.steals=this.blocks=0;
        this.turnovers =0;

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

}