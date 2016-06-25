package ccmetz.basketballsim;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import CbbSimEngine.Player;

/**
 * Created by ccmetz on 6/16/16.
 *
 * Custom ArrayAdapter that will handle the player's that show up in the lineup ListView
 * and track the changes made to the depth chart by the user
 */
public class LineupListArrayAdapter extends ArrayAdapter<Player> {

    private Context context;
    private ArrayList<Player> rosterList;
    private int[] roleTracker;

    public LineupListArrayAdapter(Context context, ArrayList<Player> rosterList){

        super(context, R.layout.adjust_lineup_list_item, rosterList);

        this.context = context;
        this.rosterList = rosterList;

        roleTracker = new int[3]; //Max # of players per position is 3 right now

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adjust_lineup_list_item, parent, false);
        }

        TextView detailText = (TextView) convertView.findViewById(R.id.player_detail_text);
        detailText.setText(rosterList.get(position).displayShortPlayerInfo());
        detailText.setTypeface(null, Typeface.BOLD);

        final RadioButton firstString = (RadioButton) convertView.findViewById(R.id.radio_starter);
        final RadioButton secondString = (RadioButton) convertView.findViewById(R.id.radio_backup);
        final RadioButton thirdString = (RadioButton) convertView.findViewById(R.id.radio_bench);


        Player.Role playerRole = rosterList.get(position).getPlayerRole();

        if(playerRole == Player.Role.STARTER){
            //Log.i("Role", "STARTER");
            detailText.setTextColor(Color.parseColor("#84c103"));
            firstString.setChecked(true);
            roleTracker[position] = 1;
        }
        else if(playerRole == Player.Role.ROLEPLAYER){
          //  Log.i("Role", "ROLE");
            detailText.setTextColor(Color.parseColor("#d4444a"));
            secondString.setChecked(true);
            roleTracker[position] = 2;
        }
        else if(playerRole == Player.Role.BENCH){
           // Log.i("Role", "BENCH");
            detailText.setTextColor(Color.GRAY);
            thirdString.setChecked(true);
            roleTracker[position] = 3;
        }

        firstString.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(firstString.isChecked()){

                    roleTracker[position] = 1; // Set to starter
                }
            }
        });

        secondString.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(secondString.isChecked()){

                    roleTracker[position] = 2; // Set to Role player
                }
            }
        });

        thirdString.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(thirdString.isChecked()){

                    roleTracker[position] = 3; // Set to bench
                }
            }
        });

        return convertView;

    }

    public void updateLineupAdapter(){

        //Clear the roleTracker
       for(int i = 0; i < roleTracker.length; i++){

            roleTracker[i] = 0;
       }

        this.notifyDataSetChanged();

    }

    public int[] getRoleChanges(){

        return roleTracker;
    }


}
