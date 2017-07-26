package ccmetz.basketballsim.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ccmetz.basketballsim.R;

public class TeamActivity extends AppCompatActivity
{

  private Spinner confSpinner; //Spinner that the user can use to select conferences
  private List<String> confNames; //ArrayList that holds all of the conference names
  private ArrayAdapter<String> confAdapter; //Adapter that attaches conference names to the confSpinner

  private ListView teamView; //ListView used to display teams in the activity
  private List<String> teamNames; //ArrayList that holds all of the team names
  private ArrayAdapter<String> teamAdapter; //Adapter that attaches the teams names to the teamView

  private int currentConf; //Keeps track of currently selected conference (when conference changes -> teamView updates)
  private int currentTeam; //Keeps track of currently selected team


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_team);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    teamNames = new ArrayList<String>();
    // Defaults to BIG 10 team names
    teamNames.addAll(Arrays.asList(getResources().getStringArray(R.array.big10_array)));

    currentConf = 0; //First conference is selected by default
    currentTeam = 0; //First team is selected by default

        /* Spinner for Conferences */
    confSpinner = (Spinner) findViewById(R.id.conf_spinner);

    // Create the confNames ArrayList to be used in the confAdapter
    confNames = new ArrayList<String>();
    confNames.addAll(Arrays.asList(getResources().getStringArray(R.array.conference_array)));

    confAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, confNames);
    confAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    confSpinner.setAdapter(confAdapter);

    confSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
      {

        currentConf = position;
        //Change the teamNames ArrayList to include only teams from the selected conference
        teamNames.clear();

        if (currentConf == 0)
        {
          teamNames.addAll(Arrays.asList(getResources().getStringArray(R.array.big10_array)));
        }
        else if (currentConf == 1)
        {
          teamNames.addAll(Arrays.asList(getResources().getStringArray(R.array.big12_array)));
        }
        else if (currentConf == 2)
        {
          teamNames.addAll(Arrays.asList(getResources().getStringArray(R.array.sec_array)));
        }
        else if (currentConf == 3)
        {
          teamNames.addAll(Arrays.asList(getResources().getStringArray(R.array.acc_array)));
        }

        teamAdapter.notifyDataSetChanged(); //updates the listview with new teams
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent)
      {

      }
    });

        /* ListView for Teams that belong to conference picked in the Spinner */
    teamView = (ListView) findViewById(R.id.team_list);

    teamAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teamNames);
    teamView.setAdapter(teamAdapter);

        /* The team roster will display when clicked on */
    teamView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id)
      {

        currentTeam = position;
        // Get just the team name for the confirmation dialog (Need to subtract " - Prestige: ##")
        String confirmName = null;
        if (teamNames.get(currentTeam).contains(" -"))
        {
          confirmName = teamNames.get(currentTeam).substring(0, teamNames.get(currentTeam).indexOf(" -"));
        }

                /* Create and show the dialog for the team selected */
        AlertDialog.Builder dialog = new AlertDialog.Builder(TeamActivity.this);
        dialog.setMessage("Are you sure you want to be the coach of " + confirmName + "?");


        dialog.setPositiveButton("Accept the job!", new DialogInterface.OnClickListener()
        {
          @Override
          public void onClick(DialogInterface dialog, int which)
          {

            Intent intent = new Intent(TeamActivity.this, MainActivity.class);
                        /* Push info for the team that the user picked to the main activity */
            intent.putExtra("chosenConf", currentConf);
            intent.putExtra("chosenTeam", currentTeam);
            startActivity(intent);

          }
        });

        dialog.setNegativeButton("Consider other offers", new DialogInterface.OnClickListener()
        {
          @Override
          public void onClick(DialogInterface dialog, int which)
          {
            //Exit dialog
          }
        });

        dialog.show();
      }
    });


  }


}
