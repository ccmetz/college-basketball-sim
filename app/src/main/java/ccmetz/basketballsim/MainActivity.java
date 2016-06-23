package ccmetz.basketballsim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import CbbSimEngine.League;
import CbbSimEngine.Player;
import CbbSimEngine.Team;

public class MainActivity extends AppCompatActivity {

    private int confCounter; //the position of the conference in the League class
    private int teamCounter; //position of the team in the Conference class
    private TabLayout tabLayout;

    private LinearLayout buttonPanel; //Horizontal layout for lineup and stats buttons
    private Button lineupButton; //Button for adjusting the lineup
    private Button statsButton; //Button for viewing player stats
    private TextView scheduleText; //Header for Schedule tab
    private TextView confSpinnerText; //Header for conference spinner
    private TextView teamSpinnerText; //Header for team spinner
    private Button simButton;

    private ExpandableListView rosterView;
    private ExpandableListAdapterRoster rosterAdapter; //Adapter for roster listview

    private ListView scheduleView;
    private ArrayAdapter<String> scheduleAdapter;

    private Spinner confSpinner; //Spinner to choose conference on Around the League tab
    private ArrayList<String> confList;
    private ArrayAdapter<String> confAdapter;
    private Spinner teamSpinner; //Spinner to choose team on Around the League tab
    private ArrayList<String> teamList;
    private ArrayAdapter<String> teamAdapter;

    private League league; //This will be the league that the user will officially play in
    private Team userTeam; //The team that the user will be controlling
    private int season; //Current season
    private ArrayList<String> posList;

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

        // Initialize and define the list of player positions
        posList = new ArrayList<String>();
        posList.add("PG");
        posList.add("SG");
        posList.add("SF");
        posList.add("PF");
        posList.add("C");

