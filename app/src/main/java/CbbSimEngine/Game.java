package CbbSimEngine;

import java.util.ArrayList;

/**
 * Created by ccmetz on 5/1/16.
 */
public class Game {

    public boolean hasBeenPlayed;

    private Team homeTeam;
    private Team awayTeam;


    private ArrayList<Player> homeLineup;
    private ArrayList<Player> homeOnFloor;//Home team current players that are on the floor
    private ArrayList<Player> homeStarters;
    private ArrayList<Player> homeBench; //Home team players that are available to be subbed into the game
    private ArrayList<Player> awayLineup;
    private ArrayList<Player> awayOnFloor; //Away team players that are on the floor
    private ArrayList<Player> awayStarters;
    private ArrayList<Player> awayBench; //Away team players that are available to be subbed into the game

    private int clock; //Game clock
    private int numOvertimes;
    private int homeScore;
    private int awayScore;
    private int possession; //0 = home team, 1 = away team
    private boolean isBenchIn; //false -> Starters, true -> Bench (role players)

    private String gameLog; //Keeps track of the play by play of the game

    private BoxScore boxScore; //Handles the creation of the game box score


    Game(Team hTeam, Team aTeam){

        homeTeam = hTeam;
        awayTeam = aTeam;
        hasBeenPlayed = false;
        isBenchIn = false;
        possession = 0; //home team starts with the ball

        homeLineup = new ArrayList<Player>();
        homeOnFloor = new ArrayList<Player>();
        homeStarters = new ArrayList<Player>();
        homeBench = new ArrayList<Player>();
        homeLineup = homeTeam.getRoster(); //set homeLineup equal to home team's roster

        awayLineup = new ArrayList<Player>();
        awayOnFloor = new ArrayList<Player>();
        awayStarters = new ArrayList<Player>();
        awayBench = new ArrayList<Player>();
        awayLineup = awayTeam.getRoster(); //set awayLineup equal to the away team's roster

        gameLog = ""; //Game log is initially blank
    }

    /**
     * Used for determining the result of each team's possession
     * MAKE -> Possession changes to other team
     * MISS -> Rebound opportunity
     * STEAL -> Possession changes to other team
     * BLOCK -> Rebound opportunity
     */
    public enum Result{
        MAKE, MISS, STEAL, BLOCK
    }

