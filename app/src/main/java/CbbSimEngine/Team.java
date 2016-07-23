package CbbSimEngine;

import java.util.ArrayList;

/**
 * Created by ccmetz on 4/3/16.
 *
 * Team class that will contain Players and team stats
 */
public class Team {

    private String name;
    private String conference;
    private String abbreviation;
    private int programLevel; //this will determine the caliber of players you can recruit
    private int teamOverall;

    //Used to keep track of team's record
    private int wins;
    private int losses;

    private League league;

    private ArrayList<Player> pgList; //Lists that contain the players that belong to this team
    private ArrayList<Player> sgList;
    private ArrayList<Player> sfList;
    private ArrayList<Player> pfList;
    private ArrayList<Player> cList;
    private ArrayList<Player> rosterList;
    private boolean userControl; //True or false that indicates if the user has chosen to control this team

    private ArrayList<Game> schedule;
    private ArrayList<String> scheduleList;
    public ArrayList<String> homeGames;
    public ArrayList<String> awayGames;

    //Creates new Team and assigns it to a conference
    public Team(String nm, String conf, String abbr, int progLvl, League l){

        name = nm;
        conference = conf;
        abbreviation = abbr;
        userControl = false;
        programLevel = progLvl;

        league = l;

        wins = 0;
        losses = 0;

        rosterList = new ArrayList<Player>();
        pgList = new ArrayList<Player>();
        sgList = new ArrayList<Player>();
        sfList = new ArrayList<Player>();
        pfList = new ArrayList<Player>();
        cList = new ArrayList<Player>();

        generateRoster(2, 3, 3, 2, 2); //12 total scholarship players

        calcTeamOverall();

        schedule = new ArrayList<Game>();
        scheduleList = new ArrayList<String>();
        homeGames = new ArrayList<String>();
        awayGames = new ArrayList<String>();

    }

    public String getTeamName(){

        return name;
    }

    public int getProgramLevel(){
        return programLevel;
    }

    public ArrayList<Player> getRoster(){

        return rosterList;
    }

    // Will randomly determine what level of recruit will be added to the team
    public int determineRecruitStars(){

        double random = Math.random();
        double baseRating = (double) programLevel/20; // Most likely recruit will be stars = (int) baseRating
        double comparison = baseRating - (programLevel/20);
        int stars = (int) baseRating;

        // Check for One and Done chance
        if(programLevel >= 90){

            double oneAndDoneChance = 0.10;

            if(programLevel >=95){

                oneAndDoneChance = 0.20;
            }

            if(Math.random() < oneAndDoneChance) {

                return 6;
            }
        }

        // If not recruit != one and done, determine stars rating for normal recruit
        if(comparison >= 0.75){

            if(random > 0.75) stars = (int) baseRating + 1;
            else if(random < 0.05) stars = (int) baseRating - 1;
        }
        else if(comparison >= 0.5){

            if(random > 0.85) stars = (int) baseRating + 1;
            else if(random < 0.10) stars = (int) baseRating - 1;
        }
        else{

            if(random > 0.90) stars = (int) baseRating + 1;
            else if(random < 0.15) stars = (int) baseRating - 1;
        }

        return stars;

    }

    public void generateRoster(int numPGs, int numSGs, int numSFs, int numPFs, int numCs) {

        for (int i = 0; i < numPGs; i++) {

            pgList.add(i, new Player(league.getRandomFirstName(), league.getRandomLastName(), 1, (int) (4 * Math.random()),
                    determineRecruitStars(), (int) (4 * Math.random()), this));

        }

        sortList(pgList);

        for (int i = 0; i < numSGs; i++) {

            sgList.add(i, new Player(league.getRandomFirstName(), league.getRandomLastName(), 2, (int) (4 * Math.random()),
                    determineRecruitStars(), (int) (4 * Math.random()), this));

        }

        sortList(sgList);

        for (int i = 0; i < numSFs; i++) {

            sfList.add(i, new Player(league.getRandomFirstName(), league.getRandomLastName(), 3, (int) (4 * Math.random()),
                    determineRecruitStars(), (int) (4 * Math.random()), this));

        }

        sortList(sfList);

        for (int i = 0; i < numPFs; i++) {

            pfList.add(i, new Player(league.getRandomFirstName(), league.getRandomLastName(), 4, (int) (4 * Math.random()),
                    determineRecruitStars(), (int) (4 * Math.random()), this));

        }

        sortList(pfList);

        for (int i = 0; i < numCs; i++) {

            cList.add(i, new Player(league.getRandomFirstName(), league.getRandomLastName(), 5, (int) (4 * Math.random()),
                    determineRecruitStars(), (int) (4 * Math.random()), this));

        }

        sortList(cList);

        // Create the rosterList that will be displayed on the Lineup tab in MainActivity
        createRosterList();
    }

