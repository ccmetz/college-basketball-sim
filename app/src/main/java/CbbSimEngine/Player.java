package CbbSimEngine;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by ccmetz on 4/3/16.
 *
 * Generic Player class that will contain a player's name, position, attributes, stats, etc
 */
public class Player {

    // Defined in the constructor
    private String name;
    private String fName;
    private String lName;
    private int position; //Position marked by ints (ex: 1 = pg, 2 = sg, etc)
    private String posString;
    private int year;
    private int stars;
    private int type;
    private String typeString;
    private Team team;

    // Player Skill Ratings
    /* Shooting */
    private int layupRating;
    private int postRating;
    private int midRating;
    private int threeRating;
    /* Defense */
    private int defenseRating;
    private int reboundRating;
    private int stealRating;
    private int blockRating;
    /* General Basketball Skills */
    private int passRating;
    private int heightRating;
    private int handleRating;

    // Player Tendencies
    /* Offensive */
    private int shootOrPass; // Scale 0 - 100 where 0 is shoot and 100 is pass
    private int jumperOrDrive;
    private int layupOrPost;
    private int midOrThree;
    /* Defensive */
    private int tryForSteal; // Scale 0 - 100 where 0 is play sound defense and 100 is gamble for steals/blocks
    private int tryForBlock;

    // Miscellaneous Player variables
    /* String representation of the height rating */
    private String playerHt;
    /* Overall rating */
    private int overall;
    private int offOvr;
    private int defOvr;
    private int oneAndDoneBonus;
    /* Starter, Role Player, or Bench? */
    private Role playerRole;

    // Player Career Stats
    private int points = 0;
    private int games = 0;
    private int rebounds = 0;
    private int defRebounds = 0;
    private int offRebounds = 0;
    private int assists = 0;
    private int steals = 0;
    private int blocks = 0;
    private int fgm2 = 0;
    private int fga2 = 0;
    private int fgm3 = 0;
    private int fga3 = 0;
    private int turnovers = 0;


    //Constructor for randomly generated Player Name and attributes
    //STARS will influence the player's attributes
    public Player(String first, String last, int pos, int yr, int stars, int t, Team tm){

        fName = first;
        lName = last;
        name = first + " " + last;
        position = pos;
        year = yr;
        team = tm;
        type = t;
        this.stars = stars;
        oneAndDoneBonus = 0;
        playerRole = Role.BENCH; //defaults to bench


        if(year == 4) year = 3; //In the unlikely possibility that year = 4, change to 3

        // If player is one and done talent -> year will be freshman
        if(stars == 6){
            year = 0;
            oneAndDoneBonus = 10; //Makes up for lack of years when generating player attributes
        }

        if(type == 4) type = 3; //In the unlikely possibility that type = 4, change to 3


        // Set Position String
        if(position == 1){

            posString = "PG";
        }
        else if(position == 2){

            posString = "SG";

        }
        else if(position == 3){

            posString = "SF";
        }
        else if(position == 4){

            posString = "PF";
        }
        else if(position == 5){

            posString = "C";
        }

        // Generate Player Ratings and Tendencies
        generatePlayerRatings();
        generatePlayerTendencies();


    }

    // Player roles will be designated as either STARTER, ROLEPLAYER, BENCHRIDER
    // STARTERs will start and get the majority of the minutes
    // ROLEPLAYERs will come off the bench and take the rest of the minutes
    // BENCH players will not play
    public enum Role{
        STARTER, ROLEPLAYER, BENCH
    }

    // Sets the Player's playerRole to either STARTER, ROLEPLAYER, or BENCHRIDER
    public void setPlayerRole(Role r){

        playerRole = r;
    }


    /* Used for displaying player ratings in string form */
    public String displayPlayerInfo(){

        return "[" + getYearString() + "] " + posString + " " + name + " " + playerHt + " - Ovr: " + overall;
    }

    /* Returns ArrayList of Players stats and ratings */
    public ArrayList<String> displayStatsAndRatings(){

        ArrayList<String> list = new ArrayList<String>();
        list.add("Lay: " + layupRating + "/Def: " + defenseRating);
        list.add("Post: " + postRating + "/Reb: " + reboundRating);
        list.add("Mid: " + midRating + "/Stl: " + stealRating);
        list.add("Out: " + threeRating + "/Blk: " + blockRating);
        list.add("Pass: " + passRating + "/Type: " + typeString);
        list.add("Hand: " + handleRating + "/Stars: " + stars);
        list.add("Off Ovr: " + offOvr + "/Def Ovr: " + defOvr);

        return list;
    }

