package ccmetz.basketballsim.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ccmetz.basketballsim.Models.Player;

/**
 * Created by ccmetz on 10/2/16.
 */
public class PlayerStatsAdapter extends BaseAdapter {

    private ArrayList<Player> roster;
    private Context context;
    private int counter = 0;

    public PlayerStatsAdapter(ArrayList<Player> roster, Context c){

        this.roster = roster;
        context = c;
    }

    @Override
    public int getCount() {
        return roster.get(counter).getPlayerStats().length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textView;

        if(convertView == null){
            textView = new TextView(context);
        }
        else{
            textView = (TextView) convertView;
        }


        textView.setText(roster.get(counter).getPlayerStats()[position]);
        return textView;
    }

    public void updatePlayerStats(int position){

        counter = position;
        this.notifyDataSetChanged();
    }
}