    public void setUserControl(boolean control){

        userControl = control;
    }

    public boolean checkUserControl(){

        return userControl;
    }

    public String getAbbr(){

        return abbreviation;
    }


    public String displayFullTeamString(){

        return name + " - Prestige: " + programLevel;
    }

    public ArrayList<Game> getSchedule(){

        return schedule;
    }

    // Method to sort Player lists by overall rating
    public void sortList(ArrayList<Player> list){

        Player lowerPlayer;
        Player higherPlayer;

        for(int i = 0; i < list.size(); i++) {

            for (int j = 0; j < list.size(); j++) {

                // Check if at the end of the list
                if (j + 1 < list.size()) {

                    if (list.get(j).getOverall() < list.get(j + 1).getOverall()) {

                        // Flip Players
                        lowerPlayer = list.get(j);
                        higherPlayer = list.get(j + 1);
                        list.set(j, higherPlayer);
                        list.set(j + 1, lowerPlayer);

                    }
                }
            }
        }
    }

    // This method will create and sort the rosterList by starters and bench players
    public void createRosterList(){

        ArrayList<Player> listToBeSorted = new ArrayList<Player>();

        //Add Starters first (highest overall)
        rosterList.add(pgList.get(0));
        rosterList.add(sgList.get(0));
        rosterList.add(sfList.get(0));
        rosterList.add(pfList.get(0));
        rosterList.add(cList.get(0));

        for(int i = 1; i < pgList.size(); i++){
            listToBeSorted.add(pgList.get(i));
        }

        for(int i = 1; i < sgList.size(); i++){
            listToBeSorted.add(sgList.get(i));
        }

        for(int i = 1; i < sfList.size(); i++){
            listToBeSorted.add(sfList.get(i));
        }

        for(int i = 1; i < pfList.size(); i++){
            listToBeSorted.add(pfList.get(i));
        }

        for(int i = 1; i < cList.size(); i++){
            listToBeSorted.add(cList.get(i));
        }

        // Sort bench players by overall and add them to the rosterList
        sortList(listToBeSorted);
        for(int i = 0; i < listToBeSorted.size(); i++){

            rosterList.add(listToBeSorted.get(i));
        }

        // Set best players at their positions as STARTER
        for(int i = 0; i < 5; i++){

            rosterList.get(i).setPlayerRole(Player.Role.STARTER);
        }

        // Set the next 5 best players to ROLEPLAYER
        for(int i = 5; i < 10; i++){

            rosterList.get(i).setPlayerRole(Player.Role.ROLEPLAYER);
        }
    }

    public String getRecord(){

        return wins + " - " + losses;
    }

    // Calculates the teams overall based on the starters and first 5 bench players
    public void calcTeamOverall(){

        int starterScore = 0;
        int benchScore = 0;

        // Give a score for the top 5 position players
        for(int i = 0; i < 5; i++){

            starterScore = rosterList.get(i).getOverall() + starterScore;
        }
        // Give a score for the next 5 best bench players
        for(int i = 5; i < 10; i++){

            benchScore = rosterList.get(i).getOverall() + benchScore;
        }

        starterScore = starterScore*3; //starters will be 3 times as important than the bench

        teamOverall = starterScore + benchScore;
    }

    public int getTeamOverall(){

        return teamOverall;
    }

    //Updates a string ArrayList of the team's schedule
    public void updateScheduleList(){

        scheduleList.clear();

        for(int i = 0; i < schedule.size(); i++){

            if(abbreviation.equals(schedule.get(i).getHomeTeam().getAbbr())){

                scheduleList.add(schedule.get(i).getAwayTeam().getTeamName() + "/" + schedule.get(i).getGameResult(true));
            }
            else if(abbreviation.equals(schedule.get(i).getAwayTeam().getAbbr())){

                scheduleList.add("@" + schedule.get(i).getHomeTeam().getTeamName() + "/" + schedule.get(i).getGameResult(false));
            }
        }
    }

    // Returns the ArrayList of Strings that fills the items on the team's schedule listview
    public ArrayList<String> getScheduleList(){

        return scheduleList;
    }

