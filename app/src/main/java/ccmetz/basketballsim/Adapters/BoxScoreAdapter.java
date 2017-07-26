package ccmetz.basketballsim.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ccmetz on 6/1/16.
 */
public class BoxScoreAdapter extends BaseAdapter
{

  private String[] homeBoxScore;
  private String[] awayBoxScore;
  private Context context;
  private int counter = 0;


  public BoxScoreAdapter(ArrayList<String[]> boxScore, Context c)
  {
    homeBoxScore = boxScore.get(0);
    awayBoxScore = boxScore.get(1);
    context = c;
  }

  public int getCount()
  {

    return homeBoxScore.length;
  }

  public Object getItem(int position)
  {

    return null;
  }

  public long getItemId(int position)
  {

    return 0;
  }

  public View getView(int position, View convertView, ViewGroup parent)
  {

    TextView textView;

    if (convertView == null)
    {

      textView = new TextView(context);
    }
    else
    {

      textView = (TextView) convertView;
    }

    if (counter == 0) textView.setText(homeBoxScore[position]);
    else if (counter == 1) textView.setText(awayBoxScore[position]);

    return textView;
  }

  // Method used to update the box score when a new team is selected in the box score dialog
  public void updateBoxScore(int position)
  {

    counter = position;
    this.notifyDataSetChanged();
  }
}