    /* Returns a shorter String to be used for the Adjust Lineup dialog */
    public String displayShortPlayerInfo(){

        return "[" + getYearString() + "] " + posString + " " + fName.substring(0,1) + "." + lName +
                " " + playerHt + "\nOvr: " + overall;
    }


    public String getYearString(){

        if(year == 3 || year == 4){
            return "Sr";
        }
        else if(year == 2){
            return "Jr";
        }
        else if(year == 1){
            return "So";
        }
        else if(year == 0){
            return "Fr";
        }

        return "?";
    }

    /* Method that calculates the Player's height in ft and inches based on their heightRating integer value */
    private void calcPlayerHt(int htRating){

        float measurement = (float) ((71 + (14*htRating/100))/12.0);
        int feet = (int) measurement;
        float fraction = measurement - feet;
        int inches = (int) (fraction*12.0);

        playerHt = feet + "'" + inches + "''";
    }


    /* Method to set the height rating of the player based on position */
    public void setHeightRating(int pos){

        if(pos == 1){

            heightRating = (int) (100 * Math.random()/2); //Rating will range from 0 - 50 for point guards
        }
        else if(pos == 2){

            heightRating = (int) ((100 * Math.random()/2) + 20); //Rating will range from 20-60 for shooting guards
            // Limit heightRating to 60
            if(heightRating > 60){
                heightRating = 60;
            }
        }
        else if(pos == 3){

            heightRating = (int) ((100 * Math.random()/2) + 40); //Rating will range from 40-80 for SFs
            // Limit heightRating to 80
            if(heightRating > 80){
                heightRating = 80;
            }
        }
        else if(pos == 4){

            heightRating = (int) ((100 * Math.random()/2) + 60); //Rating will range from 60-90 for PFs
            // Limit heightRating to 90
            if(heightRating > 90){
                heightRating = 90;
            }
        }
        else if(pos == 5){

            heightRating = (int) ((100 * Math.random()/2) + 70); //Rating will range from 70-100 for Cs
            // Limit heightRating to 100
            if(heightRating > 100){
                heightRating = 100;
            }
        }

        calcPlayerHt(heightRating);
    }

    /* Method to set the default base ratings for each position */
    public int[] setModifiers(int pos){

        int[] base = new int[10];

        if(pos ==  1){
            base[0] = 50; // layup
            base[1] = 30; // post
            base[2] = 45; // mid
            base[3] = 45; // three
            base[4] = 45; // defense
            base[5] = 30; // rebounding
            base[6] = 45; // steal
            base[7] = 30; // block
            base[8] = 50; // passing
            base[9] = 50; // handles
        }
        else if(pos == 2){
            base[0] = 45;
            base[1] = 35;
            base[2] = 45;
            base[3] = 45;
            base[4] = 45;
            base[5] = 35;
            base[6] = 45;
            base[7] = 30;
            base[8] = 45;
            base[9] = 45;
        }
        else if(pos == 3){
            base[0] = 45;
            base[1] = 35;
            base[2] = 45;
            base[3] = 45;
            base[4] = 45;
            base[5] = 40;
            base[6] = 45;
            base[7] = 40;
            base[8] = 45;
            base[9] = 40;
        }
        else if(pos == 4){
            base[0] = 40;
            base[1] = 45;
            base[2] = 45;
            base[3] = 35;
            base[4] = 45;
            base[5] = 45;
            base[6] = 35;
            base[7] = 45;
            base[8] = 35;
            base[9] = 40;
        }
        else if(pos == 5){
            base[0] = 40;
            base[1] = 50;
            base[2] = 40;
            base[3] = 30;
            base[4] = 45;
            base[5] = 50;
            base[6] = 30;
            base[7] = 50;
            base[8] = 30;
            base[9] = 35;
        }

        return base;
    }


