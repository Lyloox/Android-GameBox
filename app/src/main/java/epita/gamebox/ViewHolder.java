package epita.gamebox;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jérémy on 12/06/2016.
 */
public class ViewHolder {

    final TextView datetimeTextview;
    final TextView playerTextview;
    final TextView gameTextview;
    final ImageView winImageview;

    public ViewHolder(View rowView)
    {
        datetimeTextview =
                (TextView) rowView.findViewById(R.id.date_time);
        playerTextview =
                (TextView) rowView.findViewById(R.id.player);
        gameTextview =
                (TextView) rowView.findViewById(R.id.game);
        winImageview =
                (ImageView) rowView.findViewById(R.id.win);
    }
}
