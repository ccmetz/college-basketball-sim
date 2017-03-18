package ccmetz.basketballsim.Models;

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
    private int[][] homePtReb; //Total PTs, REBs
    private int[] homeAst; // AST

    private int[][] away2pt;
    private int[][] away3pt;
    private int[][] awayReb;
    private int[][] awaySBT;
    private int[][] awayPtReb;
    private int[] awayAst;

    private int posMod; //Will change from 0 to 5 depending on if the stats should be added
                            // to a starter or a role player

    public BoxScore(String homeNames, String awayNames){

        homePlayers = homeNames;
        awayPlayers = awayNames;

        homeBoxScore = new String[8];
        awayBoxScore = new String[8];

        home2pt = new int[10][2]; //These sizes will need to be changed to 10 once subs are added
        home3pt = new int[10][2];
        homeReb = new int[10][2];
        homeSBT = new int[10][3];
        homePtReb = new int[10][2];
        homeAst = new int[10];

        away2pt = new int[10][2];
        away3pt = new int[10][2];
        awayReb = new int[10][2];
        awaySBT = new int[10][3];
        awayPtReb = new int[10][2];
        awayAst = new int[10];

        posMod = 0;

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
                home2pt[4][0] + "  " + home2pt[4][1] + "\n" +
                home2pt[5][0] + "  " + home2pt[5][1] + "\n" +
                home2pt[6][0] + "  " + home2pt[6][1] + "\n" +
                home2pt[7][0] + "  " + home2pt[7][1] + "\n" +
                home2pt[8][0] + "  " + home2pt[8][1] + "\n" +
                home2pt[9][0] + "  " + home2pt[9][1] + "\n\n";

        homeBoxScore[2] = "3FGM/A" + "\n" + home3pt[0][0] + "  " + home3pt[0][1] + "\n" +
                home3pt[1][0] + "  " + home3pt[1][1] + "\n" +
                home3pt[2][0] + "  " + home3pt[2][1] + "\n" +
                home3pt[3][0] + "  " + home3pt[3][1] + "\n" +
                home3pt[4][0] + "  " + home3pt[4][1] + "\n" +
                home3pt[5][0] + "  " + home3pt[5][1] + "\n" +
                home3pt[6][0] + "  " + home3pt[6][1] + "\n" +
                home3pt[7][0] + "  " + home3pt[7][1] + "\n" +
                home3pt[8][0] + "  " + home3pt[8][1] + "\n" +
                home3pt[9][0] + "  " + home3pt[9][1] + "\n\n";

        homeBoxScore[3] = "O/DREB" + "\n" + homeReb[0][0] + "  " + homeReb[0][1] + "\n" +
                homeReb[1][0] + "  " + homeReb[1][1] + "\n" +
                homeReb[2][0] + "  " + homeReb[2][1] + "\n" +
                homeReb[3][0] + "  " + homeReb[3][1] + "\n" +
                homeReb[4][0] + "  " + homeReb[4][1] + "\n" +
                homeReb[5][0] + "  " + homeReb[5][1] + "\n" +
                homeReb[6][0] + "  " + homeReb[6][1] + "\n" +
                homeReb[7][0] + "  " + homeReb[7][1] + "\n" +
                homeReb[8][0] + "  " + homeReb[8][1] + "\n" +
                homeReb[9][0] + "  " + homeReb[9][1] + "\n\n";

        homeBoxScore[4] = "\n" + homePlayers;

        homeBoxScore[5] = "Pts/Rebs" + "\n" + homePtReb[0][0] + "  " + homePtReb[0][1] + "\n" +
                homePtReb[1][0] + "  " + homePtReb[1][1] + "\n" +
                homePtReb[2][0] + "  " + homePtReb[2][1] + "\n" +
                homePtReb[3][0] + "  " + homePtReb[3][1] + "\n" +
                homePtReb[4][0] + "  " + homePtReb[4][1] + "\n" +
                homePtReb[5][0] + "  " + homePtReb[5][1] + "\n" +
                homePtReb[6][0] + "  " + homePtReb[6][1] + "\n" +
                homePtReb[7][0] + "  " + homePtReb[7][1] + "\n" +
                homePtReb[8][0] + "  " + homePtReb[8][1] + "\n" +
                homePtReb[9][0] + "  " + homePtReb[9][1] + "\n\n";

        homeBoxScore[6] = "AST" + "\n" + homeAst[0] + "\n" + homeAst[1] + "\n" + homeAst[2] + "\n" +
                homeAst[3] + "\n" + homeAst[4] + "\n" + homeAst[5] + "\n" + homeAst[6] + "\n" +
                homeAst[7] + "\n" + homeAst[8] + "\n" + homeAst[9] + "\n\n";

        homeBoxScore[7] = "Stl/Blk/TO" + "\n" + homeSBT[0][0] + "   " + homeSBT[0][1] + "   " + homeSBT[0][2] + "\n" +
                homeSBT[1][0] + "   " + homeSBT[1][1] + "   " + homeSBT[1][2] + "\n" +
                homeSBT[2][0] + "   " + homeSBT[2][1] + "   " + homeSBT[2][2] + "\n" +
                homeSBT[3][0] + "   " + homeSBT[3][1] + "   " + homeSBT[3][2] + "\n" +
                homeSBT[4][0] + "   " + homeSBT[4][1] + "   " + homeSBT[4][2] + "\n" +
                homeSBT[5][0] + "   " + homeSBT[5][1] + "   " + homeSBT[5][2] + "\n" +
                homeSBT[6][0] + "   " + homeSBT[6][1] + "   " + homeSBT[6][2] + "\n" +
                homeSBT[7][0] + "   " + homeSBT[7][1] + "   " + homeSBT[7][2] + "\n" +
                homeSBT[8][0] + "   " + homeSBT[8][1] + "   " + homeSBT[8][2] + "\n" +
                homeSBT[9][0] + "   " + homeSBT[9][1] + "   " + homeSBT[9][2] + "\n\n";
    }

    private void createAwayBoxScore(){

        awayBoxScore[0] = "\n" + awayPlayers;

        awayBoxScore[1] = "2FGM/A" + "\n" + away2pt[0][0] + "  " + away2pt[0][1] + "\n" +
                away2pt[1][0] + "  " + away2pt[1][1] + "\n" +
                away2pt[2][0] + "  " + away2pt[2][1] + "\n" +
                away2pt[3][0] + "  " + away2pt[3][1] + "\n" +
                away2pt[4][0] + "  " + away2pt[4][1] + "\n" +
                away2pt[5][0] + "  " + away2pt[5][1] + "\n" +
                away2pt[6][0] + "  " + away2pt[6][1] + "\n" +
                away2pt[7][0] + "  " + away2pt[7][1] + "\n" +
                away2pt[8][0] + "  " + away2pt[8][1] + "\n" +
                away2pt[9][0] + "  " + away2pt[9][1] + "\n\n";

        awayBoxScore[2] = "3FGM/A" + "\n" + away3pt[0][0] + "  " + away3pt[0][1] + "\n" +
                away3pt[1][0] + "  " + away3pt[1][1] + "\n" +
                away3pt[2][0] + "  " + away3pt[2][1] + "\n" +
                away3pt[3][0] + "  " + away3pt[3][1] + "\n" +
                away3pt[4][0] + "  " + away3pt[4][1] + "\n" +
                away3pt[5][0] + "  " + away3pt[5][1] + "\n" +
                away3pt[6][0] + "  " + away3pt[6][1] + "\n" +
                away3pt[7][0] + "  " + away3pt[7][1] + "\n" +
                away3pt[8][0] + "  " + away3pt[8][1] + "\n" +
                away3pt[9][0] + "  " + away3pt[9][1] + "\n\n";


        awayBoxScore[3] = "O/DREB" + "\n" + awayReb[0][0] + "  " + awayReb[0][1] + "\n" +
                awayReb[1][0] + "  " + awayReb[1][1] + "\n" +
                awayReb[2][0] + "  " + awayReb[2][1] + "\n" +
                awayReb[3][0] + "  " + awayReb[3][1] + "\n" +
                awayReb[4][0] + "  " + awayReb[4][1] + "\n" +
                awayReb[5][0] + "  " + awayReb[5][1] + "\n" +
                awayReb[6][0] + "  " + awayReb[6][1] + "\n" +
                awayReb[7][0] + "  " + awayReb[7][1] + "\n" +
                awayReb[8][0] + "  " + awayReb[8][1] + "\n" +
                awayReb[9][0] + "  " + awayReb[9][1] + "\n\n";

        awayBoxScore[4] = "\n" + awayPlayers;

        awayBoxScore[5] = "Pts/Rebs" + "\n" + awayPtReb[0][0] + "  " + awayPtReb[0][1] + "\n" +
                awayPtReb[1][0] + "  " + awayPtReb[1][1] + "\n" +
                awayPtReb[2][0] + "  " + awayPtReb[2][1] + "\n" +
                awayPtReb[3][0] + "  " + awayPtReb[3][1] + "\n" +
                awayPtReb[4][0] + "  " + awayPtReb[4][1] + "\n" +
                awayPtReb[5][0] + "  " + awayPtReb[5][1] + "\n" +
                awayPtReb[6][0] + "  " + awayPtReb[6][1] + "\n" +
                awayPtReb[7][0] + "  " + awayPtReb[7][1] + "\n" +
                awayPtReb[8][0] + "  " + awayPtReb[8][1] + "\n" +
                awayPtReb[9][0] + "  " + awayPtReb[9][1] + "\n\n";

        awayBoxScore[6] = "AST" + "\n" + awayAst[0] + "\n" + awayAst[1] + "\n" + awayAst[2] + "\n" +
                awayAst[3] + "\n" + awayAst[4] + "\n" + awayAst[5] + "\n" + awayAst[6] + "\n" +
                awayAst[7] + "\n" + awayAst[8] + "\n" + awayAst[9] + "\n\n";

        awayBoxScore[7] = "Stl/Blk/TO" + "\n" + awaySBT[0][0] + "   " + awaySBT[0][1] + "   " + awaySBT[0][2] + "\n" +
                awaySBT[1][0] + "   " + awaySBT[1][1] + "   " + awaySBT[1][2] + "\n" +
                awaySBT[2][0] + "   " + awaySBT[2][1] + "   " + awaySBT[2][2] + "\n" +
                awaySBT[3][0] + "   " + awaySBT[3][1] + "   " + awaySBT[3][2] + "\n" +
                awaySBT[4][0] + "   " + awaySBT[4][1] + "   " + awaySBT[4][2] + "\n" +
                awaySBT[5][0] + "   " + awaySBT[5][1] + "   " + awaySBT[5][2] + "\n" +
                awaySBT[6][0] + "   " + awaySBT[6][1] + "   " + awaySBT[6][2] + "\n" +
                awaySBT[7][0] + "   " + awaySBT[7][1] + "   " + awaySBT[7][2] + "\n" +
                awaySBT[8][0] + "   " + awaySBT[8][1] + "   " + awaySBT[8][2] + "\n" +
                awaySBT[9][0] + "   " + awaySBT[9][1] + "   " + awaySBT[9][2] + "\n\n";
    }

    private void modifyPosMod(boolean bench){
        if(bench) posMod = 5;
        else posMod = 0;
    }

    public void add2ptMake(int possession, int player, boolean isBenchIn){

        modifyPosMod(isBenchIn);

        if(possession == 0) {
            home2pt[player + posMod][0]++;
            home2pt[player + posMod][1]++;
            homePtReb[player + posMod][0] += 2;
        }
        else if(possession == 1){
            away2pt[player + posMod][0]++;
            away2pt[player + posMod][1]++;
            awayPtReb[player + posMod][0] += 2;
        }
    }

    public void add2ptMiss(int possession, int player, boolean isBenchIn){

        modifyPosMod(isBenchIn);

        if(possession == 0) home2pt[player + posMod][1]++;
        else if(possession == 1) away2pt[player + posMod][1]++;

    }

    public void add3ptMake(int possession, int player, boolean isBenchIn){

        modifyPosMod(isBenchIn);

        if(possession == 0){
            home3pt[player + posMod][0]++;
            home3pt[player + posMod][1]++;
            homePtReb[player + posMod][0] += 3;
        }
        else if(possession == 1){
            away3pt[player + posMod][0]++;
            away3pt[player + posMod][1]++;
            awayPtReb[player + posMod][0] += 3;
        }
    }

    public void add3ptMiss(int possession, int player, boolean isBenchIn){

        modifyPosMod(isBenchIn);

        if(possession == 0) home3pt[player + posMod][1]++;
        else if(possession == 1) away3pt[player + posMod][1]++;
    }

    public void addOReb(int possession, int player, boolean isBenchIn){

        modifyPosMod(isBenchIn);

        if(possession == 0) {
            homeReb[player + posMod][0]++;
            homePtReb[player + posMod][1]++;
        }
        else if(possession == 1) {
            awayReb[player + posMod][0]++;
            awayPtReb[player + posMod][1]++;
        }
    }

    public void addDReb(int possession, int player, boolean isBenchIn){

        modifyPosMod(isBenchIn);

        // The defensive rebound goes to the team that did not currently have possession
        if(possession == 0) {
            awayReb[player + posMod][1]++;
            awayPtReb[player + posMod][1]++;
        }
        else if(possession == 1) {
            homeReb[player + posMod][1]++;
            homePtReb[player + posMod][1]++;
        }
    }

    public void addSteal(int possession, int player, boolean isBenchIn){

        modifyPosMod(isBenchIn);

        if(possession == 0){
            homeSBT[player + posMod][2]++; //Add TO
            awaySBT[player + posMod][0]++; //Add steal
        }
        else if(possession == 1){
            awaySBT[player + posMod][2]++; //Add TO
            homeSBT[player + posMod][0]++; //Add steal
        }
    }

    public void addBlock(int possession, int player, boolean isBenchIn){

        modifyPosMod(isBenchIn);

        if(possession == 0) awaySBT[player + posMod][1]++;
        else if(possession == 1) homeSBT[player + posMod][1]++;
    }

    public void addUnforcedTO(){

        // Will add unforced Turnovers once those are added to the game sim logic
    }

    public void addAst(int possession, int player, boolean isBenchIn){

        modifyPosMod(isBenchIn);

        if(possession == 0) homeAst[player + posMod]++;
        else if(possession == 1) awayAst[player + posMod]++;
    }



}