    // Initialize the BoxScore object with the names of the players in the starting lineup
    public void initializeBoxScore(){

        String homeNames = "";
        String awayNames = "";

        for(int i = 0; i < homeStarters.size(); i++){
            homeNames = homeNames + homeStarters.get(i).getLastName() + "\n";
        }

        for(int i = 0; i < homeBench.size(); i++){
            homeNames = homeNames + homeBench.get(i).getLastName() + "\n";
        }

        homeNames = homeNames + "\n";

        for(int i = 0; i < awayStarters.size(); i++){
            awayNames = awayNames + awayStarters.get(i).getLastName() + "\n";
        }

        for(int i = 0; i < awayBench.size(); i++){
            awayNames = awayNames + awayBench.get(i).getLastName() + "\n";
        }

        awayNames = awayNames + "\n";

        boxScore = new BoxScore(homeNames, awayNames);
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
    public BoxScore getBoxScore(){

        boxScore.createBoxScore();
        return boxScore;

    }

    // Sets the current starters and available bench players for each team
    public void setTeamStartersAndBench(){

        for(int i = 0; i < homeLineup.size(); i++){

            if(homeLineup.get(i).getPlayerRole() == Player.Role.STARTER){
                homeStarters.add(homeLineup.get(i));
                homeLineup.get(i).addGame();
            }
            else if(homeLineup.get(i).getPlayerRole() == Player.Role.ROLEPLAYER){
                homeBench.add(homeLineup.get(i));
                homeLineup.get(i).addGame();
            }

        }

        Sorter.sortByPosition(homeBench);

        for(int i = 0; i < awayLineup.size(); i++){

            if(awayLineup.get(i).getPlayerRole() == Player.Role.STARTER){
                awayStarters.add(awayLineup.get(i));
                awayLineup.get(i).addGame();
            }
            else if(awayLineup.get(i).getPlayerRole() == Player.Role.ROLEPLAYER){
                awayBench.add(awayLineup.get(i));
                awayLineup.get(i).addGame();
            }

        }

        Sorter.sortByPosition(awayBench);

        homeOnFloor.addAll(homeStarters);
        awayOnFloor.addAll(awayStarters);
    }


    private void makeSubstitutions(){

        homeOnFloor.clear();
        awayOnFloor.clear();

        if(!isBenchIn) {
            homeOnFloor.addAll(homeBench);
            awayOnFloor.addAll(awayBench);
            isBenchIn = true;
        }
        else {
            homeOnFloor.addAll(homeStarters);
            awayOnFloor.addAll(awayStarters);
            isBenchIn = false;
        }
    }


    // This method will be used to simulate the Game!
    public void simGame(){

        if(!hasBeenPlayed){

            setTeamStartersAndBench();
            initializeBoxScore();

            clock = 2400; //2400 secs = 40 min
            numOvertimes = 0;
            homeScore = 0;
            awayScore = 0;

            while(clock > 0){

                //Check clock to decide if subs should come in
                //The role players will play from 1800 - 1500 secs and 600 - 300 secs left in the game
                if(clock <= 1800 && clock >= 1500 && !isBenchIn) makeSubstitutions();
                else if(clock <= 1500 && clock >= 1200 && isBenchIn) makeSubstitutions();
                else if(clock <= 600 && clock >= 300 && !isBenchIn) makeSubstitutions();
                else if(clock <= 300 && isBenchIn) makeSubstitutions();


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
        double passChance = 0.90; // 90% chance the first ball handler will make a pass
        int passCounter = 0; //counts the number of passes to determine how much time each possession takes
        Result result;

        Player hasBall = offense.get(0); // Step 1: PG starts with the ball
        Player passer = hasBall; //PG will default as the passer
        int posOfBall = 0; //Keeps track of the position of the player within the offense ArrayList (Different from player pos)
        int posOfPasser = 0; //Keeps track of the position of the passer within the offense ArrayList (Different from player pos)


        while(curPossession){

            if(Math.random() < passChance){

                //Decide who to pass to
                passer = hasBall;
                posOfPasser = posOfBall;
                posOfBall = determineNextPass(hasBall, offense);
                hasBall = offense.get(posOfBall);
                passCounter++;
                passChance = passChance*0.7;

            }
            else{

                //Player with the ball shoots
                result = attemptShot(passer, hasBall, defense, posOfBall, posOfPasser);

                if(result == Result.MISS || result == Result.BLOCK){

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
                    curPossession = false; //current possession ends after shot is made or ball is stolen
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

                passProb = (offense.get(i).getOffOvr() * 2 * Math.random());

                if(passProb > largestPassProb){
                    largestPassProb = passProb;
                    posToPass = i;
                }
            }

        }

        return posToPass;


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

            boxScore.addDReb(possession, posOfDefReb, isBenchIn);
            defense.get(posOfDefReb).addDefRebound();
            return -1;
        }
        else{
            //offensive  rebound
            gameLog = gameLog + offense.get(posOfOffReb).getTeamAbbr() + " " + offense.get(posOfOffReb).getName() +
                        " grabbed the offensive rebound " + clock + "\n";

            boxScore.addOReb(possession, posOfOffReb, isBenchIn);
            offense.get(posOfOffReb).addOffRebound();
            return posOfOffReb;
        }

    }

    /* Method for determining if the player makes or misses his shot
    *  1. Determines if the shot will be assisted and what the passerBonus will be (based on passer's pass rating)
    *  2. Determines the type of shot attempted (post, layup, mid, or three) using player tendencies
    *  3. Creates a Shot object and passes the appropriate shot type as a parameter
    *  4. Record the shotResult and update the score, box score, and game log
    *  Returns the result of the shot attempt (Steal, Block, Make, Miss) from the Shot object */
    public Result attemptShot(Player passer, Player shooter, ArrayList<Player> defense, int posBall, int posPass){

        Result shotResult; // records the result of the shot attempt
        int shotType; // 0 = post, 1 = layup, 2 = midrange, 3 = three
        double assistChance = (double) passer.getPassRating()/150;
        int passerBonus = 0;
        Player defender = defense.get(posBall); //The player that will be defending the shooter
        boolean isAssisted = false;

        // Chance of Assist based on passer's Pass Rating
        // A passer with a Pass Rating of 75 will have a 50% chance of assisting on the shot
        if(!passer.equals(shooter) && (Math.random() < assistChance)) {

            isAssisted = true;
            //No passer bonus for passers with rating under 70
            if (passer.getPassRating() >= 70) {

                passerBonus = (4 + ((passer.getPassRating() - 70) / 2));
            }
        }


        // Determine what kind of shot the player will attempt based on tendencies
        if(100*Math.random() < shooter.getJumperOrDrive()){
            //Player will drive
            if(100*Math.random() < shooter.getLayupOrPost()){
                //Player will Post up
                shotType = 0;

            } else{
                //Player will drive and attempt a layup
                shotType = 1;
            }
        }
        else{
            //Player will shoot jumper
            if(100*Math.random() < shooter.getMidOrThree()){
                //Player will shoot a three
                shotType = 3;
            }
            else{
                //Player will shoot a midrange jumpshot
                shotType = 2;
            }
        }

        // Attempt the shot
        Shot shot = new Shot(passerBonus, shooter, defender, shotType);
        shotResult = shot.shoot();

        // Handle Stl, Blk, TO, Made/Missed shot stats
        recordResult(shotResult, shotType, shooter, defender, posBall);

        // Check for assist
        if(shotResult == Result.MAKE && isAssisted){

            boxScore.addAst(possession, posPass, isBenchIn);
            passer.addAssist();

            gameLog = gameLog + "(assisted by " + passer.getName() + ")\n";
        }

        return shotResult;
    }

    // Helper method to handle different shot results and write to the box score, game log, and update the score
    private void recordResult(Result shotResult, int shotType, Player shooter, Player defender, int posBall){

        String shotStr = "";
        // If not a three, save the type of 2 pt shot attempt
        switch(shotType){
            case 0:
                shotStr = "post shot ";
                break;
            case 1:
                shotStr = "layup ";
                break;
            case 2:
                shotStr = "midrange jumper ";
                break;
        }

        if(shotResult == Result.STEAL){

            gameLog = gameLog + defender.getTeamAbbr() + " " + defender.getName() + " stole the ball from " +
                    shooter.getName() + " " + clock + "\n";

            boxScore.addSteal(possession, posBall, isBenchIn);
            defender.addSteal();
            shooter.addTurnover();
        }
        else if(shotResult == Result.BLOCK){

            gameLog = gameLog + defender.getTeamAbbr() + " " + defender.getName() + " blocked " + shooter.getName() +
                    " " + clock + "\n";

            if(shotType == 3){
                boxScore.add3ptMiss(possession, posBall, isBenchIn);
                shooter.add3ptMiss();
            }
            else {
                boxScore.add2ptMiss(possession, posBall, isBenchIn);
                shooter.add2ptMiss();
            }
            boxScore.addBlock(possession, posBall, isBenchIn);
            defender.addBlock();
        }
        else if(shotResult == Result.MAKE && shotType != 3){
            // 2 pt field goal made
            String shotLog = " made a " + shotStr;

            if(possession == 0) homeScore += 2;
            else awayScore += 2;

            boxScore.add2ptMake(possession, posBall, isBenchIn);
            shooter.add2ptMake();

            gameLog = gameLog + shooter.getTeamAbbr() + " " + shooter.getName() + shotLog + clock + "\n";
        }
        else if(shotResult == Result.MAKE){
            // 3 pt field goal made
            if(possession == 0) homeScore += 3;
            else awayScore += 3;

            boxScore.add3ptMake(possession, posBall, isBenchIn);
            shooter.add3ptMake();

            gameLog = gameLog + shooter.getTeamAbbr() + " " + shooter.getName() + " made a 3 pt shot " + clock + "\n";
        }
        else if(shotResult == Result.MISS && shotType != 3){
            // Missed 2 pt field goal
            String shotLog = " missed a " + shotStr;

            boxScore.add2ptMiss(possession, posBall, isBenchIn);
            shooter.add2ptMiss();

            gameLog = gameLog + shooter.getTeamAbbr() + " " + shooter.getName() + shotLog + clock + "\n";
        }
        else if(shotResult == Result.MISS){
            // Missed 3 pt field goal
            boxScore.add3ptMiss(possession, posBall, isBenchIn);
            shooter.add3ptMiss();

            gameLog = gameLog + shooter.getTeamAbbr() + " " + shooter.getName() + " missed a 3 pt shot " + clock + "\n";
        }
    }
}
