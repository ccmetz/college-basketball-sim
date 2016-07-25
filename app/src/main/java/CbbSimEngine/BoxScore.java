package CbbSimEngine;

/**
 * Created by ccmetz on 7/25/16.
 */
public class BoxScore {

    private String homePlayers;
    private String awayPlayers;
    private String[] homeBoxScore; //String array to hold a the home team's completed box score
    private String[] awayBoxScore; //String array to hold a the away team's completed box score

    private int[][] home2pt; // 2FGM, 2FGA
    private int[][] home3pt; // 3FGM, 3FGA
    private int[][] homeReb; // OREB, DREB
    private int[][] homeSBT; // STL, BLK, TO
    private int[] homeAst; // AST

    private int[][] away2pt;
    private int[][] away3pt;
    private int[][] awayReb;
    private int[][] awaySBT;
    private int[] awayAst;

    public BoxScore(String homeNames, String awayNames){

        homePlayers = homeNames;
        awayPlayers = awayNames;

        homeBoxScore = new String[7];
        awayBoxScore = new String[7];

        home2pt = new int[5][2]; //These sizes will need to be changed to 10 once subs are added
        home3pt = new int[5][2];
        homeReb = new int[5][2];
        homeSBT = new int[5][3];
        homeAst = new int[5];

        away2pt = new int[5][2];
        away3pt = new int[5][2];
        awayReb = new int[5][2];
        awaySBT = new int[5][3];
        awayAst = new int[5];

    }

    public String[] getHomeBoxScore(){
        return homeBoxScore;
    }

    public String[] getAwayBoxScore(){
        return awayBoxScore;
    }

    public void createBoxScore(){

        createHomeBoxScore();
        createAwayBoxScore();
    }

    private void createHomeBoxScore(){

        homeBoxScore[0] = "\n" + homePlayers;

        homeBoxScore[1] = "2FGM/A" + "\n" + home2pt[0][0] + "  " + home2pt[0][1] + "\n" +
                home2pt[1][0] + "  " + home2pt[1][1] + "\n" +
                home2pt[2][0] + "  " + home2pt[2][1] + "\n" +
                home2pt[3][0] + "  " + home2pt[3][1] + "\n" +
                home2pt[4][0] + "  " + home2pt[4][1] + "\n\n";

        homeBoxScore[2] = "3FGM/A" + "\n" + home3pt[0][0] + "  " + home3pt[0][1] + "\n" +
                home3pt[1][0] + "  " + home3pt[1][1] + "\n" +
                home3pt[2][0] + "  " + home3pt[2][1] + "\n" +
                home3pt[3][0] + "  " + home3pt[3][1] + "\n" +
                home3pt[4][0] + "  " + home3pt[4][1] + "\n\n";

        homeBoxScore[3] = "O/DREB" + "\n" + homeReb[0][0] + "  " + homeReb[0][1] + "\n" +
                homeReb[1][0] + "  " + homeReb[1][1] + "\n" +
                homeReb[2][0] + "  " + homeReb[2][1] + "\n" +
                homeReb[3][0] + "  " + homeReb[3][1] + "\n" +
                homeReb[4][0] + "  " + homeReb[4][1] + "\n\n";

        homeBoxScore[4] = "\n" + homePlayers;

        homeBoxScore[5] = "Stl/Blk/TO" + "\n" + homeSBT[0][0] + "   " + homeSBT[0][1] + "   " + homeSBT[0][2] + "\n" +
                homeSBT[1][0] + "   " + homeSBT[1][1] + "   " + homeSBT[1][2] + "\n" +
                homeSBT[2][0] + "   " + homeSBT[2][1] + "   " + homeSBT[2][2] + "\n" +
                homeSBT[3][0] + "   " + homeSBT[3][1] + "   " + homeSBT[3][2] + "\n" +
                homeSBT[4][0] + "   " + homeSBT[4][1] + "   " + homeSBT[4][2] + "\n\n";

        homeBoxScore[6] = "AST" + "\n" + homeAst[0] + "\n" + homeAst[1] + "\n" + homeAst[2] + "\n" +
                homeAst[3] + "\n" + homeAst[4] + "\n\n";
    }

