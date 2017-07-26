package ccmetz.basketballsim.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import ccmetz.basketballsim.Adapters.BoxScoreAdapter;
import ccmetz.basketballsim.Adapters.ExpandableListAdapterRoster;
import ccmetz.basketballsim.Adapters.LineupListArrayAdapter;
import ccmetz.basketballsim.Adapters.PlayerStatsAdapter;
import ccmetz.basketballsim.Adapters.ScheduleListArrayAdapter;
import ccmetz.basketballsim.Models.Game;
import ccmetz.basketballsim.Models.League;
import ccmetz.basketballsim.Models.Player;
import ccmetz.basketballsim.Models.Team;
import ccmetz.basketballsim.R;

public class MainActivity extends AppCompatActivity
{

  private int confCounter; //the position of the conference in the League class
  private int teamCounter; //position of the team in the Conference class
  private TabLayout tabLayout;

  private LinearLayout buttonPanel; //Horizontal layout for lineup and stats buttons
  private Button lineupButton; //Button for adjusting the lineup
  private Button statsButton; //Button for viewing player stats
  private TextView scheduleText; //Header for Schedule tab
  private TextView confSpinnerText; //Header for conference spinner
  private TextView teamSpinnerText; //Header for team spinner
  private Button simButton; //Button for simming games
  private RadioGroup radioGroup; //Group that contains the 2 radio buttons for seeing the roster and schedule
  private RadioButton rosterButton; //RadioButton for viewing the roster
  private RadioButton scheduleButton; //RadioButton for viewing the schedule

  private ExpandableListView rosterView;
  private ExpandableListAdapterRoster rosterAdapter; //Adapter for roster listview

  private ListView scheduleView;
  private ScheduleListArrayAdapter scheduleAdapter;
  private boolean onATLTab; //Keeps track of what box scores need to be accessed based on which tab is selected
  private ArrayList<Game> boxScoreTracker; //Keeps track of which team's Game List to use for box scores

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
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // NEW GAME or LOAD GAME?
    Bundle bundle = getIntent().getExtras();
    if (bundle.getBoolean("LOAD"))
    {
      loadLeague("leaguefile.ser");
    }
    else
    {
      league = new League();
      Intent intent = getIntent();
      confCounter = intent.getIntExtra("chosenConf", 0);
      teamCounter = intent.getIntExtra("chosenTeam", 0);
      /* Assign the correct Team to the user */
      userTeam = league.getConferences().get(confCounter).getTeams().get(teamCounter);
      userTeam.setUserControl(true);
    }

    onATLTab = false; //Default to appropriate box score for schedule tab (user's team)
    boxScoreTracker = userTeam.getGameArrayList(); //Set to user's team by default

    season = league.getCurrentSeason();

    // Grab the list of basketball positions from the League object
    posList = new ArrayList<String>();
    posList.addAll(league.getPosList());

    getSupportActionBar().setTitle(userTeam.getTeamName() + " " + season + " Season");

    tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.addTab(tabLayout.newTab().setText(R.string.roster_tab_title));
    tabLayout.addTab(tabLayout.newTab().setText(R.string.schedule_tab_title));
    tabLayout.addTab(tabLayout.newTab().setText(R.string.rest_of_league_title));

    buttonPanel = (LinearLayout) findViewById(R.id.button_panel);
    lineupButton = (Button) findViewById(R.id.lineup_button);
    statsButton = (Button) findViewById(R.id.stats_button);
    scheduleText = (TextView) findViewById(R.id.schedule_text);
    scheduleText.setText("#" + userTeam.getRanking() + " " + userTeam.getTeamName() + " (" + userTeam.getRecord() + ")");
    simButton = (Button) findViewById(R.id.sim_week_button);
    confSpinnerText = (TextView) findViewById(R.id.conf_spinner_text);
    teamSpinnerText = (TextView) findViewById(R.id.team_spinner_text);