    /* Type:
    *  0 - Passer or Rebounder
    *  1 - Shooter
    *  2 - Driver or Post Up
    *  3 - Defender */
    public void addTypeBonus(int[] b, int pos, int type){


        if(type == 0 && (pos == 4 || pos == 5)){

            typeString = "Rebounder";
            b[5] += 10;
        }
        else if(type == 0 && (pos == 1 || pos == 2 || pos == 3)){

            typeString = "Passer";
            b[8] += 10;
        }
        else if(type == 1){

            typeString = "Shooter";
            b[2] += 5;
            b[3] += 5;
        }
        else if(type == 2 && (pos == 4 || pos == 5)){

            typeString = "Post Up";
            b[1] += 10;
        }
        else if(type == 2 && (pos == 1 || pos == 2 || pos == 3)){

            typeString = "Driver";
            b[0] += 10;
        }
        else if(type == 3 && (pos == 4 || pos == 5)){

            typeString = "Defender";
            b[4] += 10;
            b[7] += 5; //Bonus to Blocks
        }
        else if(type == 3 && (pos == 1 || pos == 2 || pos == 3)){

            typeString = "Defender";
            b[4] += 10;
            b[6] += 5; //Bonus to Steals
        }
    }


    /* EXPERIMENTAL: SINGLE METHOD FOR GENERATING ALL PLAYER POSITIONS
     * Note: Still need to decide what to do with player specialties? Keep them or get rid of them? */
    public void generatePlayerRatings(){

        int[] base = setModifiers(position);

        addTypeBonus(base, position, type);

        layupRating = (int) (base[0] + (year*3) + (stars*4) + (20 * Math.random()) + oneAndDoneBonus);
        postRating = (int) (base[1] + (year*3) + (stars*4) + (20 * Math.random()) + oneAndDoneBonus);
        midRating = (int) (base[2] + (year*3) + (stars*4) + (20 * Math.random()) + oneAndDoneBonus);
        threeRating = (int) (base[3] + (year*3) + (stars*4) + (20 * Math.random()) + oneAndDoneBonus);

        defenseRating = (int) (base[4] + (year*3) + (stars*4) + (20 * Math.random()) + oneAndDoneBonus);
        reboundRating = (int) (base[5] + (year*3) + (stars*4) + (20 * Math.random()) + oneAndDoneBonus);
        stealRating = (int) (base[6] + (year*3) + (stars*4) + (20 * Math.random()) + oneAndDoneBonus);
        blockRating = (int) (base[7] + (year*3) + (stars*4) + (20 * Math.random()) + oneAndDoneBonus);

        passRating = (int) (base[8] + (year*3) + (stars*4) + (20 * Math.random()) + oneAndDoneBonus);
        handleRating = (int) (base[9] + (year*3) + (stars*4) + (20 * Math.random()) + oneAndDoneBonus);

        setHeightRating(position);

        calcOverall();

    }

    /* Method that will generate player tendencies based upon their position, ratings, and player type
    * shootOrPass is affected by player type
    * jumperOrDrive is affected by player type
    * layupOrPost is affected by position and player type
    * midOrThree is affected by player ratings
    * tryForSteal is affected by position and player ratings
    * tryForBlock is affected by position and player ratings */
    public void generatePlayerTendencies(){

        // Default Tendencies
        shootOrPass = 60;
        jumperOrDrive = 50;
        layupOrPost = 50;
        midOrThree = 50;
        tryForSteal = 20;
        tryForBlock = 20;

        // Tendency adjustments based on position played
        if(position == 1){

            layupOrPost -= 40;
            tryForSteal += 5;
        }
        else if(position == 2 || position == 3){

            layupOrPost -= 35;
            tryForSteal += 5;
        }
        else if(position == 4 || position == 5){

            tryForBlock += 5;
        }

        // Tendency adjustments based on attributes
        if(threeRating >= midRating + 10){

            midOrThree += 10;
        }
        else if(midRating >= threeRating + 10){

            midOrThree -= 10;
        }

        if(stealRating >= 75){

            tryForSteal += 10;
        }

        if(blockRating >= 75){

            tryForBlock += 10;
        }


        // Tendency adjustments based on Player Type (Rebounder and Defender doesn't affect tendencies)
        // Passer
        if(type == 0 && position == 1){

            shootOrPass += 15;
        }
        // Shooter
        else if(type == 1){

            shootOrPass -= 10;
            jumperOrDrive -= 10;

        }
        // Driver/Post Up
        else if(type == 2){

            jumperOrDrive += 10;

            if(position == 1 || position == 2 || position == 3){
                layupOrPost -= 5;
            }
            else if(position == 4 || position == 5){
                layupOrPost += 10;
            }
        }

    }