    // Returns the ArrayList of Game objects that are on this team's schedule
    public ArrayList<Game> getGameArrayList(){

        return schedule;
    }

    public void addWin(){
        wins++;
    }

    public void addLoss(){
        losses++;
    }

    // Returns an ArrayList of the players that play a specific position on the team
    public ArrayList<Player> getPositionList(int pos){

        //ArrayList<Player> playerList = new ArrayList<Player>();
        //int counter = 0;

        switch (pos){

            case 0:
                return pgList;

            case 1:
                return sgList;

            case 2:
                return sfList;

            case 3:
                return pfList;

            case 4:
              /*  for(int i = 0; i < rosterList.size(); i++){
                    if(rosterList.get(i).getPosition() == 5){
                        playerList.add(rosterList.get(i));
                    }
                }
                break; */
                return cList;
        }

        //return playerList;
        return pgList;

    }

    // This method is called when the user makes changes to their team's lineup
    // The rosterList is re-sorted according to any new starters or role players
    public void updateTeamLineup(){

        ArrayList<Player> roleList = new ArrayList<Player>();
        ArrayList<Player> benchList = new ArrayList<Player>();

        updateStarters(pgList);
        updateStarters(sgList);
        updateStarters(sfList);
        updateStarters(pfList);
        updateStarters(cList);

        rosterList.clear();

        // Re-add the starters
        rosterList.add(pgList.get(0));
        rosterList.add(sgList.get(0));
        rosterList.add(sfList.get(0));
        rosterList.add(pfList.get(0));
        rosterList.add(cList.get(0));

        // Populate the role player and bench lists
        for(int i = 1; i < pgList.size(); i++){

            if(pgList.get(i).getPlayerRole() == Player.Role.ROLEPLAYER) {
                roleList.add(pgList.get(i));
            }
            else if(pgList.get(i).getPlayerRole() == Player.Role.BENCH){
                benchList.add(pgList.get(i));
            }
        }

        for(int i = 1; i < sgList.size(); i++){

            if(sgList.get(i).getPlayerRole() == Player.Role.ROLEPLAYER) {
                roleList.add(sgList.get(i));
            }
            else if(sgList.get(i).getPlayerRole() == Player.Role.BENCH){
                benchList.add(sgList.get(i));
            }
        }

        for(int i = 1; i < sfList.size(); i++){

            if(sfList.get(i).getPlayerRole() == Player.Role.ROLEPLAYER) {
                roleList.add(sfList.get(i));
            }
            else if(sfList.get(i).getPlayerRole() == Player.Role.BENCH){
                benchList.add(sfList.get(i));
            }
        }

        for(int i = 1; i < pfList.size(); i++){

            if(pfList.get(i).getPlayerRole() == Player.Role.ROLEPLAYER) {
                roleList.add(pfList.get(i));
            }
            else if(pfList.get(i).getPlayerRole() == Player.Role.BENCH){
                benchList.add(pfList.get(i));
            }
        }

        for(int i = 1; i < cList.size(); i++){

            if(cList.get(i).getPlayerRole() == Player.Role.ROLEPLAYER) {
                roleList.add(cList.get(i));
            }
            else if(cList.get(i).getPlayerRole() == Player.Role.BENCH){
                benchList.add(cList.get(i));
            }
        }

        sortList(roleList);
        sortList(benchList);

        for(int i = 0; i < roleList.size(); i++){

            rosterList.add(roleList.get(i));
        }

        for(int i = 0; i < benchList.size(); i++){

            rosterList.add(benchList.get(i));
        }

    }

    // This method is called inside of updateTeamLineup
    // The starters for all 5 position groups will be repositioned to the front
    // of their respective ArrayLists
    public void updateStarters(ArrayList<Player> list){


        for(int i = 0; i < list.size(); i++){

            if(list.get(i).getPlayerRole() == Player.Role.STARTER && i != 0){

                //Swap the new starter with the old starter
                Player temp = list.get(0);
                list.set(0, list.get(i));
                list.set(i, temp);
            }

        }

        //Resort the list with role players ahead of bench players
        for(int i = 1; i < list.size() - 1; i++){

            if(list.get(i).getPlayerRole() == Player.Role.BENCH){

                //Check for any role players below it in the list
                for(int j = i + 1; j < list.size(); j++){

                    if(list.get(j).getPlayerRole() == Player.Role.ROLEPLAYER){
                        //Swap
                        Player temp = list.get(i);
                        list.set(i, list.get(j));
                        list.set(j, temp);

                    }
                }
            }
        }

    }

}
