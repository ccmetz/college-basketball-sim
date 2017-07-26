package ccmetz.basketballsim.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ccmetz.basketballsim.Models.Player;
import ccmetz.basketballsim.Models.Player.Role;
import ccmetz.basketballsim.R;

/**
 * Created by ccmetz on 5/2/16.
 * <p>
 * Custom ExpandableListAdapter that will show Player ratings and stats
 */
public class ExpandableListAdapterRoster extends BaseExpandableListAdapter
{

  private Activity context;
  private ArrayList<Player> rosterList = new ArrayList<Player>();

  public ExpandableListAdapterRoster(Activity context, ArrayList<Player> rosterList)
  {

    this.context = context;
    this.rosterList.addAll(rosterList);

  }

  @Override
  public int getGroupCount()
  {
    return rosterList.size();
  }

  @Override
  public int getChildrenCount(int groupPosition)
  {
    return rosterList.get(groupPosition).displayStatsAndRatings().size();
  }

  @Override
  public Player getGroup(int groupPosition)
  {
    return rosterList.get(groupPosition);
  }

  @Override
  public String getChild(int groupPosition, int childPosition)
  {
    return rosterList.get(groupPosition).displayStatsAndRatings().get(childPosition);
  }

  @Override
  public long getGroupId(int groupPosition)
  {
    return groupPosition;
  }

  @Override
  public long getChildId(int groupPosition, int childPosition)
  {
    return childPosition;
  }

  @Override
  public boolean hasStableIds()
  {
    return true;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
  {

    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    convertView = layoutInflater.inflate(R.layout.group_player_details, null);
    TextView detailText = (TextView) convertView.findViewById(R.id.group_player_text);
    detailText.setText(getGroup(groupPosition).displayPlayerInfo());
    detailText.setTypeface(null, Typeface.BOLD);

    Role playerRole = getGroup(groupPosition).getPlayerRole();

    if (playerRole == Role.STARTER)
    {
      detailText.setTextColor(Color.parseColor("#84c103"));
    }
    else if (playerRole == Role.ROLEPLAYER)
    {
      detailText.setTextColor(Color.parseColor("#d4444a"));

    }


    return convertView;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
  {

    LayoutInflater inflater = context.getLayoutInflater();

    if (convertView == null)
    {
      convertView = inflater.inflate(R.layout.child_player_details, null);
    }

    String playerStats = getChild(groupPosition, childPosition);
    String[] statArray = playerStats.split("/");
    TextView leftText = (TextView) convertView.findViewById(R.id.left_detail_text);
    leftText.setText(statArray[0]);
    TextView rightText = (TextView) convertView.findViewById(R.id.right_detail_text);
    rightText.setText(statArray[1]);

    return convertView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition)
  {
    return true;
  }

  public void updateRosterList(ArrayList<Player> newRoster)
  {

    rosterList.clear();
    rosterList.addAll(newRoster);
    this.notifyDataSetChanged();
  }
}


