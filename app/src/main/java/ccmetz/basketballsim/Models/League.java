package ccmetz.basketballsim.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ccmetz on 4/4/16.
 *
 * League class that will contain the conferences, teams, and players for the league
 * that the user will be playing in.
 */
public class League implements Serializable {

    // String arrays that contain the first and last names that will be used for players throughout the league
    final private String[] firstNames = {"Daniel", "Isaiah", "Devyn", "Roy", "Aaron", "Bryan", "Demarcus", "Stephen", "Lebron",
                                        "Michael", "Eric", "Andrew", "Anthony", "Jared", "Eli", "John", "Ahmad", "Christian",
                                        "Josh", "Connor", "Jack", "Adam", "Mike", "Micah", "Jordy", "Kendrick", "Derrick",
                                        "Dwayne", "Melo", "Greg", "Peter", "Trevor", "Patrick", "Kyrie", "Kevin", "Tristan",
                                        "Brandon", "Bobby", "Ricky", "Dion", "Demarre", "Dirk"};
    final private String[] lastNames = {"Cook", "White", "Marble", "Towns", "Nelson", "Rose", "James", "Williams", "Jordan",
                                        "Miller", "Randle", "Green", "Thompson", "Wagner", "Brown", "Smith", "Murray", "Ulis",
                                        "Booker", "Thomas", "Bryant", "McDonald", "Irving", "Parker", "Hart", "Lowry", "Hill",
                                        "Young", "Carter", "Manning", "Jok", "Valentine", "Baldwin", "Simmons", "Durant", "King",
                                        "Allen", "Parsons", "Forbes", "Taylor", "Harden", "Nowitzki"};

    private ArrayList<String> posList; //List of basketball positions
    private ArrayList<Conference> confList; //List that contains the conferences that belong to this league
    private ArrayList<Team> leagueTeamList; //List that contains all of the teams in league
    private int currentWeek; //Starts at 0
    private int currentSeason; //Starts at 2016
    private Team userTeam;


    public League(){

        currentSeason = 2016;
        currentWeek = 0;

        posList = new ArrayList<String>();
        posList.add("PG");
        posList.add("SG");
        posList.add("SF");
        posList.add("PF");
        posList.add("C");

        confList = new ArrayList<Conference>();
        confList.add(new Conference("BIG 10", this));
        confList.add(new Conference("BIG 12", this));
        confList.add(new Conference("SEC", this));
        confList.add(new Conference("ACC", this));

        leagueTeamList = new ArrayList<Team>();

        /** Add teams to the conferences - keeping them to 10 teams each for now **/
        // BIG 10
        confList.get(0).getTeams().add(new Team("MICHIGAN ST", "BIG 10", "MSU", 85, this));
        confList.get(0).getTeams().add(new Team("MICHIGAN", "BIG 10", "MIC", 80, this));
        confList.get(0).getTeams().add(new Team("OHIO ST", "BIG 10", "OSU", 80, this));
        confList.get(0).getTeams().add(new Team("WISCONSIN", "BIG 10", "WIS", 75, this));
        confList.get(0).getTeams().add(new Team("IOWA", "BIG 10", "IA", 70, this));
        confList.get(0).getTeams().add(new Team("PURDUE", "BIG 10", "PU", 70, this));
        confList.get(0).getTeams().add(new Team("MINNESOTA", "BIG 10", "MN", 65, this));
        confList.get(0).getTeams().add(new Team("ILLINOIS", "BIG 10", "IL", 60, this));
        confList.get(0).getTeams().add(new Team("NEBRASKA", "BIG 10", "NE", 55, this));
        confList.get(0).getTeams().add(new Team("RUTGERS", "BIG 10", "RU", 50, this));

        // BIG 12
        confList.get(1).getTeams().add(new Team("KANSAS", "BIG 12", "KS", 90, this));
        confList.get(1).getTeams().add(new Team("IOWA ST", "BIG 12", "ISU", 80, this));
        confList.get(1).getTeams().add(new Team("OKLAHOMA", "BIG 12", "OK", 80, this));
        confList.get(1).getTeams().add(new Team("TEXAS", "BIG 12", "TX", 75, this));
        confList.get(1).getTeams().add(new Team("Baylor", "BIG 12", "BU", 70, this));
        confList.get(1).getTeams().add(new Team("OKLAHOMA ST", "BIG 12", "OKS", 70, this));
        confList.get(1).getTeams().add(new Team("KANSAS ST", "BIG 12", "KSU", 65, this));
        confList.get(1).getTeams().add(new Team("WEST VIRGINIA", "BIG 12", "WV", 65, this));
        confList.get(1).getTeams().add(new Team("TEXAS TECH", "BIG 12", "TT", 60, this));
        confList.get(1).getTeams().add(new Team("TEXAS CHRISTIAN", "BIG 12", "TCU", 50, this));

        // SEC
        confList.get(2).getTeams().add(new Team("KENTUCKY", "SEC", "KY", 95, this));
        confList.get(2).getTeams().add(new Team("FLORIDA", "SEC", "FL", 80, this));
        confList.get(2).getTeams().add(new Team("LOUISIANA ST", "SEC", "LSU", 70, this));
        confList.get(2).getTeams().add(new Team("SOUTH CAROLINA", "SEC", "SC", 70, this));
        confList.get(2).getTeams().add(new Team("TENNESSEE", "SEC", "TENN", 70, this));
        confList.get(2).getTeams().add(new Team("VANDERBILT", "SEC", "VU", 70, this));
        confList.get(2).getTeams().add(new Team("ARKANSAS", "SEC", "AK", 65, this));
        confList.get(2).getTeams().add(new Team("GEORGIA", "SEC", "GU", 65, this));
        confList.get(2).getTeams().add(new Team("ALABAMA", "SEC", "AL", 55, this));
        confList.get(2).getTeams().add(new Team("AUBURN", "SEC", "AU", 50, this));

        // ACC
        confList.get(3).getTeams().add(new Team("DUKE", "ACC", "DUKE", 90, this));
        confList.get(3).getTeams().add(new Team("NORTH CAROLINA", "ACC", "UNC", 90, this));
        confList.get(3).getTeams().add(new Team("LOUISVILLE", "ACC", "LOU", 80, this));
        confList.get(3).getTeams().add(new Team("MIAMI", "ACC", "MIA", 75, this));
        confList.get(3).getTeams().add(new Team("SYRACUSE", "ACC", "SYR", 75, this));
        confList.get(3).getTeams().add(new Team("VIRGINIA", "ACC", "UVA", 70, this));
        confList.get(3).getTeams().add(new Team("FLORIDA ST", "ACC", "FSU", 65, this));
        confList.get(3).getTeams().add(new Team("GEORGIA TECH", "ACC", "GT", 65, this));
        confList.get(3).getTeams().add(new Team("NORTH CAROLINA ST", "ACC", "NCST", 60, this));
        confList.get(3).getTeams().add(new Team("BOSTON COLLEGE", "ACC", "BC", 50, this));

        //Populate the leagueTeamList
        for(int i = 0; i < confList.size(); i++){
            for(int j = 0; j < confList.get(i).getTeams().size(); j++){

                leagueTeamList.add(confList.get(i).getTeams().get(j));
            }
        }

        // Make the league schedule
        setTeamSchedules();

    }

