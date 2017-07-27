package ccmetz.basketballsim.Helpers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ccmetz.basketballsim.Models.Player;
import ccmetz.basketballsim.Models.Team;

/**
 * Created by ccmetz on 7/26/16.
 * <p>
 * Class used for sorting Lists by certain attributes
 */
public class Sorter
{
  // Sorts a List<Player> by position (From Point Guard to Center)
  public static void sortByPosition(List<Player> list)
  {
    Collections.sort(list, new PositionComp());
  }

  // Sorts a List<Team> by Prestige (Highest to Lowest)
  public static void sortByPrestige(List<Team> list)
  {
    Collections.sort(list, new PrestigeComp());
  }

  // Sorts a List<Player> by Overall (Highest to Lowest)
  public static void sortPlayersByOverall(List<Player> list)
  {
    Collections.sort(list, new PlayerOvrComp());
  }

  // Sorts a List<Team> by Poll Score
  public static void sortByPollScore(List<Team> list)
  {
    Collections.sort(list, new TeamPollComp());
  }

  // Sorts a List<Player> by their POY score
  public static void sortByPlayerOfTheYearScore(List<Player> list)
  {
    Collections.sort(list, new POYScoreComp());
  }


  // Inner Comparator class for sorting Players by position
  static class PositionComp implements Comparator<Player>
  {
    @Override
    public int compare(Player a, Player b)
    {
      if (a.getPosition() < b.getPosition()) return -1;
      else if (a.getPosition() == b.getPosition())
        return a.getLastName().compareTo(b.getLastName());
      else return 1;
    }
  }

  // Inner Comparator class for sorting Teams by prestige and name
  static class PrestigeComp implements Comparator<Team>
  {
    @Override
    public int compare(Team a, Team b)
    {
      if (a.getProgramLevel() < b.getProgramLevel()) return 1;
      else if (a.getProgramLevel() == b.getProgramLevel())
        return a.getTeamName().compareTo(b.getTeamName());
      else return -1;
    }
  }

  // Inner Comparator class for sorting Players by overall and name
  static class PlayerOvrComp implements Comparator<Player>
  {
    @Override
    public int compare(Player a, Player b)
    {
      if (a.getOverall() < b.getOverall()) return 1;
      else if (a.getOverall() == b.getOverall()) return a.getLastName().compareTo(b.getLastName());
      else return -1;
    }
  }

  // Inner Comparator class for sorting Teams by their polling score
  static class TeamPollComp implements Comparator<Team>
  {
    @Override
    public int compare(Team a, Team b)
    {
      if (a.getPollScore() < b.getPollScore()) return 1;
      else if (a.getPollScore() == b.getPollScore()) return 0;
      else return -1;
    }
  }

  // Inner Comparator class for sorting Players by their Player of the Year score
  static class POYScoreComp implements Comparator<Player>
  {
    @Override
    public int compare(Player a, Player b)
    {
      if (a.getPOYScore() < b.getPOYScore()) return 1;
      else if (a.getPOYScore() == b.getPOYScore()) return 0;
      else return -1;
    }
  }
}