        getSupportActionBar().setTitle(userTeam.getTeamName() + " " + season + " Season");

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.roster_tab_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.schedule_tab_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.rest_of_league_title));

        buttonPanel = (LinearLayout) findViewById(R.id.button_panel);
        lineupButton = (Button) findViewById(R.id.lineup_button);
        statsButton = (Button) findViewById(R.id.stats_button);
        scheduleText = (TextView) findViewById(R.id.schedule_text);
        scheduleText.setText(userTeam.getTeamName() + " (" + userTeam.getRecord() + ")");
        simButton = (Button) findViewById(R.id.sim_week_button);
        confSpinnerText = (TextView) findViewById(R.id.conf_spinner_text);
        teamSpinnerText = (TextView) findViewById(R.id.team_spinner_text);

        /* Define the functionality of the lineupButton */
        lineupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rootView = inflater.inflate(R.layout.lineup_dialog, null);

                //Initialize the Spinner
                Spinner posSpinner = (Spinner) rootView.findViewById(R.id.position_spinner);
                ArrayAdapter<String> posAdapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item, posList);
                posAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                posSpinner.setAdapter(posAdapter);

                final ArrayList<Player> playerList = new ArrayList<Player>();
                playerList.addAll(userTeam.getPositionList(0));

                //Initialize the ListView and set the custom adapter
                ListView lineupView = (ListView) rootView.findViewById(R.id.lineup_view);
                final LineupListArrayAdapter lineupAdapter =
                        new LineupListArrayAdapter(MainActivity.this, playerList);
                lineupView.setAdapter(lineupAdapter);

                // Handle ListView changes when the spinner position is changed
                posSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        playerList.clear();
                        playerList.addAll(userTeam.getPositionList(position));

                        lineupAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //Initialize the Save Lineup button
                Button saveLineupButton = (Button) rootView.findViewById(R.id.save_lineup_button);
                saveLineupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Check for valid # of Starters, Role Players, etc
                        // Reorganize the userTeam roster and update the rosterView on the Manage Roster tab

                    }
                });

                //Show the Adjust Lineup Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(rootView);
                builder.show();
            }
        });

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

                /** FOR DEBUGGING PURPOSES **/

               /* //Display a dialog that shows the log of the game
                String log = userTeam.getGameArrayList().get(position).getGameLog();
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(log)
                        .show(); */

                //Get the gameBoxScore array from the specific game
                String[] gameBoxScore = userTeam.getGameArrayList().get(position).getBoxScore();

                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rootView = inflater.inflate(R.layout.boxscore_dialog, null);

                //Initialize the GridView and set the custom BoxScoreAdapter
                GridView boxScoreGrid = (GridView) rootView.findViewById(R.id.box_score_grid);
                boxScoreGrid.setAdapter(new BoxScoreAdapter(gameBoxScore, MainActivity.this));

                //Show the box score dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(rootView);
                builder.show();

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


        /* Setup the conference and team spinners */
        confList = new ArrayList<String>();
        confSpinner = (Spinner) findViewById(R.id.main_conf_spinner);

        for (int i = 0; i < league.getConferences().size(); i++) {

            confList.add(i, league.getConferences().get(i).getConfName());
        }

        confAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, confList);
        confAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        confSpinner.setAdapter(confAdapter);

        teamList = new ArrayList<String>();
        teamSpinner = (Spinner) findViewById(R.id.main_team_spinner);

        for (int i = 0; i < league.getConferences().get(0).getTeams().size(); i++) {

            // Defaults to teams belonging to the first conference initialized in the league
            teamList.add(i, league.getConferences().get(0).getTeams().get(i).getTeamName());
        }

        teamAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teamList);
        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpinner.setAdapter(teamAdapter);

        confSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                teamList.clear();

                for (int i = 0; i < league.getConferences().get(position).getTeams().size(); i++) {

                    teamList.add(i, league.getConferences().get(position).getTeams().get(i).getTeamName());
                }

                teamAdapter.notifyDataSetChanged(); //Updates the team spinner with appropriate team names
                teamSpinner.setSelection(0); //Reset the spinner to the top of the list

                confCounter = position;
                teamCounter = 0;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                teamCounter = position;

                // Set the roster to the selected team and update the Adapter
                rosterAdapter.updateRosterList(league.getConferences().get(confCounter)
                        .getTeams().get(teamCounter).getRoster());

                rosterView.setSelection(0); //Scroll rosterView to the top after update
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /* The following views will default as not visible because the Manage Roster tab is selected by default
         * Set visibility of views that are not on the Manage Roster tab to View.GONE */
        if (scheduleText != null) {
            scheduleText.setVisibility(View.GONE);
        }

        scheduleView.setVisibility(View.GONE);
        simButton.setVisibility(View.GONE);

        if (confSpinnerText != null) {
            confSpinnerText.setVisibility(View.GONE);
        }

        confSpinner.setVisibility(View.GONE);

        if(teamSpinnerText != null){
            teamSpinnerText.setVisibility(View.GONE);
        }
        teamSpinner.setVisibility(View.GONE);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tabLayout.getSelectedTabPosition();

                if (pos == 0) {

                    buttonPanel.setVisibility(View.VISIBLE);
                    lineupButton.setVisibility(View.VISIBLE);
                    statsButton.setVisibility(View.VISIBLE);
                    rosterView.setVisibility(View.VISIBLE);

                    //Set the roster to the user's team and update the adapter
                    rosterAdapter.updateRosterList(userTeam.getRoster());

                } else if (pos == 1) {

                    scheduleText.setVisibility(View.VISIBLE);
                    scheduleView.setVisibility(View.VISIBLE);
                    simButton.setVisibility(View.VISIBLE);
                } else if (pos == 2) {

                    confSpinnerText.setVisibility(View.VISIBLE);
                    confSpinner.setVisibility(View.VISIBLE);
                    teamSpinnerText.setVisibility(View.VISIBLE);
                    teamSpinner.setVisibility(View.VISIBLE);
                    rosterView.setVisibility(View.VISIBLE);

                    //Set the roster to the current team selected in the team spinner and update the Adapter
                    rosterAdapter.updateRosterList(league.getConferences().get(confCounter)
                            .getTeams().get(teamCounter).getRoster());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tabLayout.getSelectedTabPosition();

                if (pos == 0) {

                    buttonPanel.setVisibility(View.GONE);
                    lineupButton.setVisibility(View.GONE);
                    statsButton.setVisibility(View.GONE);
                    rosterView.setVisibility(View.GONE);

                } else if (pos == 1) {

                    scheduleText.setVisibility(View.GONE);
                    scheduleView.setVisibility(View.GONE);
                    simButton.setVisibility(View.GONE);
                } else if (pos == 2) {

                    confSpinnerText.setVisibility(View.GONE);
                    confSpinner.setVisibility(View.GONE);
                    teamSpinnerText.setVisibility(View.GONE);
                    teamSpinner.setVisibility(View.GONE);
                    rosterView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

}

