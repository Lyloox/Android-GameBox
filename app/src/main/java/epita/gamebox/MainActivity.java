package epita.gamebox;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements MenuFragment.menuAction, ScoreFragment.scoreAction,
                                                                MinesweeperFragment.sendScore {

    MenuFragment menuFragment;
    ScoreFragment scoreFragment;
    String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_action);

        menuFragment = new MenuFragment();
        scoreFragment = new ScoreFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, menuFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void save() {
        Toast toast = null;
        EditText name = (EditText) findViewById(R.id.edit);
        str = name.getText().toString();
        if (str.length() < 1)
            toast = Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT);
        else {
            toast = Toast.makeText(this, "Your name is saved", Toast.LENGTH_SHORT);
            menuFragment.hang.setEnabled(true);
            menuFragment.mines.setEnabled(true);
            menuFragment.scores.setEnabled(true);
            menuFragment.tic.setEnabled(true);
        }
        toast.show();
    }

    @Override
    public void score() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, scoreFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void tictactoe() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, new TicTacToeFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void hangman() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, new HangmanFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void minesweeper() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, new MinesweeperFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void return_menu() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, menuFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void sendWin(String game) {
        Bundle bundle = new Bundle();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM\nHH:mm");
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
        bundle.putString("datetime", currentDateandTime);
        bundle.putString("player", str);
        bundle.putString("game", game);
        bundle.putBoolean("win", true);
        scoreFragment.setArguments(bundle);
    }

    @Override
    public void sendLose(String game) {
        Bundle bundle = new Bundle();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM\nHH:mm:ss");
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
        bundle.putString("datetime", currentDateandTime);
        bundle.putString("player", str);
        bundle.putString("game", game);
        bundle.putBoolean("win", false);
        scoreFragment.setArguments(bundle);
    }
}
