package epita.gamebox;

import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by Jérémy on 11/06/2016.
 */
public class HangmanFragment extends Fragment implements View.OnClickListener {

    private Button[] alphabet;
    int alphalen;

    Button startHang;
    Button resetHang;

    TextView wordDisplay;
    TextView fails;
    int nbGuesses = 0;

    char[] hiddenWord;
    char[] currentWord;

    public HangmanFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View view = inflater.inflate(R.layout.hangmanfragment, container, false);

        // Get buttons
        startHang = (Button) view.findViewById(R.id.hangStart);
        resetHang = (Button) view.findViewById(R.id.hangReset);
        startHang.setOnClickListener(this);
        resetHang.setOnClickListener(this);

        // Get textview
        wordDisplay = (TextView) view.findViewById(R.id.WordDisplayed);
        fails = (TextView) view.findViewById(R.id.nbFails);

        // Get grid
        GridLayout grid = (GridLayout) view.findViewById(R.id.LetterGrid);
        alphalen = grid.getChildCount();
        alphabet = new Button[alphalen];
        for (int k = 0; k < alphalen; ++k){
            alphabet[k] = (Button) grid.getChildAt(k);
            alphabet[k].setOnClickListener(this);
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

    public void startGame() {
        // Game logic
        for (int k = 0; k < alphalen; ++k)
                alphabet[k].setEnabled(true);
        resetHang.setEnabled(true);
        startHang.setEnabled(false);

        // Pick a word
        hiddenWord = randomWord();

        nbGuesses = hiddenWord.length;
        fails.setTextColor(Color.GREEN);
        fails.setText(String.format("%d", nbGuesses));

        currentWord = new char[hiddenWord.length];
        for (int i = 0; i < hiddenWord.length; ++i)
            currentWord[i] = '_';
        wordDisplay.setText(Arrays.toString(currentWord));
        Toast.makeText(getContext(), "Game started. Good luck !", Toast.LENGTH_SHORT).show();
    }

    public void incFails() {
        nbGuesses--;
        if (nbGuesses == 0)
            Toast.makeText(getContext(), "YOU LOOSE", Toast.LENGTH_SHORT).show();
        else {
            if (nbGuesses <= 3)
                fails.setTextColor(Color.RED);
            else if (nbGuesses <= 5)
                fails.setTextColor(Color.BLACK);
            fails.setText(String.format("%d", nbGuesses));
        }
    }

    public boolean isWon() {
        for (int i = 0; i < currentWord.length; ++i)
            if (currentWord[i] == '_')
                return false;
        return true;
    }

    public void lookup(char letter) {
        boolean found = false;
        for (int i = 0; i < hiddenWord.length; ++i)
            if (hiddenWord[i] == letter) {
                currentWord[i] = letter;
                found = true;
            }

        wordDisplay.setText(Arrays.toString(currentWord));
        if (!found)
            incFails();
        else if (isWon())
            Toast.makeText(getContext(), "YOU WON", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v == startHang) {
            startGame();
        }
        else if (v == resetHang) {
            Toast.makeText(getContext(), "Resetting...", Toast.LENGTH_SHORT).show();
            startGame();
        }
        else
            for (int k = 0; k < alphalen; ++k)
                    if (v == alphabet[k]) {
                        char letter = alphabet[k].getText().charAt(0);
                        Toast.makeText(getContext(), "letter " + letter + " choosen",
                            Toast.LENGTH_SHORT).show();

                        lookup(letter);

                        alphabet[k].setEnabled(false);
                    }
    }

    public char[] randomWord() {
        return "ETOILE".toCharArray();
    }

    public interface hangmanAction {
        void return_menu();
    }
}
