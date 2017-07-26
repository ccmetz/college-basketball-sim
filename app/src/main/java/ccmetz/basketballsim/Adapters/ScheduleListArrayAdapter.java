package ccmetz.basketballsim.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ccmetz.basketballsim.R;

/**
 * Created by ccmetz on 5/4/16.
 */
public class ScheduleListArrayAdapter extends ArrayAdapter<String>
{
  private Context context;
  private ArrayList<String> scheduleList = new ArrayList<String>();


  public ScheduleListArrayAdapter(Context context, ArrayList<String> scheduleList)
  {
    super(context, R.layout.schedule_list_item, scheduleList);

    this.context = context;
    this.scheduleList.addAll(scheduleList);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (convertView == null)
    {
      convertView = inflater.inflate(R.layout.schedule_list_item, parent, false);
    }

    TextView leftText = (TextView) convertView.findViewById(R.id.opp_textview);
    TextView rightText = (TextView) convertView.findViewById(R.id.result_textview);

    String[] splitArray = scheduleList.get(position).split("/");
    leftText.setText(splitArray[0]);
    rightText.setText(splitArray[1]);

    return convertView;
  }

  public void updateScheduleList(ArrayList<String> newSchedule)
  {
    scheduleList.clear();
    scheduleList.addAll(newSchedule);
    this.notifyDataSetChanged();
  }
}