    private void createAwayBoxScore(){

        awayBoxScore[0] = "\n" + awayPlayers;

        awayBoxScore[1] = "2FGM/A" + "\n" + away2pt[0][0] + "  " + away2pt[0][1] + "\n" +
                away2pt[1][0] + "  " + away2pt[1][1] + "\n" +
                away2pt[2][0] + "  " + away2pt[2][1] + "\n" +
                away2pt[3][0] + "  " + away2pt[3][1] + "\n" +
                away2pt[4][0] + "  " + away2pt[4][1] + "\n\n";

        awayBoxScore[2] = "3FGM/A" + "\n" + away3pt[0][0] + "  " + away3pt[0][1] + "\n" +
                away3pt[1][0] + "  " + away3pt[1][1] + "\n" +
                away3pt[2][0] + "  " + away3pt[2][1] + "\n" +
                away3pt[3][0] + "  " + away3pt[3][1] + "\n" +
                away3pt[4][0] + "  " + away3pt[4][1] + "\n\n";

        awayBoxScore[3] = "O/DREB" + "\n" + awayReb[0][0] + "  " + awayReb[0][1] + "\n" +
                awayReb[1][0] + "  " + awayReb[1][1] + "\n" +
                awayReb[2][0] + "  " + awayReb[2][1] + "\n" +
                awayReb[3][0] + "  " + awayReb[3][1] + "\n" +
                awayReb[4][0] + "  " + awayReb[4][1] + "\n\n";

        awayBoxScore[4] = "\n" + awayPlayers;

        awayBoxScore[5] = "Stl/Blk/TO" + "\n" + awaySBT[0][0] + "   " + awaySBT[0][1] + "   " + awaySBT[0][2] + "\n" +
                awaySBT[1][0] + "   " + awaySBT[1][1] + "   " + awaySBT[1][2] + "\n" +
                awaySBT[2][0] + "   " + awaySBT[2][1] + "   " + awaySBT[2][2] + "\n" +
                awaySBT[3][0] + "   " + awaySBT[3][1] + "   " + awaySBT[3][2] + "\n" +
                awaySBT[4][0] + "   " + awaySBT[4][1] + "   " + awaySBT[4][2] + "\n\n";

        awayBoxScore[6] = "AST" + "\n" + awayAst[0] + "\n" + awayAst[1] + "\n" + awayAst[2] + "\n" +
                awayAst[3] + "\n" + awayAst[4] + "\n\n";
    }

    public void add2ptMake(int possession, int player){

        if(possession == 0) {
            home2pt[player][0]++;
            home2pt[player][1]++;
        }
        else if(possession == 1){
            away2pt[player][0]++;
            away2pt[player][1]++;
        }
    }

    public void add2ptMiss(int possession, int player){

        if(possession == 0) home2pt[player][1]++;
        else if(possession == 1) away2pt[player][1]++;

    }

    public void add3ptMake(int possession, int player){

        if(possession == 0){
            home3pt[player][0]++;
            home3pt[player][1]++;
        }
        else if(possession == 1){
            away3pt[player][0]++;
            away3pt[player][1]++;
        }
    }

    public void add3ptMiss(int possession, int player){

        if(possession == 0) home3pt[player][1]++;
        else if(possession == 1) away3pt[player][1]++;
    }

    public void addOReb(int possession, int player){

        if(possession == 0) homeReb[player][0]++;
        else if(possession == 1) awayReb[player][0]++;
    }

    public void addDReb(int possession, int player){

        // The defensive rebound goes to the team that did not currently have possession
        if(possession == 0) awayReb[player][1]++;
        else if(possession == 1) homeReb[player][1]++;
    }

    public void addSteal(int possession, int player){

        if(possession == 0){
            homeSBT[player][2]++; //Add TO
            awaySBT[player][0]++; //Add steal
        }
        else if(possession == 1){
            awaySBT[player][2]++; //Add TO
            homeSBT[player][0]++; //Add steal
        }
    }

    public void addBlock(int possession, int player){

        if(possession == 0) awaySBT[player][1]++;
        else if(possession == 1) homeSBT[player][1]++;
    }

    public void addUnforcedTO(){

        // Will add unforced Turnovers once those are added to the game sim logic
    }

    public void addAst(int possession, int player){

        if(possession == 0) homeAst[player]++;
        else if(possession == 1) awayAst[player]++;
    }



}
