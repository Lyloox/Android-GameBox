package epita.gamebox;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;


/**
 * Created by Jérémy on 12/06/2016.
 */
public class MyCustomAdapter extends BaseAdapter{
    private ArrayList<ScoreInfo> l;
    private Context c;

    public MyCustomAdapter(ArrayList<ScoreInfo> l, Context c) {
        this.l = l;
        this.c = c;
    }

    @Override
    public int getCount() {
        return l.size();
    }

    @Override
    public Object getItem(int position) {
        return l.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // first let us retrieve the item at the specified position
        ScoreInfo currentItem = null;
        final ViewHolder vh;
        currentItem = (ScoreInfo) getItem(position);
        // now we build a view

        View rowView = null;
        if (convertView == null)
        {
            final LayoutInflater layoutInflater = LayoutInflater.from(c);
            rowView = layoutInflater.inflate(
                    R.layout.scoreinfo,
                    parent,
                    false
            );
            vh = new ViewHolder(rowView);
            rowView.setTag(vh);
        }
        else {
            rowView = convertView;
            vh = (ViewHolder) rowView.getTag();
        }

        vh.datetimeTextview.setText(currentItem.getDate_time());
        vh.playerTextview.setText(currentItem.getPlayer());
        vh.gameTextview.setText(currentItem.getGame());
        if (currentItem.isWin()) {
            Drawable d = Resources.getSystem().getDrawable(android.R.drawable.checkbox_on_background);
            vh.winImageview.setImageDrawable(d);
        }
        else {
            Drawable d = Resources.getSystem().getDrawable(android.R.drawable.checkbox_off_background);
            vh.winImageview.setImageDrawable(d);
        }
        return rowView;

    }
}