    /* Define the functionality of the lineupButton */
    lineupButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
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
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
          {
            playerList.clear();
            playerList.addAll(userTeam.getPositionList(position));

            lineupAdapter.updateLineupAdapter();
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Initialize the Save Lineup button
        Button saveLineupButton = (Button) rootView.findViewById(R.id.save_lineup_button);
        saveLineupButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v)
          {
            // Check for valid # of Starters, Role Players, etc
            int[] roleTracker = lineupAdapter.getRoleChanges();
            int starterCount = 0;
            int roleCount = 0;

            for (int i = 0; i < roleTracker.length; i++)
            {
              if (roleTracker[i] == 1) starterCount++;
              else if (roleTracker[i] == 2) roleCount++;
            }

            // Reorganize the userTeam roster and update the rosterView on the Manage Roster tab
            if (starterCount == 1 && roleCount == 1)
            {
              for (int i = 0; i < playerList.size(); i++)
              {
                switch (roleTracker[i])
                {
                  case 1:
                    playerList.get(i).setPlayerRole(Player.Role.STARTER);
                    break;
                  case 2:
                    playerList.get(i).setPlayerRole(Player.Role.ROLEPLAYER);
                    break;
                  case 3:
                    playerList.get(i).setPlayerRole(Player.Role.BENCH);
                    break;
                }
              }

              userTeam.updateTeamLineup(); //Reorganize the team roster
              rosterAdapter.updateRosterList(userTeam.getRoster()); //update the rosterView ListView
              lineupAdapter.updateLineupAdapter(); //Update the the dialog once the roster is reorganized
            }
            else
            {
              //Error - Don't save
              Toast toast = new Toast(MainActivity.this);
              if (starterCount != 1 && roleCount != 1)
              {
                toast = Toast.makeText(MainActivity.this, "Must have only 1 starter and 1 role player per position!", Toast.LENGTH_SHORT);
              }
              else if (starterCount != 1)
              {
                toast = Toast.makeText(MainActivity.this, "Must have 1 starter per position!", Toast.LENGTH_SHORT);
              }
              else
              {
                toast = Toast.makeText(MainActivity.this, "Must have 1 role player per position!", Toast.LENGTH_SHORT);
              }
              toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
              toast.show();
            }
          }
        });


        //Create and show the Adjust Lineup Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(rootView);
        builder.setTitle(getResources().getString(R.string.adjust_lineup_title));
        final AlertDialog dialog = builder.create();
        dialog.show();

        //Initialize the Close button
        Button closeLineupButton = (Button) rootView.findViewById(R.id.close_lineup_button);
        closeLineupButton.setOnClickListener(new View.OnClickListener()
        {
          @Override
          public void onClick(View v)
          {
            dialog.dismiss();
          }
        });
      }
    });

    /* Define the functionality of the Player Stats button */
    statsButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.player_stats_dialog, null);

        // Get list of player names on roster
        ArrayList<String> playerList = new ArrayList<String>();
        for (int i = 0; i < userTeam.getRoster().size(); i++)
        {
          playerList.add(userTeam.getRoster().get(i).displayPlayerInfo());
        }

        // Initialize the Spinner
        Spinner playerSpinner = (Spinner) rootView.findViewById(R.id.player_spinner);
        ArrayAdapter<String> playerAdapter = new ArrayAdapter<String>(MainActivity.this,
            android.R.layout.simple_spinner_item, playerList);
        playerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerSpinner.setAdapter(playerAdapter);

        GridView playerStatsGrid = (GridView) rootView.findViewById(R.id.player_stats_grid);
        final PlayerStatsAdapter playerStatsAdapter = new PlayerStatsAdapter(userTeam.getRoster(), MainActivity.this);
        playerStatsGrid.setAdapter(playerStatsAdapter);

        // Notify the PlayerStatsAdapter when the spinner selection changes
        playerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
          {
            playerStatsAdapter.updatePlayerStats(position);
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Show the Player Stats Dialog
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

    scheduleView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id)
      {

      if (userTeam.getGameArrayList().get(position).hasBeenPlayed)
      {
        /** FOR DEBUGGING PURPOSES **/
       /* //Display a dialog that shows the log of the game
        String log = userTeam.getGameArrayList().get(position).getGameLog();
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(log)
                .show(); */

        //Get the gameBoxScore array and team names from the specific game
        ArrayList<String[]> gameBoxScore = new ArrayList<String[]>();
        ArrayList<String> boxScoreTeams = new ArrayList<String>();

        if (onATLTab)
        {
          gameBoxScore.clear();
          gameBoxScore.add(boxScoreTracker.get(position).getBoxScore().getHomeBoxScore());
          gameBoxScore.add(boxScoreTracker.get(position).getBoxScore().getAwayBoxScore());

          boxScoreTeams.clear();
          boxScoreTeams.add(boxScoreTracker.get(position).getHomeTeam().getAbbr());
          boxScoreTeams.add(boxScoreTracker.get(position).getAwayTeam().getAbbr());
        }
        else
        {
          gameBoxScore.clear();
          gameBoxScore.add(userTeam.getGameArrayList().get(position).getBoxScore().getHomeBoxScore());
          gameBoxScore.add(userTeam.getGameArrayList().get(position).getBoxScore().getAwayBoxScore());

          boxScoreTeams.clear();
          boxScoreTeams.add(userTeam.getGameArrayList().get(position).getHomeTeam().getAbbr());
          boxScoreTeams.add(userTeam.getGameArrayList().get(position).getAwayTeam().getAbbr());
        }

        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.boxscore_dialog, null);

        //Initialize the boxScoreSpinner with the relevant team names
        Spinner boxScoreSpinner = (Spinner) rootView.findViewById(R.id.team_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,
            android.R.layout.simple_spinner_item, boxScoreTeams);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boxScoreSpinner.setAdapter(spinnerAdapter);

        //Initialize the GridView and set the custom BoxScoreAdapter
        GridView boxScoreGrid = (GridView) rootView.findViewById(R.id.box_score_grid);
        final BoxScoreAdapter boxScoreAdapter = new BoxScoreAdapter(gameBoxScore, MainActivity.this);
        boxScoreGrid.setAdapter(boxScoreAdapter);

        // Notify the boxScoreAdapter when the spinner selection changes
        boxScoreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
          {
            boxScoreAdapter.updateBoxScore(position);
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Show the box score dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(rootView);
        builder.show();
      }
      }
    });

    /* Sim Button functionality */
    simButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        new SimWeekTask().execute();
      }
    });


    /* Setup the conference and team spinners */
    confList = new ArrayList<String>();
    confSpinner = (Spinner) findViewById(R.id.main_conf_spinner);

    for (int i = 0; i < league.getConferences().size(); i++)
    {
      confList.add(i, league.getConferences().get(i).getConfName());
    }

    confAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, confList);
    confAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    confSpinner.setAdapter(confAdapter);

    teamList = new ArrayList<String>();
    teamSpinner = (Spinner) findViewById(R.id.main_team_spinner);

    for (int i = 0; i < league.getConferences().get(0).getTeams().size(); i++)
    {
      // Defaults to teams belonging to the first conference initialized in the league
      Team currentTeam = league.getConferences().get(0).getTeams().get(i);
      teamList.add(i, "#" + currentTeam.getRanking() + " " + currentTeam.getTeamName());
    }

    teamAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamList);
    teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    teamSpinner.setAdapter(teamAdapter);

    confSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
      {
        refreshTeamList(position);
        updateLists(); // Reset the team rosters and schedules
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {}
    });

    teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
      {
        teamCounter = position; // Update the position of the teamSpinner
        updateLists(); // Reset the team rosters and schedules
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {}
    });

    /* Set up the RadioGroup and RadioButton listeners */
    radioGroup = (RadioGroup) findViewById(R.id.radio_group);
    rosterButton = (RadioButton) findViewById(R.id.roster_button);
    scheduleButton = (RadioButton) findViewById(R.id.schedule_button);
    rosterButton.setChecked(true); //Roster is visible by default

    rosterButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
      if (rosterButton.isChecked())
      {
        scheduleView.setVisibility(View.GONE);
        rosterView.setVisibility(View.VISIBLE);
      }
      }
    });

    scheduleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
      if (scheduleButton.isChecked())
      {
        rosterView.setVisibility(View.GONE);
        scheduleView.setVisibility(View.VISIBLE);
      }
      }
    });


    /* The following views will default as not visible because the Manage Roster tab is selected by default
     * Set visibility of views that are not on the Manage Roster tab to View.GONE */
    if (scheduleText != null)
    {
      scheduleText.setVisibility(View.GONE);
    }

    scheduleView.setVisibility(View.GONE);
    simButton.setVisibility(View.GONE);

    if (confSpinnerText != null)
    {
      confSpinnerText.setVisibility(View.GONE);
    }

    confSpinner.setVisibility(View.GONE);

    if (teamSpinnerText != null)
    {
      teamSpinnerText.setVisibility(View.GONE);
    }
    teamSpinner.setVisibility(View.GONE);
    radioGroup.setVisibility(View.GONE);


    tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab)
      {
        int pos = tabLayout.getSelectedTabPosition();

        if (pos == 0)
        {
          buttonPanel.setVisibility(View.VISIBLE);
          lineupButton.setVisibility(View.VISIBLE);
          statsButton.setVisibility(View.VISIBLE);
          rosterView.setVisibility(View.VISIBLE);

          //Set the roster to the user's team and update the adapter
          rosterAdapter.updateRosterList(userTeam.getRoster());

        }
        else if (pos == 1)
        {
          onATLTab = false;

          scheduleText.setVisibility(View.VISIBLE);
          scheduleView.setVisibility(View.VISIBLE);
          simButton.setVisibility(View.VISIBLE);

          //Set the schedule to the user's team and update the adapter
          scheduleAdapter.updateScheduleList(userTeam.getScheduleList());
        }
        else if (pos == 2)
        {
          onATLTab = true;

          confSpinnerText.setVisibility(View.VISIBLE);
          confSpinner.setVisibility(View.VISIBLE);
          teamSpinnerText.setVisibility(View.VISIBLE);
          teamSpinner.setVisibility(View.VISIBLE);
          radioGroup.setVisibility(View.VISIBLE);

          if (rosterButton.isChecked())
          {
            rosterView.setVisibility(View.VISIBLE);
          }
          else if (scheduleButton.isChecked())
          {
            scheduleView.setVisibility(View.VISIBLE);
          }
          updateLists(); // Reset the team rosters and schedules
        }
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab)
      {
        int pos = tabLayout.getSelectedTabPosition();

        if (pos == 0)
        {
          buttonPanel.setVisibility(View.GONE);
          lineupButton.setVisibility(View.GONE);
          statsButton.setVisibility(View.GONE);
          rosterView.setVisibility(View.GONE);
        }
        else if (pos == 1)
        {
          scheduleText.setVisibility(View.GONE);
          scheduleView.setVisibility(View.GONE);
          simButton.setVisibility(View.GONE);
        }
        else if (pos == 2)
        {
          confSpinnerText.setVisibility(View.GONE);
          confSpinner.setVisibility(View.GONE);
          teamSpinnerText.setVisibility(View.GONE);
          teamSpinner.setVisibility(View.GONE);
          radioGroup.setVisibility(View.GONE);
          rosterView.setVisibility(View.GONE);
          scheduleView.setVisibility(View.GONE);
        }
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {}
    });
  }

  // Method for refreshing the teamList with updated rankings after a game is simulated
  public void refreshTeamList(int position)
  {
    teamList.clear();

    for (int i = 0; i < league.getConferences().get(position).getTeams().size(); i++)
    {
      Team currentTeam = league.getConferences().get(position).getTeams().get(i);
      teamList.add(i, "#" + currentTeam.getRanking() + " " + currentTeam.getTeamName());
    }

    teamAdapter.notifyDataSetChanged(); //Updates the team spinner with appropriate team names
    confCounter = position; // Update the position of the confSpinner
    teamSpinner.setSelection(0); // Set the teamSpinner to show the first team in the conference
    teamCounter = 0; // Record the current position of the teamSpinner
  }

  // Method for resetting the rosterView and scheduleView when a new team is selected
  public void updateLists()
  {
    // Set the roster to the selected team and update the Adapter
    rosterAdapter.updateRosterList(league.getConferences().get(confCounter)
        .getTeams().get(teamCounter).getRoster());
    rosterView.setSelection(0);

    // Set the schedule to the selected team and update the Adapter
    scheduleAdapter.updateScheduleList(league.getConferences().get(confCounter)
        .getTeams().get(teamCounter).getScheduleList());
    scheduleView.setSelection(0);

    // Set the boxScoreTracker to the selected team and update the Adapter
    boxScoreTracker = league.getConferences().get(confCounter).getTeams().get(teamCounter)
        .getGameArrayList();
  }

  // Load a league file
  public void loadLeague(String fileName)
  {
    try
    {
      FileInputStream fis = getApplicationContext().openFileInput(fileName);
      ObjectInputStream is = new ObjectInputStream(fis);
      League loadedLeague = (League) is.readObject();
      is.close();
      fis.close();

      if (loadedLeague != null)
      {
        league = loadedLeague;
        userTeam = league.getUserTeam();
      }
    }
    catch (IOException | ClassNotFoundException e)
    {
      Log.e("Load", e.getMessage());
    }
  }

  @Override
  public void onBackPressed()
  {
    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
    startActivity(intent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_home, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_save)
    {
      // Save the serializable League object
      new saveLeague("leaguefile.ser").execute();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  // Inner AsyncTask for simming the current week's games
  private class SimWeekTask extends AsyncTask<Void, Void, Boolean>
  {
    private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

    @Override
    protected void onPreExecute()
    {
      this.dialog.setMessage("Simming Next Game...");
      this.dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params)
    {
      try
      {
        league.simCurrentWeek();
        return true;
      }
      catch (Exception e)
      {
        Log.e("SimWeekTask", "Error in AsyncTask", e);
        return false;
      }
    }

    @Override
    protected void onPostExecute(final Boolean success)
    {
      if (dialog.isShowing())
      {
        dialog.dismiss();
      }
      //Refresh the team record and game schedule on the schedule tab
      scheduleText.setText(userTeam.getTeamName() + " (" + userTeam.getRecord() + ")");
      scheduleAdapter.updateScheduleList(userTeam.getScheduleList());
      // Refresh team rankings
      scheduleText.setText("#" + userTeam.getRanking() + " " + userTeam.getTeamName() + " (" + userTeam.getRecord() + ")");
      refreshTeamList(confCounter);
    }
  }

  // Inner AsyncTask for saving the current League
  private class saveLeague extends AsyncTask<Void, Void, Boolean>
  {
    private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
    private String fileName;

    public saveLeague(String fileName)
    {
      this.fileName = fileName;
    }

    @Override
    protected void onPreExecute()
    {
      this.dialog.setMessage("Saving...");
      this.dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params)
    {
      // Save the serializable League object
      try
      {
        FileOutputStream fos = MainActivity.this.getApplicationContext()
            .openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(league);
        os.close();
        fos.close();
      }
      catch (IOException e)
      {
        Log.e("Save", e.getMessage(), e);
        return false;
      }
      return true;
    }

    @Override
    protected void onPostExecute(final Boolean success)
    {
      if (dialog.isShowing())
      {
        dialog.dismiss();
      }
    }
  }
}

