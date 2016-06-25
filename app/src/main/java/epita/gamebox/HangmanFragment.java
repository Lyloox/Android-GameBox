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
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

/**
 * Created by Jérémy on 11/06/2016.
 */
public class HangmanFragment extends Fragment implements View.OnClickListener {

    private Button[][] alphabet;

    public HangmanFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View view = inflater.inflate(R.layout.hangmanfragment, container, false);

        // Get elements
        GridLayout grid = (GridLayout) view.findViewById(R.id.LetterGrid);
        alphabet = new Button[5][5];
        int i = 0;
        int j = 0;
        for (int k = 0; k < grid.getChildCount(); ++k){
            if (k % 5 == 0 && k != 0) {
                ++i;
                j = 0;
            }
            alphabet[i][j] = (Button) grid.getChildAt(k);
            alphabet[i][j].setOnClickListener(this);
            ++j;
        }

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
                ScoreFragment.scoreAction actionfrag = (ScoreFragment.scoreAction) getActivity();
                actionfrag.return_menu();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < 5; ++i)
            for (int j = 0; j < 5; ++j)
                if (v == alphabet[i][j]) {
                    Toast.makeText(getContext(), "letter " + alphabet[i][j].getText() + " choosen",
                            Toast.LENGTH_SHORT).show();
                    alphabet[i][j].setEnabled(false);
                }
    }

    public interface hangmanAction {
        void return_menu();
    }
}
