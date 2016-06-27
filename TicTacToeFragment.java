package epita.gamebox;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jérémy on 11/06/2016.
 */
public class TicTacToeFragment extends Fragment implements View.OnClickListener{

    Button reset;
    private Button[] b;
    int size;
    private int player = 1;

    public static int registered = 1;
    public static int guest = 2;
    private TextView text;
    private AlertDialog.Builder builderWon;
    private AlertDialog alertWin;
    private AlertDialog alertLose;
    private DialogInterface.OnClickListener listener;


    public TicTacToeFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View view = inflater.inflate(R.layout.tictactoefragment, container, false);
        reset = (Button) view.findViewById(R.id.resetTTT);
        text = (TextView) view.findViewById(R.id.playerName);
        reset.setOnClickListener(this);

        builderWon = new AlertDialog.Builder(getContext());
        alertWin = builderWon.create();
        alertWin.setTitle("Result");
        alertWin.setMessage("Player X win");

        alertLose = builderWon.create();
        alertLose.setTitle("Result");
        alertLose.setMessage("Player O win");

        final TextView game = (TextView) view.findViewById(R.id.title_game);
        listener = new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int whichButton) {
                        if (dialogInterface.equals(alertLose)) {
                            if (whichButton == DialogInterface.BUTTON_NEUTRAL) {
                                alertLose.hide();
                                sendScore transit = (TicTacToeFragment.sendScore) getActivity();
                                transit.sendLose(game.getText().toString());
                            }
                        }
                        if (dialogInterface.equals(alertWin)) {
                            if (whichButton == DialogInterface.BUTTON_NEUTRAL) {
                                alertWin.hide();
                                sendScore transit = (TicTacToeFragment.sendScore) getActivity();
                                transit.sendWin(game.getText().toString());
                            }
                        }
                    }
                };

        GridLayout t = (GridLayout) view.findViewById(R.id.gridGame);
        size = t.getChildCount();
        b = new Button[size];

        for( int i = 0; i < size; i++ ){
            b[i] = (Button) t.getChildAt(i);
            b[i].setOnClickListener(this);
        }

        startGame();

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

        if (v == reset)
            startGame();

        for (int i = 0; i < size; i++) {
            if (v == b[i]) {
                markBox(b[i]);
                b[i].setEnabled(false);
            }
        }

        int res = gameWon();

        if (res != -1) {
            if (res == 0) {
                b[0].setText("x win");
                alertWin.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", listener);
                alertWin.show();
            }
            if (res == 1) {
                alertLose.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", listener);
                alertLose.show();
                startGame();
            }
        }
        else
            changeTurn();
    }

    public void markBox(Button b)
    {
        if (player == 1)
        {
            b.setText(R.string.registered);
            b.setTag("1");
        }
        else
        {
            b.setText(R.string.guest);
            b.setTag("2");
        }
    }

    public void changeTurn() {
        if (player == registered) {
            player = guest;
            text.setText(R.string.guest);
        } else {
            player = registered;
            text.setText(R.string.registered);
        }
    }

    public int gameWon()
    {

        if (b[0].getText().toString().equals(b[1].getText().toString())
                && b[0].getText().toString().equals(b[2].getText().toString()))
        {
            if (b[0].getTag().toString().equals("1"))
                return 0;
            else if (b[0].getTag().toString().equals("2"))
                return 1;
            else
                return -1;

        }
        else if (b[0].getText().toString().equals(b[4].getText().toString())
                && b[0].getText().toString().equals(b[8].getText().toString()))
        {
            if (b[0].getTag().toString().equals("1"))
                return 0;
            else if (b[0].getTag().toString().equals("2"))
                return 1;
            else
                return -1;

        }

        else if (b[0].getText().toString().equals(b[3].getText().toString())
                && b[0].getText().toString().equals(b[6].getText().toString()))
        {
            if (b[0].getTag().toString().equals("1"))
                return 0;
            else if (b[0].getTag().toString().equals("2"))
                return 1;
            else
                return -1;

        }
/*

        else if (b[1].getText().toString().equals(b[4].getText().toString())
                && b[1].getText().toString().equals(b[7].getText().toString()))
        {
            if (b[0].getTag().toString().equals("1"))
                return 0;
            else if (b[0].getTag().toString().equals("2"))
                return 1;
            else
                return -1;

        }

        else if (b[2].getText().toString().equals(b[5].getText().toString())
                && b[2].getText().toString().equals(b[8].getText().toString()))
        {
            if (b[0].getTag().toString().equals("1"))
                return 0;
            else if (b[0].getTag().toString().equals("2"))
                return 1;
            else
                return -1;

        }

        else if (b[2].getText().toString().equals(b[4].getText().toString())
                && b[2].getText().toString().equals(b[6].getText().toString()))
        {
            if (b[0].getTag().toString().equals("1"))
                return 0;
            else if (b[0].getTag().toString().equals("2"))
                return 1;
            else
                return -1;

        }

        else if (b[3].getText().toString().equals(b[4].getText().toString())
                && b[3].getText().toString().equals(b[5].getText().toString()))
        {
            if (b[0].getTag().toString().equals("1"))
                return 0;
            else if (b[0].getTag().toString().equals("2"))
                return 1;
            else
                return -1;

        }

        else if (b[6].getText().toString().equals(b[7].getText().toString())
                && b[6].getText().toString().equals(b[8].getText().toString()))
        {
            if (b[0].getTag().toString().equals("1"))
                return 0;
            else if (b[0].getTag().toString().equals("2"))
                return 1;
            else
                return -1;

        }*/
else
        return -1;
    }

    public void startGame() {

        int first = new Random().nextInt(2);
        if(first == 0) {
            player = registered;
            text.setText(R.string.registered);
        }
        else {
            player = guest;
            text.setText(R.string.guest);
        }
        for (int i = 0; i < size; i++) {
                b[i].setEnabled(true);
                b[i].setText(R.string.emptyButton);
        }
    }

    public interface sendScore {
        void sendWin(String game);
        void sendLose(String game);
    }
}
