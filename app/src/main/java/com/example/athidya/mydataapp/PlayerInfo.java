package com.example.athidya.mydataapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

/**
 * Created by s5vignes on 2017-10-02.
 */

public class PlayerInfo implements Parcelable {

    public Random rand = new Random();
    private String playerid;
    private String firstName;
    private String lastName;
    protected int fieldGoalAttempts,fieldGoalMade,
    numOfGamesPlayed,
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
        this.numOfGamesToPlay = 3;
        this.numOfGamesPlayed=0;
    }

    protected PlayerInfo(Parcel in) {
        playerid = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        fieldGoalAttempts = in.readInt();
        fieldGoalMade = in.readInt();
        freeThrowAttempts = in.readInt();
        freeThrowMade = in.readInt();
        threePointsMade = in.readInt();
        points = in.readInt();
        rebounds = in.readInt();
        assists = in.readInt();
        steals = in.readInt();
        blocks = in.readInt();
        turnovers = in.readInt();
        fieldGoalPercentage = in.readFloat();
        freeThrowPercentage = in.readFloat();
        teamName = in.readString();
        numOfGamesToPlay = in.readInt();
        numOfGamesPlayed = in.readInt();
    }

    public static final Creator<PlayerInfo> CREATOR = new Creator<PlayerInfo>() {
        @Override
        public PlayerInfo createFromParcel(Parcel in) {
            return new PlayerInfo(in);
        }

        @Override
        public PlayerInfo[] newArray(int size) {
            return new PlayerInfo[size];
        }
    };

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

    public void CalculateStats() {
        fieldGoalPercentage = ((fieldGoalMade/fieldGoalAttempts)/numOfGamesPlayed)*numOfGamesToPlay;
        freeThrowPercentage = ((freeThrowMade/freeThrowAttempts)/numOfGamesPlayed)*numOfGamesToPlay;
    }

    public String toString() {
        String player = "Player ID: " + playerid + ", First Name: " + firstName + ", Last Name: " + lastName;
        return player;
    }

    public String[] getStats(){
        String[] playerstats = {Float.toString(fieldGoalPercentage), Integer.toString(threePointsMade), Float.toString(freeThrowPercentage), Integer.toString(points), Integer.toString(rebounds),
                                Integer.toString(assists), Integer.toString(steals), Integer.toString(blocks), Integer.toString(turnovers)};
        return playerstats;
    }

    /*public void setStatsStr(String[] stats) {
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
    }*/
    public String getPlayerid() {
        return playerid;
    }

    public String getName() { return firstName + " " + lastName;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(playerid);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeInt(fieldGoalAttempts);
        dest.writeInt(fieldGoalMade);
        dest.writeInt(freeThrowAttempts);
        dest.writeInt(freeThrowMade);
        dest.writeInt(threePointsMade);
        dest.writeInt(points);
        dest.writeInt(rebounds);
        dest.writeInt(assists);
        dest.writeInt(steals);
        dest.writeInt(blocks);
        dest.writeInt(turnovers);
        dest.writeFloat(fieldGoalPercentage);
        dest.writeFloat(freeThrowPercentage);
        dest.writeString(teamName);
        dest.writeInt(numOfGamesToPlay);
        dest.writeInt(numOfGamesPlayed);
    }
}