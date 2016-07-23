package CbbSimEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ccmetz on 4/4/16.
 *
 * Conference class that will contain Teams and conference stats
 */
public class Conference {

    private String name; //name of the conference
    private League league; //League that the conference belongs to
    private List<Team> teamList; //List of the teams that belong to this conference


    public Conference(String nm, League lg){

        name = nm;
        league = lg;
        teamList = new ArrayList<Team>();

    }

    public String getConfName(){

        return name;
    }

    public List<Team> getTeams(){

        return teamList;
    }

    // Creates the schedules for all teams in the conference and calls the method for
    // setting up each team's individual schedule string list
    public void setConfSchedule(){

        //ArrayList<Team> teams = teamList;
        Collections.shuffle(teamList); //Shuffle teams


        for(int week = 0; week < 2*teamList.size()-2; week++){

            for(int gameNum = 0; gameNum < teamList.size()/2; gameNum++){

                Team a = teamList.get((week + gameNum) % 9);
                Team b;

                if(gameNum == 0){
                    b = teamList.get(9);
                }
                else{
                    b = teamList.get((9 - gameNum + week) % 9);
                }

                Game game;

                if(!a.homeGames.contains(b.getAbbr())){

                    game = new Game(a, b);
                    a.homeGames.add(b.getAbbr());
                    b.awayGames.add(a.getAbbr());

                    a.getSchedule().add(game);
                    b.getSchedule().add(game);
                }
                else if(!a.awayGames.contains(b.getAbbr())){

                    game = new Game(b, a);
                    a.awayGames.add(b.getAbbr());
                    b.homeGames.add(a.getAbbr());

                    a.getSchedule().add(game);
                    b.getSchedule().add(game);
                }


            }
        }

        Collections.sort(teamList, new PrestigeComp());

        //Call each team's setScheduleStringList method now that their schedules have been created
        for(int i = 0; i < teamList.size(); i++) {

            teamList.get(i).updateScheduleList();
        }

    }

    // Inner Comparator class for sorting Teams by prestige and name
    class PrestigeComp implements Comparator<Team> {

        @Override
        public int compare(Team a, Team b){
            if(a.getProgramLevel() < b.getProgramLevel()) return 1;
            else if(a.getProgramLevel() == b.getProgramLevel()) return a.getTeamName().compareTo(b.getTeamName());
            else return -1;
        }
    }

}
