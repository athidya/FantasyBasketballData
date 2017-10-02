package com.example.athidya.mydataapp;

/**
 * Created by s5vignes on 2017-10-02.
 */

public class PlayerInfo {

    private String firstName;
    private String lastName;
    private int fieldGoalAttempts,fieldGoalMade,
    freeThrowAttempts,freeThrowMade,
    threePointsMade,
    points,
    rebounds,
    assists,
    steals,
    blocks,
    turnovers;

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

    public void printStats()
    {
        System.out.println(firstName+" "+lastName);
        System.out.println("=======================================");
        System.out.println("FT% = " + Integer.toString( freeThrowMade ));
        System.out.println("FG% = " + Integer.toString( fieldGoalAttempts ));
        System.out.println("3PM = " + Integer.toString( threePointsMade ));
        System.out.println("POINTS = " + Integer.toString( points ));
        System.out.println("REBOUNDS = " + Integer.toString( rebounds ));
        System.out.println("ASSISTS = " + Integer.toString( assists ));
        System.out.println("STEALS = " + Integer.toString( steals ));
        System.out.println("BLOCKS = " + Integer.toString( blocks ));
        System.out.println("TURNOVERS = " + Integer.toString( turnovers ));

    }

}