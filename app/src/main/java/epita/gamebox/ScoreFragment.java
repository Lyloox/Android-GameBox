package epita.gamebox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jérémy on 11/06/2016.
 */
public class ScoreFragment extends Fragment {

    private ListView listView;
    private TextView emptylist;
    private ArrayList<ScoreInfo> arr;
    private Boolean flag = true;
    private Bundle ban = null;

    public ScoreFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View view = inflater.inflate(R.layout.scorefragment, container, false);
        listView = (ListView) view.findViewById(R.id.activity_main_list);
        emptylist = (TextView) view.findViewById(R.id.empty);
        if (flag) {
            arr = new ArrayList<>();
            flag = false;
        }
        Bundle bun = getArguments();
        if ((ban != null && !ban.equals(bun)) || (bun != null && ban == null)) {
            String datetime = bun.getString("datetime");
            String player = bun.getString("player");
            String game = bun.getString("game");
            Boolean win = bun.getBoolean("win");
            ScoreInfo si = new ScoreInfo(datetime, player, game, win);
            arr.add(si);
        }
        ban = bun;

        MyCustomAdapter mca = new MyCustomAdapter(arr, getContext());
        if (arr.isEmpty())
            listView.setEmptyView(emptylist);
        else
            listView.setAdapter(mca);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu:
                scoreAction actionfrag = (ScoreFragment.scoreAction) getActivity();
                actionfrag.return_menu();
                break;
            default:
                break;
        }
        return true;
    }

    public interface scoreAction {
        void return_menu();
    }
}
