package CbbSimEngine;

import java.util.ArrayList;

/**
 * Created by ccmetz on 5/1/16.
 */
public class Game {

    private Team homeTeam;
    private Team awayTeam;
    private boolean hasBeenPlayed;

    private ArrayList<Player> homeLineup;
    private ArrayList<Player> homeOnFloor;//Home team current players that are on the floor
    private ArrayList<Player> homeBench; //Home team players that are available to be subbed into the game
    private ArrayList<Player> awayLineup;
    private ArrayList<Player> awayOnFloor; //Away team players that are on the floor
    private ArrayList<Player> awayBench; //Away team players that are available to be subbed into the game

    private int clock; //Game clock
    private int numOvertimes;
    private int homeScore;
    private int awayScore;
    private int possession; //0 = home team, 1 = away team

    private String gameLog; //Keeps track of the play by play of the game

    // These variables will be used for stats for the box score
    private int[][] homeStats;
    private int[][] awayStats;


    Game(Team hTeam, Team aTeam){

        homeTeam = hTeam;
        awayTeam = aTeam;
        hasBeenPlayed = false;
        possession = 0; //home team starts with the ball

        homeLineup = new ArrayList<Player>();
        homeOnFloor = new ArrayList<Player>();
        homeBench = new ArrayList<Player>();
        homeLineup = homeTeam.getRoster(); //set homeLineup equal to home team's roster

        awayLineup = new ArrayList<Player>();
        awayOnFloor = new ArrayList<Player>();
        awayBench = new ArrayList<Player>();
        awayLineup = awayTeam.getRoster(); //set awayLineup equal to the away team's roster

        gameLog = ""; //Game log is initially blank

        homeStats = new int[5][6]; //5 players -> tracks 2FGM, 2FGA, 3FGM, 3FGA, OREB, DREB
        awayStats = new int[5][6];

    }

    public String getGameResult(boolean home){

        String r = " ----";

        if(home){ //From the home team's perspective
            if(homeScore > awayScore){
                r = "W " + homeScore + "-" + awayScore;
            }
            else if(awayScore > homeScore){
                r = "L " + homeScore + "-" + awayScore;
            }
        }
        else{ //From the away team's perspective
            if(homeScore > awayScore){
                r = "L " + awayScore + "-" + homeScore;
            }
            else if(awayScore > homeScore){
                r = "W " + awayScore + "-" + homeScore;
            }
        }

        if(numOvertimes > 0){
            r = r + "(" + numOvertimes + "OT)";
        }

        return "Result: " + r;

    }

    public Team getHomeTeam(){

        return homeTeam;
    }

    public Team getAwayTeam(){

        return awayTeam;
    }

    public String getGameLog(){

        return gameLog;
    }