    // This method will be responsible for calling all of the simGame methods for every game during the currentWeek
    public void simCurrentWeek(){

        if(currentWeek < leagueTeamList.get(0).getSchedule().size()) {

            for (int i = 0; i < leagueTeamList.size(); i++) {

                leagueTeamList.get(i).getSchedule().get(currentWeek).simGame();
                leagueTeamList.get(i).updateScheduleList(); //Recreate the schedule list with updated results
            }
            currentWeek++;
        }
    }

    public int getCurrentSeason(){

        return currentSeason;
    }

    public int getCurrentWeek(){

        return currentWeek;
    }

    public List<Conference> getConferences(){

        return confList;
    }
/*
    public Team getUserTeam(){

        //Will be used when looping through all of the league teams
        Team currentTeam = confList.get(0).getTeams().get(0); //defaults to 1st team

        for(int i = 0; i < confList.size(); i++){

            for(int j = 0; j < confList.get(i).getTeams().size(); j++){

                currentTeam = confList.get(i).getTeams().get(j);

                if(currentTeam.checkUserControl()){

                    //Exit the for loops
                    i = confList.size();
                    j = confList.get(i).getTeams().size();
                }

            }
        }

        return currentTeam;
    }
*/
    // Method for setting the schedules up for the teams each season
    public void setTeamSchedules(){

        for(int i = 0; i < confList.size(); i++){

            confList.get(i).setConfSchedule();
        }
    }

    // Methods for generating a first and last name
    public String getRandomFirstName(){

        Random r = new Random();

        return firstNames[r.nextInt(firstNames.length)];
    }

    public String getRandomLastName(){

        Random r = new Random();

        return lastNames[r.nextInt(lastNames.length)];
    }

    public List<String> getPosList(){

        return posList;
    }

    public void setUserTeam(Team userTeam)
    {
        this.userTeam = userTeam;
    }

    public Team getUserTeam()
    {
        return userTeam;
    }

    public void calcPreseasonTeamRankings(){

        //Calculate preseason rankings based on team overalls (maybe incorporate program level?)
        //Need to decide on what kind of sort to use: Merge? Bubble? Something else?
    }
}
