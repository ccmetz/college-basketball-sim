package ccmetz.basketballsim;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by ccmetz on 6/1/16.
 */
public class BoxScoreAdapter extends BaseAdapter {

    private String[] gameBoxScore;
    private Context context;


    public BoxScoreAdapter(String[] boxScore, Context c){

        gameBoxScore = boxScore;
        context = c;
    }

    public int getCount(){

        return gameBoxScore.length;
    }

    public Object getItem(int position){

        return null;
    }

    public long getItemId(int position){

        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        TextView textView;

        if(convertView == null){

            textView = new TextView(context);
        }
        else{

            textView = (TextView) convertView;
        }

        textView.setText(gameBoxScore[position]);
        return textView;
    }
}