    // Method will create a box score to be displayed in a dialog on the schedule tab
    // ONLY OPTIMIZED FOR GAMES PLAYED WITHOUT SUBS - WILL NEED TO MODIFY WHEN SUB FUNCTIONALITY ADDED
    public String[] getBoxScore(){

        String[] boxScore = new String[4];
        String left;
        String right2pt;
        String right3pt;
        String rightReb;

        left = homeTeam.getAbbr() + "\n" + homeOnFloor.get(0).getName() + "\n" + homeOnFloor.get(1).getName() +
                "\n" + homeOnFloor.get(2).getName() + "\n" + homeOnFloor.get(3).getName() + "\n" +
                homeOnFloor.get(4).getName() + "\n" + awayTeam.getAbbr() + "\n" + awayOnFloor.get(0).getName() +
                "\n" + awayOnFloor.get(1).getName() + "\n" + awayOnFloor.get(2).getName() + "\n" + awayOnFloor.get(3).getName()
                + "\n" + awayOnFloor.get(4).getName() + "\n\n";

        right2pt = "2FGM  2FGA" + "\n" + homeStats[0][0] + "  " + homeStats[0][1] + "\n" + homeStats[1][0] +
                    "  " + homeStats[1][1] + "\n" + homeStats[2][0] + "  " + homeStats[2][1] + "\n" +
                    homeStats[3][0] + "  " + homeStats[3][1] + "\n" + homeStats[4][0] + "  " + homeStats[4][1] +
                    "\n--\n" + awayStats[0][0] + "  " + awayStats[0][1] + "\n" + awayStats[1][0] + "  " +
                    awayStats[1][1] + "\n" + awayStats[2][0] + "  " + awayStats[2][1] + "\n" + awayStats[3][0] +
                    "  " + awayStats[3][1] + "\n" + awayStats[4][0] + "  " + awayStats[4][1] + "\n\n";

        right3pt = "3FGM  3FGA" + "\n" + homeStats[0][2] + "  " + homeStats[0][3] + "\n" + homeStats[1][2] +
                    "  " + homeStats[1][3] + "\n" + homeStats[2][2] + "  " + homeStats[2][3] + "\n" +
                    homeStats[3][2] + "  " + homeStats[3][3] + "\n" + homeStats[4][2] + "  " + homeStats[4][3] + "\n--\n" +
                    awayStats[0][2] + "  " + awayStats[0][3] + "\n" + awayStats[1][2] + "  " + awayStats[1][3] +
                    "\n" + awayStats[2][2] + "  " + awayStats[2][3] + "\n" + awayStats[3][2] + "  " + awayStats[3][3] +
                    "\n" + awayStats[4][2] + "  " + awayStats[4][3] + "\n\n";

        rightReb = "OREB  DREB" + "\n" + homeStats[0][4] + "  " + homeStats[0][5] + "\n" + homeStats[1][4] +
                    "  " + homeStats[1][5] + "\n" + homeStats[2][4] + "  " + homeStats[2][5] + "\n" +
                    homeStats[3][4] + "  " + homeStats[3][5] + "\n" + homeStats[4][4] + "  " + homeStats[4][5] + "\n--\n" +
                    awayStats[0][4] + "  " + awayStats[0][5] + "\n" + awayStats[1][4] + "  " + awayStats[1][5] +
                    "\n" + awayStats[2][4] + "  " + awayStats[2][5] + "\n" + awayStats[3][4] + "  " + awayStats[3][5] +
                    "\n" + awayStats[4][4] + "  " + awayStats[4][5] + "\n";

        boxScore[0] = left;
        boxScore[1] = right2pt;
        boxScore[2] = right3pt;
        boxScore[3] = rightReb;

        return boxScore;

    }

    // Sets the current starters and available bench players for each team
    public void setTeamStartersAndBench(){

        for(int i = 0; i < homeLineup.size(); i++){

            if(homeLineup.get(i).getPlayerRole() == Player.Role.STARTER){
                homeOnFloor.add(homeLineup.get(i));
            }
            else if(homeLineup.get(i).getPlayerRole() == Player.Role.ROLEPLAYER){
                homeBench.add(homeLineup.get(i));
            }

        }

        for(int i = 0; i < awayLineup.size(); i++){

            if(awayLineup.get(i).getPlayerRole() == Player.Role.STARTER){
                awayOnFloor.add(awayLineup.get(i));
            }
            else if(awayLineup.get(i).getPlayerRole() == Player.Role.ROLEPLAYER){
                awayBench.add(awayLineup.get(i));
            }

        }
    }


    // This method will be used to simulate the Game!
    public void simGame(){

        if(!hasBeenPlayed){

            setTeamStartersAndBench();

            clock = 2400; //2400 secs = 40 min
            numOvertimes = 0;
            homeScore = 0;
            awayScore = 0;

            while(clock > 0){

                // Home team is on offense
                if(possession == 0){

                    runPlay(homeOnFloor, awayOnFloor);
                    possession = 1; //change possession after the home team finishes their play
                }
                // Away team is on offense
                else if(possession == 1){

                    runPlay(awayOnFloor, homeOnFloor);
                    possession = 0; //change possession after the away team finishes their play
                }

                if(clock <= 0 && homeScore == awayScore){

                    //Overtime!
                    clock = 300; // Add 5 min to clock
                    numOvertimes++;
                }

            }

            if(homeScore > awayScore){
                homeTeam.addWin();
                awayTeam.addLoss();
            }
            else if(awayScore > homeScore){
                awayTeam.addWin();
                homeTeam.addLoss();
            }

            hasBeenPlayed = true; //Once the game is finished, set to true

        }

    }

