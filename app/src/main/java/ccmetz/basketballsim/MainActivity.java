package ccmetz.basketballsim;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import CbbSimEngine.League;
import CbbSimEngine.Team;

public class MainActivity extends AppCompatActivity {

    private int confCounter; //the position of the conference in the League class
    private int teamCounter; //position of the team in the Conference class
    private TabLayout tabLayout;
    private TextView rosterText; //Header for Manage Roster tab
    private TextView scheduleText; //Header for Schedule tab
    private TextView restOfLeagueText; //Header for Around the League tab
    private Button simButton;

    private ExpandableListView rosterView;
    private ExpandableListAdapterRoster rosterAdapter; //Adapter for roster listview

    private ListView scheduleView;
    private ArrayAdapter<String> scheduleAdapter;

    private League league; //This will be the league that the user will officially play in
    private Team userTeam; //The team that the user will be controlling
    private int season; //Current season

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        /* This will probably need to be encased in an if statement that checks for NEW GAME or LOAD GAME */
        league = new League();
        Intent intent = getIntent();
        confCounter = intent.getIntExtra("chosenConf", 0);
        teamCounter = intent.getIntExtra("chosenTeam", 0);
        ////////////////////////////////////////////////////////////////////////////////////////////////////

        /* Assign the correct Team to the user */
        userTeam = league.getConferences().get(confCounter).getTeams().get(teamCounter);
        userTeam.setUserControl(true);

        season = league.getCurrentSeason();

        getSupportActionBar().setTitle(userTeam.getTeamName() + " " + season + " Season");

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.roster_tab_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.schedule_tab_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.rest_of_league_title));

        rosterText = (TextView) findViewById(R.id.roster_text);
        scheduleText = (TextView) findViewById(R.id.schedule_text);
        scheduleText.setText(userTeam.getTeamName() + " (" + userTeam.getRecord() + ")");
        simButton = (Button) findViewById(R.id.sim_week_button);
        restOfLeagueText = (TextView) findViewById(R.id.rest_of_league_text);

        /* Setup the ExpandableListView with a custom adapter */
        rosterView = (ExpandableListView) findViewById(R.id.roster_view);
        rosterAdapter = new ExpandableListAdapterRoster(this, userTeam.getRoster());
        rosterView.setAdapter(rosterAdapter);

        /* Setup the schedule listview with a custom adapter */
        scheduleView = (ListView) findViewById(R.id.schedule_view);
        scheduleAdapter = new ScheduleListArrayAdapter(this, userTeam.getScheduleList());
        scheduleView.setAdapter(scheduleAdapter);

        scheduleView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               /* //Display a dialog that shows the log of the game
                String log = userTeam.getGameArrayList().get(position).getGameLog();
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(log)
                        .show(); */

                //CREATE A BOXSCORE DIALOG USING A GRIDVIEW AND PROBABLY A CUSTOM ADAPTER THAT
                //TAKES IN gameBoxScore AS AN ARGUMENT
                String[] gameBoxScore = userTeam.getGameArrayList().get(position).getBoxScore();

                // Logging for debugging purposes!
                Log.i("gameBoxScore[0]: ", gameBoxScore[0]);
                Log.i("gameBoxScore[1]: ", gameBoxScore[1]);
                Log.i("gameBoxScore[2]: ", gameBoxScore[2]);
                Log.i("gameBoxScore[3]: ", gameBoxScore[3]);

            }
        });


        /* Sim Button functionality */
        simButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                league.simCurrentWeek();
                //Refresh the team record and game schedule on the schedule tab
                scheduleText.setText(userTeam.getTeamName() + " (" + userTeam.getRecord() + ")");
                scheduleAdapter.notifyDataSetChanged();
            }
        });


        /* The following views will default as not visible because the lineup tab is selected by default */
        if (scheduleText != null) {
            scheduleText.setVisibility(View.GONE);
        }

        scheduleView.setVisibility(View.GONE);
        simButton.setVisibility(View.GONE);

        if(restOfLeagueText != null){
            restOfLeagueText.setVisibility(View.GONE);
        }


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tabLayout.getSelectedTabPosition();

                if(pos == 0){

                    rosterText.setVisibility(View.VISIBLE);
                    rosterView.setVisibility(View.VISIBLE);
                }
                else if(pos == 1){

                    scheduleText.setVisibility(View.VISIBLE);
                    scheduleView.setVisibility(View.VISIBLE);
                    simButton.setVisibility(View.VISIBLE);
                }
                else if(pos == 2){

                    restOfLeagueText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tabLayout.getSelectedTabPosition();

                if(pos == 0){

                    rosterText.setVisibility(View.GONE);
                    rosterView.setVisibility(View.GONE);
                }
                else if(pos == 1){

                    scheduleText.setVisibility(View.GONE);
                    scheduleView.setVisibility(View.GONE);
                    simButton.setVisibility(View.GONE);
                }
                else if(pos == 2){

                    restOfLeagueText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void onBackPressed(){

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
