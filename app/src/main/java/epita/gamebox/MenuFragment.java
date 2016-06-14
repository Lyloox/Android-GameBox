package epita.gamebox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Jérémy on 11/06/2016.
 */
public class MenuFragment extends Fragment {
    Button scores;
    Button tic;
    Button hang;
    Button mines;

    public MenuFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.menufragment, container, false);
        scores = (Button) view.findViewById(R.id.scores);
        tic = (Button) view.findViewById(R.id.tic);
        hang = (Button) view.findViewById(R.id.hang);
        mines = (Button) view.findViewById(R.id.mines);
        ImageButton save = (ImageButton) view.findViewById(R.id.save);

        scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuAction actionfrag = (MenuFragment.menuAction) getActivity();
                actionfrag.score();
            }
        });

        tic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuAction actionfrag = (MenuFragment.menuAction) getActivity();
                actionfrag.tictactoe();
            }
        });

        hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuAction actionfrag = (MenuFragment.menuAction) getActivity();
                actionfrag.hangman();
            }
        });

        mines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuAction actionfrag = (MenuFragment.menuAction) getActivity();
                actionfrag.minesweeper();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuAction actionfrag = (MenuFragment.menuAction) getActivity();
                actionfrag.save();
            }
        });

        return view;
    }

    public interface menuAction {
        void save();
        void score();
        void tictactoe();
        void hangman();
        void minesweeper();
    }

}