    /* Method for a single play
    *  Step 1: Determine what player has the ball
    *  Step 2: Determine whether a mismatch can be exploited
    *  Step 3: Determine whether to try to exploit the mismatch or run normal offense
    *  Step 4: Run normal offense -> determine whether pass or shoot while looping
    *  IMPORTANT: The positions within the offense and defense ArrayLists should always stay in order!
    *             Never sort them. They should always stay in the order of PG, SG, SF, PF, C or their
    *             respective subs! */
    public void runPlay(ArrayList<Player> offense, ArrayList<Player> defense){

        final int bringUpBallTime = 5; //5 seconds to bring the ball up the court on each possession
        boolean curPossession = true; //Keeps track of offensive possession
        double passChance = 0.90; // 85% chance the first ball handler will make a pass
        int passCounter = 0; //counts the number of passes to determine how much time each possession takes
        boolean shotMade = false;

        Player hasBall = offense.get(0); // Step 1: PG starts with the ball
        Player passer = hasBall; //PG will default as the passer
        int posOfBall = 0; //Keeps track of the position of the player within the offense ArrayList


        while(curPossession){

            if(Math.random() < passChance){

                //Decide who to pass to
                passer = hasBall;
                posOfBall = determineNextPass(hasBall, offense);
                hasBall = offense.get(posOfBall);
                passCounter++;
                passChance = passChance*0.7;

            }
            else{

                //Player with the ball shoots
                shotMade = attemptShot(passer, hasBall, defense, posOfBall);

                if(!shotMade){

                    //rebound the ball
                    posOfBall = rebound(offense, defense);

                    if(posOfBall == -1){
                        //defensive rebound - possession ends
                        curPossession = false;
                    }
                    else{
                        //offensive rebound - start possession over
                        hasBall = offense.get(posOfBall);
                        curPossession = true;
                    }

                }
                else {
                    curPossession = false; //current possession ends after shot is made
                }

                // Adjust the game clock
                if(passCounter*5 > 30){
                    clock = clock - 30; //clock can only lose 30 seconds per possession
                }
                else{
                    clock = clock - (passCounter*5) - bringUpBallTime;
                }


            }
        }



    }

    /* Method for determining where the next pass should go
    *  Currently based off of chance and each player's offensive overall */
    public int determineNextPass(Player p, ArrayList<Player> offense){

        int posToPass = 0; //defaults to PG
        double passProb = 0; //Stores a score that will be used to decide who will be passed to
        double largestPassProb = 0; //Stores the largest passProb score


        for(int i = 0; i < offense.size(); i++){

            if(!(offense.get(i).equals(p))){ //If current ball handler does not equal this player, calc passProb

                passProb = (offense.get(i).getOffOvr()*Math.random()*2);

                if(passProb > largestPassProb){
                    largestPassProb = passProb;
                    posToPass = i;
                }
            }

        }

        return posToPass;


    }

