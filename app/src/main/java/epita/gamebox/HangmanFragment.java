package epita.gamebox;

import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

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
    TextView infoView;
    int nbGuesses = 0;

    char[] hiddenWord;
    char[] currentWord;

    String[] wordList = {"SUN", "STAR", "SKY", "WATER", "STYX", "BUFFALO", "CAT", "DOG", "HORSE",
            "ABSTRACTION", "AMBIGUOUS", "ARITHMETIC", "BACKSLASH", "CODE", "BITMAP", "BYTECODE",
            "CIRCUMSTANCE", "COMBINATION", "CONSEQUENTLY", "THEREFORE", "CONSORTIUM",
            "DECREMENTING", "DEPENDENCY", "DYNAMIC", "DISAMBIGUATE", "EQUIVALENT", "EXPRESSION",
            "FACILITATE", "HEXADECIMAL", "BINARY", "FOLDER", "FILES", "FRIENDS", "FRAGMENT",
            "ANDROID", "IMPLEMENTATION", "INHERITANCE", "INTERNET", "MICROPROCESSOR", "JAVA",
            "NAVIGATION", "OPTIMIZATION", "PARAMETER", "FUNCTION", "LAMBDA", "CALCULUS", "PICKLE",
            "POLYMORPHIC", "SPECIFICATION", "DOCUMENTATION", "STRUCTURE", "LEXICAL", "LIKEWISE",
            "OTHERWISE", "MANIPULATE", "MATHEMATICS", "VERTEX", "HOTJAVA", "PRIMITIVES",
            "TRADITIONAL", "LOVE"
    };

    private AlertDialog.Builder popupBuilder;
    private AlertDialog popupLoose;
    private AlertDialog popupWin;
    private DialogInterface.OnClickListener alertListener;

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
        infoView = (TextView) view.findViewById(R.id.infoTextView);

        // Get grid
        GridLayout grid = (GridLayout) view.findViewById(R.id.LetterGrid);
        alphalen = grid.getChildCount();
        alphabet = new Button[alphalen];
        for (int k = 0; k < alphalen; ++k){
            alphabet[k] = (Button) grid.getChildAt(k);
            alphabet[k].setOnClickListener(this);
        }

        // Handle popups
        popupBuilder = new AlertDialog.Builder(getContext());
        popupLoose = popupBuilder.create();
        popupLoose.setTitle("Result");
        popupLoose.setMessage("You lose");
        popupWin = popupBuilder.create();
        popupWin.setTitle("Result");
        popupWin.setMessage("You win");

        final TextView game = (TextView) view.findViewById(R.id.title_game);
        alertListener = new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int whichButton) {
                        if (dialogInterface.equals(popupLoose)) {
                            if (whichButton == DialogInterface.BUTTON_NEUTRAL) {
                                popupLoose.hide();
                                sendScore transit = (sendScore) getActivity();
                                transit.sendLose(game.getText().toString());
                            }
                        }
                        if (dialogInterface.equals(popupWin)) {
                            if (whichButton == DialogInterface.BUTTON_NEUTRAL) {
                                popupWin.hide();
                                sendScore transit = (sendScore) getActivity();
                                transit.sendWin(game.getText().toString());
                            }
                        }
                    }
                };

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
        infoView.setText(getString(R.string.fails));

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

    public void resetGame() {
        for (int k = 0; k < alphalen; ++k)
            alphabet[k].setEnabled(false);
        resetHang.setEnabled(false);
        startHang.setEnabled(true);
        hiddenWord = null;
        currentWord = null;
        fails.setText("");
    }

    public void incFails() {
        nbGuesses--;
        if (nbGuesses == 0) {
            //TODO : Send the loose
            Toast.makeText(getContext(), "YOU LOOSE", Toast.LENGTH_SHORT).show();
            popupLoose.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", alertListener);
            popupLoose.show();
            infoView.setText(new StringBuilder().append("The word was ").append(hiddenWord)
                    .append(". Try again?").toString());
            resetGame();
        }
        else {
            if (nbGuesses < 3)
                fails.setTextColor(Color.RED);
            else if (nbGuesses < 6)
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
        else if (isWon()) {
            // TODO : Send the win
            Toast.makeText(getContext(), "YOU WON", Toast.LENGTH_SHORT).show();
            popupWin.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", alertListener);
            popupWin.show();
            infoView.setText(R.string.congrats);
            resetGame();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == startHang)
            startGame();
        else if (v == resetHang)
            resetGame();
        else
            for (int k = 0; k < alphalen; ++k)
                    if (v == alphabet[k]) {
                        char letter = alphabet[k].getText().charAt(0);
                        lookup(letter);
                        alphabet[k].setEnabled(false);
                    }
    }

    public char[] randomWord() {
        Random rnd = new Random();
        int choosen = rnd.nextInt(wordList.length);
        return wordList[choosen].toCharArray();
    }

    public interface hangmanAction {
        void return_menu();
    }

    public interface sendScore {
        void sendWin(String game);
        void sendLose(String game);
    }
}
