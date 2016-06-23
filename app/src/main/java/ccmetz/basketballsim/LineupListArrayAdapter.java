package ccmetz.basketballsim;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    public LineupListArrayAdapter(Context context, ArrayList<Player> rosterList){

        super(context, R.layout.adjust_lineup_list_item, rosterList);

        this.context = context;
        this.rosterList = rosterList;

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

        RadioButton firstString = (RadioButton) convertView.findViewById(R.id.radio_starter);
        RadioButton secondString = (RadioButton) convertView.findViewById(R.id.radio_backup);
        RadioButton thirdString = (RadioButton) convertView.findViewById(R.id.radio_bench);


        Player.Role playerRole = rosterList.get(position).getPlayerRole();

        if(playerRole == Player.Role.STARTER){
            //Log.i("Role", "STARTER");
            detailText.setTextColor(Color.parseColor("#84c103"));
            firstString.setChecked(true);
        }
        else if(playerRole == Player.Role.ROLEPLAYER){
          //  Log.i("Role", "ROLE");
            detailText.setTextColor(Color.parseColor("#d4444a"));
            secondString.setChecked(true);
        }
        else if(playerRole == Player.Role.BENCH){
           // Log.i("Role", "BENCH");
            detailText.setTextColor(Color.GRAY);
            thirdString.setChecked(true);
        }

        return convertView;

    }


}