    /* Method for determining the outcome of a player's shot
    *  1. Determine the player's defender
    *  2. Determine whether the player will take an inside or outside shot
    *  3. Calculate whether the shot was made */
    public boolean attemptShot(Player passer, Player shooter, ArrayList<Player> defense, int pos){

        double probIns = shooter.getInsideShotRating();
        double probOut = shooter.getOutsideShotRating();
        Player defender = defense.get(pos);
        double insShotPercent = 15 + (shooter.getInsideShotRating()/2) - (defender.getDefenseRating()/5);
        double outShotPercent = 10 + (shooter.getOutsideShotRating()/2) - (defender.getDefenseRating()/5);
        double passerBonus = 0;

        if(!passer.equals(shooter)) {
            //No passer bonus for passers with rating under 70
            if (passer.getPassRating() >= 70) {

                passerBonus = 4 + ((passer.getPassRating() - 70) / 2);
            }
        }


        if(probIns * Math.random() >= probOut* Math.random()){
            //take inside shot
            if(100*Math.random() < insShotPercent + passerBonus){
                //shot made
                if(possession == 0){
                    homeScore += 2;
                    //Add 2FGM and 2FGA
                    homeStats[pos][0]++;
                    homeStats[pos][1]++;
                }
                else{
                    awayScore += 2;
                    //Add 2FGM and 2FGA
                    awayStats[pos][0]++;
                    awayStats[pos][1]++;
                }

                gameLog = gameLog + shooter.getTeamAbbr() + " " + shooter.getName() + " made a 2 pt basket " + clock + "\n";
                return true;
            }
            else{
                //shot missed
                if(possession == 0){
                    //Add 2FGA
                    homeStats[pos][1]++;
                }
                else{
                    //Add 2FGA
                    awayStats[pos][1]++;
                }

                gameLog = gameLog + shooter.getTeamAbbr() + " " + shooter.getName() + " missed a 2 pt basket " + clock + "\n";
                return false;
            }
        }
        else{
            //take outside shot
            if(100*Math.random() < outShotPercent + passerBonus){
                //shot made
                if(possession == 0){
                    homeScore += 3;
                    //Add 3FGM and 3FGA
                    homeStats[pos][2]++;
                    homeStats[pos][3]++;
                }
                else{
                    awayScore += 3;
                    //Add 3FGM and 3FGA
                    awayStats[pos][2]++;
                    awayStats[pos][3]++;
                }

                gameLog = gameLog + shooter.getTeamAbbr() + " " + shooter.getName() + " made a 3 pt basket " + clock + "\n";
                return true;
            }
            else{
                //shot missed
                if(possession == 0){
                    //Add 3FGA
                    homeStats[pos][3]++;
                }
                else{
                    //Add 3FGA
                    awayStats[pos][3]++;
                }

                gameLog = gameLog + shooter.getTeamAbbr() + " " + shooter.getName() + " missed a 3 pt basket " + clock + "\n";
                return false;
            }
        }


    }

    /* Method for determining who rebounds a missed shot
    *  It will return the position of the player if an offensive rebound is made (int 0 - 4)
    *  or it will return a '-1' if the defensive rebound was made */
    public int rebound(ArrayList<Player> offense, ArrayList<Player> defense){

        final double offRebPercent = 0.15; //Offensive rebounds occur 15% of the time

        int posOfDefReb = 4; //Keeps track of the position of the rebounder
        double defRebChance = 0; //Combination or rebound and height ratings multiplied by random decimal
        double largestDefRebChance = 0; //Keeps track of defender with largest rebounding chance

        int posOfOffReb = 4;
        double offRebChance = 0;
        double largestOffRebChance = 0;

        for(int i = 0; i < defense.size(); i++){

            defRebChance = (defense.get(i).getReboundRating() * Math.random()) +
                    (defense.get(i).getHeightRating() * Math.random());

            if(defRebChance > largestDefRebChance){
                largestDefRebChance = defRebChance;
                posOfDefReb = i;
            }
        }

        for(int i = 0; i < offense.size(); i++){

            offRebChance = (offense.get(i).getReboundRating() * Math.random()) +
                    (offense.get(i).getHeightRating() * Math.random());

            if(offRebChance > largestOffRebChance){
                largestOffRebChance = offRebChance;
                posOfOffReb = i;
            }
        }

        if(Math.random() > offRebPercent){

            //defensive rebound
            gameLog = gameLog + defense.get(posOfDefReb).getTeamAbbr() + " " + defense.get(posOfDefReb).getName() +
                        " grabbed the defensive rebound " + clock + "\n";
            if(possession == 1){
                //Away team possession so home team defensive rebounds
                homeStats[posOfDefReb][5]++;
            }
            else{
                //Home team possession so away team defensive rebounds
                awayStats[posOfDefReb][5]++;
            }
            return -1;
        }
        else{
            //offensive  rebound
            gameLog = gameLog + offense.get(posOfOffReb).getTeamAbbr() + " " + offense.get(posOfOffReb).getName() +
                        " grabbed the offensive rebound " + clock + "\n";
            if(possession == 0){
                //Home team possession so home team offensive rebounds
                homeStats[posOfOffReb][4]++;
            }
            else{
                //Away team possession so away team offensive rebounds
                awayStats[posOfOffReb][4]++;
            }
            return posOfOffReb;
        }

    }


}