    public void calcOverall(){

        // Backcourt overall
        if(position == 1 || position == 2 || position == 3){

            offOvr = (int) ((layupRating + postRating*0.15 + midRating + threeRating
                    + passRating*0.75 + handleRating*0.75)/4.65);
            defOvr = (int) ((defenseRating + reboundRating*0.75 + stealRating + blockRating*0.75)/3.5);
        }
        // Frontcourt overall
        else if(position == 4 || position == 5){

            offOvr = (int) ((layupRating + postRating + midRating + threeRating *0.75
                    + passRating*0.25 + handleRating*0.25)/4.25);
            defOvr = (int) ((defenseRating + reboundRating + stealRating*0.75 + blockRating)/3.75);
        }

        overall = (offOvr + defOvr)/2;
    }

    // Method for creating the player stats grid
    public String[] getPlayerStats(){

        String[] stats = new String[12];

        DecimalFormat df = new DecimalFormat("#.#");

        double ppg = (double) points/games;
        double rpg = (double) rebounds/games;
        double apg = (double) assists/games;
        double tpg = (double) turnovers/games;
        double spg = (double) steals/games;
        double bpg = (double) blocks/games;
        double fgPercent = (double) (fgm2 + fgm3)/(fga2 + fga3)*100;
        double fg3Percent = (double) fgm3/fga3*100;
        double fgaPG = (double) (fga2 + fga3)/games;
        double fga3PG = (double) fga3/games;
        double oRebPG = (double) offRebounds/games;
        double dRebPG = (double) defRebounds/games;

        stats[0] = "PPG" + "\n" + df.format(ppg);
        stats[1] = "RPG" + "\n" + df.format(rpg);
        stats[2] = "APG" + "\n" + df.format(apg);
        stats[3] = "TOPG" + "\n" + df.format(tpg);
        stats[4] = "SPG" + "\n" + df.format(spg);
        stats[5] = "BPG" + "\n" + df.format(bpg);
        stats[6] = "FG%" + "\n" + df.format(fgPercent);
        stats[7] = "3FG%" + "\n" + df.format(fg3Percent);
        stats[8] = "FGAPG" + "\n" + df.format(fgaPG);
        stats[9] = "3FGAPG" + "\n" + df.format(fga3PG);
        stats[10] = "OREB/PG" + "\n" + df.format(oRebPG);
        stats[11] = "DREB/PG" + "\n" + df.format(dRebPG);

        return stats;
    }

    // Add methods for Player Stats
    public void add2ptMake() {
        points += 2;
        fgm2++;
        fga2++;
    }

    public void add2ptMiss() {
        fga2++;
    }

    public void add3ptMake() {
        points += 3;
        fgm3++;
        fga3++;
    }

    public void add3ptMiss() {
        fga3++;
    }

    public void addDefRebound() {
        rebounds++;
        defRebounds++;
    }

    public void addOffRebound() {
        rebounds++;
        offRebounds++;
    }

    public void addAssist() {
        assists++;
    }

    public void addSteal() {
        steals++;
    }

    public void addBlock() {
        blocks++;
    }

    public void addTurnover() {
        turnovers++;
    }

    public void addGame() {
        games++;
    }


    // GET METHODS FOR PLAYER ATTRIBUTES
    public String getName(){
        return name;
    }

    public String getLastName(){
        return lName;
    }

    public String getTeamAbbr(){
        return team.getAbbr();
    }

    public Role getPlayerRole(){
        return playerRole;
    }

    public int getPostRating(){
        return postRating;
    }

    public int getLayupRating(){
        return layupRating;
    }

    public int getMidRating(){
        return midRating;
    }

    public int getThreeRating(){
        return threeRating;
    }

    public int getDefenseRating(){
        return defenseRating;
    }

    public int getReboundRating(){
        return reboundRating;
    }

    public int getPassRating(){
        return passRating;
    }

    public int getHeightRating(){
        return heightRating;
    }

    public int getHandleRating(){
        return handleRating;
    }

    public int getStealRating(){
        return stealRating;
    }

    public int getBlockRating(){
        return blockRating;
    }

    public int getPosition(){
        return position;
    }

    public int getOverall(){
        return overall;
    }

    public int getOffOvr(){
        return offOvr;
    }

    public int getJumperOrDrive(){
        return jumperOrDrive;
    }

    public int getLayupOrPost(){
        return layupOrPost;
    }

    public int getMidOrThree(){
        return midOrThree;
    }

    public int getTryForSteal(){
        return tryForSteal;
    }

    public int getTryForBlock(){
        return tryForBlock;
    }


}
