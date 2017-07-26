package ccmetz.basketballsim.Models;

/**
 * Created by ccmetz on 7/28/16.
 * <p>
 * This class will handle what happens when a player attempts to shoot the ball
 * <p>
 * Will change modifiers based on the shot type and determine the result of the shot
 */
public class Shot
{
  // Constants used for steal, block, and shot probabilities
  private final int shotMod;
  private final double stealOpp;
  private final double blockOpp;

  // Arguments passed into the Shot object
  private int passerBonus;
  private Player shooter;
  private Player defender;

  public Shot(int pBonus, Player s, Player d, int shotType)
  {
    passerBonus = pBonus;
    shooter = s;
    defender = d;

    // Set the probabilities depending on shot type
    // 0: Post shot
    // 1: Layup
    // 2: Midrange
    // 3: Three
    switch (shotType)
    {
      case 0:
        shotMod = 18;
        stealOpp = 0.10;
        blockOpp = 0.20;
        break;
      case 1:
        shotMod = 17;
        stealOpp = 0.15;
        blockOpp = 0.20;
        break;
      case 2:
        shotMod = 15;
        stealOpp = 0.05;
        blockOpp = 0.05;
        break;
      case 3:
        shotMod = 10;
        stealOpp = 0.05;
        blockOpp = 0.02;
        break;
      // Shouldn't ever get to default
      default:
        shotMod = 10;
        stealOpp = 0.10;
        blockOpp = 0.10;
        break;
    }
  }

  public Game.Result shoot()
  {
    double shotChance = shotMod + (shooter.getPostRating() / 2) - (defender.getDefenseRating() / 5);
    double stealChance = (defender.getStealRating() / 2) - (shooter.getHandleRating() / 3);
    double blockChance = (defender.getBlockRating() / 2) - (shooter.getPostRating() / 3);

    boolean blkOrStl = false; //Will change to true if steal or block occurs before shot is made/missed

    //Determine if a block or steal opportunity exists
    if (Math.random() < stealOpp)
    {
      //Steal opportunity exists - determine if defender will go for steal
      if (Math.random() < defender.getTryForSteal())
      {
        //Defender goes for steal
        if (100 * Math.random() < stealChance)
        {

          //STEAL - TURNOVER
          blkOrStl = true;
          return Game.Result.STEAL;
        }

      }
    }
    else if (Math.random() < blockOpp)
    {
      //Block opportunity exists - determine if defender will go for block
      if (Math.random() < defender.getTryForBlock())
      {
        //Defender goes for block
        if (100 * Math.random() < blockChance)
        {

          //BLOCKED
          blkOrStl = true;
          return Game.Result.BLOCK;
        }
      }
    }

    if (100 * Math.random() < shotChance + passerBonus && blkOrStl == false)
    {
      // Shot made
      return Game.Result.MAKE;
    }
    else
    {
      // Shot missed
      return Game.Result.MISS;
    }
  }
}
