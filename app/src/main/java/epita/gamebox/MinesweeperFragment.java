package epita.gamebox;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
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
public class MinesweeperFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener{

    private Button[][] block;
    private Integer[][] mine;
    private TextView flag;
    private int left = 10;
    private Button reset;
    private AlertDialog.Builder popupBuilder;
    private AlertDialog popup;
    private AlertDialog popup2;
    private DialogInterface.OnClickListener alertListener;

    public MinesweeperFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //send score
        // if F don't clique
        setHasOptionsMenu(true);
        final View view = inflater.inflate(R.layout.minesweeperfragment, container, false);
        GridLayout grid = (GridLayout) view.findViewById(R.id.grid);
        final TextView game = (TextView) view.findViewById(R.id.title_game);
        flag = (TextView) view.findViewById(R.id.left);
        reset = (Button) view.findViewById(R.id.reset);
        reset.setOnClickListener(this);
        popupBuilder = new AlertDialog.Builder(getContext());
        flag.setText("10");
        block = new Button[8][8];
        int i = 0;
        int j = 0;
        for (int k = 0; k < grid.getChildCount(); ++k){
            if (k % 8 == 0 && k != 0) {
                ++i;
                j = 0;
            }
            block[i][j] = (Button) grid.getChildAt(k);
            block[i][j].setOnClickListener(this);
            block[i][j].setOnLongClickListener(this);
            block[i][j].setTextSize(30);
            ++j;
        }
        mine = new Integer[8][8];
        for (int a = 0; a < 8; ++a)
            for (int b = 0; b < 8; ++b)
                mine[a][b] = 0;
        Random r = new Random();
        ArrayList<Point> map = new ArrayList<>();
        for (int a = 0; a < 10; ++a){
            int x = r.nextInt(8);
            int y = r.nextInt(8);
            while (map.contains(new Point(x, y))) {
                x = r.nextInt(8);
                y = r.nextInt(8);
            }
            mine[x][y] = 1;
            map.add(new Point(x, y));
        }

        popup = popupBuilder.create();
        popup.setTitle("Result");
        popup.setMessage("You lose");
        popup2 = popupBuilder.create();
        popup2.setTitle("Result");
        popup2.setMessage("You win");
        alertListener = new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int whichButton) {
                        if (dialogInterface.equals(popup)) {
                            if (whichButton == DialogInterface.BUTTON_NEUTRAL) {
                                popup.hide();
                                sendScore transit = (MinesweeperFragment.sendScore) getActivity();
                                transit.sendLose(game.getText().toString());
                            }
                        }
                        if (dialogInterface.equals(popup2)) {
                            if (whichButton == DialogInterface.BUTTON_NEUTRAL) {
                                popup2.hide();
                                sendScore transit = (MinesweeperFragment.sendScore) getActivity();
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


    @Override
    public void onClick(View v) {
        if (v == reset){
            for (int i = 0; i < 8; ++i)
                for (int j = 0; j < 8; ++j) {
                    Drawable d = getResources().getDrawable(R.drawable.my_button);
                    block[i][j].setBackground(d);
                    block[i][j].setText("");
                    block[i][j].setEnabled(true);
                }
            left = 10;
            flag.setText(String.valueOf(left));
            mine = new Integer[8][8];
            for (int a = 0; a < 8; ++a)
                for (int b = 0; b < 8; ++b)
                    mine[a][b] = 0;
            Random r = new Random();
            ArrayList<Point> map = new ArrayList<>();
            for (int a = 0; a < 10; ++a){
                int x = r.nextInt(8);
                int y = r.nextInt(8);
                while (map.contains(new Point(x, y))) {
                    x = r.nextInt(8);
                    y = r.nextInt(8);
                }
                mine[x][y] = 1;
                map.add(new Point(x, y));
            }
        }
        for (int i = 0; i < 8; ++i)
            for (int j = 0; j < 8; ++j)
                if (v == block[i][j]) {
                    int num = 0;
                    Drawable d = getResources().getDrawable(R.drawable.btn_cliked);
                    block[i][j].setBackground(d);
                    num = testBtn(mine, i, j, num);
                    setNumber(num, block[i][j]);
                    if (num != 0 && mine[i][j] != 1)
                        mine[i][j] = 2;
                    testvictory(mine);
                    //set game over show bombe and all block
                    if (mine[i][j] == 1) {
                        block[i][j].setText("B");
                        block[i][j].setTextColor(Color.BLACK);
                        d = getResources().getDrawable(R.drawable.touched);
                        block[i][j].setBackground(d);
                        showMap(i, j);
                    }
                    if (num == 0 && mine[i][j] != 1)
                        showFreeBlock(block, mine, i, j);
                }
    }

    public void testvictory(Integer[][] mine) {
        for (int i = 0; i < 8; ++i)
            for (int j = 0; j < 8; ++j)
                if (mine[i][j] == 0)
                    return;
        popup2.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", alertListener);
        popup2.show();
    }

    public void showMap(int l, int k) {
        for (int i = 0; i < 8; ++i)
            for (int j = 0; j < 8; ++j) {
                block[i][j].setEnabled(false);
                if (i == l && k == j)
                    continue;
                int num = 0;
                Drawable d = getResources().getDrawable(R.drawable.btn_cliked);
                block[i][j].setBackground(d);
                num = testBtn(mine, i, j, num);
                setNumber(num, block[i][j]);
                if (mine[i][j] == 1) {
                    block[i][j].setText("B");
                    block[i][j].setTextColor(Color.BLACK);
                }
            }
        popup.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", alertListener);
        popup.show();
    }

    public void showFreeBlock(Button[][] block, Integer[][] mine, int i, int j) {
        if (mine[i][j] == 0)
            mine[i][j] = 2;
        int num;
        if (isPresent(i - 1, j - 1)) {
            drawBlock(block, i - 1, j - 1);
            num = testBtn(mine, i - 1, j - 1, 0);
            if (mine[i - 1][j - 1] != 1)
                setNumber(num, block[i - 1][j - 1]);
            if (num != 0)
                mine[i - 1][j - 1] = 2;
            if (num == 0 && mine[i - 1][j - 1] == 0)
                showFreeBlock(block, mine, i - 1, j - 1);
        }
        if (isPresent(i - 1, j)) {
            drawBlock(block, i - 1, j);
            num = testBtn(mine, i - 1, j, 0);
            if (mine[i - 1][j] != 1)
                setNumber(num, block[i - 1][j]);
            if (num != 0)
                mine[i - 1][j] = 2;
            if (num == 0 && mine[i - 1][j] == 0)
                showFreeBlock(block, mine, i - 1, j);
        }
        if (isPresent(i - 1, j + 1)) {
            drawBlock(block, i - 1, j + 1);
            num = testBtn(mine, i - 1, j + 1, 0);
            if (mine[i - 1][j + 1] != 1)
                setNumber(num, block[i - 1][j + 1]);
            if (num != 0)
                mine[i - 1][j + 1] = 2;
            if (num == 0 && mine[i - 1][j + 1] == 0)
                showFreeBlock(block, mine, i - 1, j + 1);
        }
        if (isPresent(i, j - 1)) {
            drawBlock(block, i, j - 1);
            num = testBtn(mine, i, j - 1, 0);
            if (mine[i][j - 1] != 1)
                setNumber(num, block[i][j - 1]);
            if (num != 0)
                mine[i][j - 1] = 2;
            if (num == 0 && mine[i][j - 1] == 0)
                showFreeBlock(block, mine, i, j - 1);
        }
        if (isPresent(i, j + 1)) {
            drawBlock(block, i, j + 1);
            num = testBtn(mine, i, j + 1, 0);
            setNumber(num, block[i][j + 1]);
            if (num != 0)
                mine[i][j + 1] = 2;
            if (num == 0 && mine[i][j + 1] == 0)
                showFreeBlock(block, mine, i, j + 1);
        }
        if (isPresent(i + 1, j - 1)) {
            drawBlock(block, i + 1, j - 1);
            num = testBtn(mine, i + 1, j - 1, 0);
            if (mine[i + 1][j - 1] != 1)
                setNumber(num, block[i + 1][j - 1]);
            if (num != 0)
                mine[i + 1][j - 1] = 2;
            if (num == 0 && mine[i + 1][j - 1] == 0)
                showFreeBlock(block, mine, i + 1, j - 1);
        }
        if (isPresent(i + 1, j)) {
            drawBlock(block, i + 1, j);
            num = testBtn(mine, i + 1, j, 0);
            if (mine[i + 1][j] != 1)
                setNumber(num, block[i + 1][j]);
            if (num != 0)
                mine[i + 1][j] = 2;
            if (num == 0 && mine[i + 1][j] == 0)
                showFreeBlock(block, mine, i + 1, j);
        }
        if (isPresent(i + 1, j + 1)) {
            drawBlock(block, i + 1, j + 1);
            num = testBtn(mine, i + 1, j + 1, 0);
            if (mine[i + 1][j + 1] != 1)
                setNumber(num, block[i + 1][j + 1]);
            if (num != 0)
                mine[i + 1][j + 1] = 2;
            if (num == 0 && mine[i + 1][j + 1] == 0)
                showFreeBlock(block, mine, i + 1, j + 1);
        }
    }

    public boolean isPresent(int i, int j) {
        if (i < 0 || i > 7)
            return  false;
        if (j < 0 || j > 7)
            return  false;
        return  true;
    }

    public void drawBlock(Button[][] block, int i, int j) {
        Drawable d = getResources().getDrawable(R.drawable.btn_cliked);
        block[i][j].setBackground(d);
    }

    public int testBtn(Integer[][] mine, int i, int j, int num) {
        if (isPresent(i -  1, j - 1))
            if (mine[i - 1][j - 1] == 1)
                ++num;
        if (isPresent(i - 1, j))
            if (mine[i - 1][j] == 1)
                ++num;
        if (isPresent(i - 1, j + 1))
            if (mine[i - 1][j + 1] == 1)
                ++num;
        if (isPresent(i, j - 1))
            if (mine[i][j - 1] == 1)
                ++num;
        if (isPresent(i, j + 1))
            if (mine[i][j + 1] == 1)
                ++num;
        if (isPresent(i + 1, j - 1))
            if (mine[i + 1][j - 1] == 1)
                ++num;
        if (isPresent(i + 1, j))
            if (mine[i + 1][j] == 1)
                ++num;
        if (isPresent(i + 1, j + 1))
            if (mine[i + 1][j + 1] == 1)
                ++num;
        return num;
    }

    public void setNumber(int num, Button btn){
        if (num == 1) {
            btn.setText("1");
            btn.setTextColor(Color.BLUE);
        }
        if (num == 2) {
            btn.setText("2");
            btn.setTextColor(Color.GREEN);
        }
        if (num == 3) {
            btn.setText("3");
            btn.setTextColor(Color.RED);
        }
        if (num == 4) {
            btn.setText("4");
            btn.setTextColor(Color.YELLOW);
        }
        if (num == 5) {
            btn.setText("5");
            btn.setTextColor(Color.MAGENTA);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        for (int i = 0; i < 8; ++i)
            for (int j = 0; j < 8; ++j){
                if (mine[i][j] != 2)
                if (left >= 0 && left <= 10)
                if (v == block[i][j]) {
                    if (block[i][j].getText() == "" && left != 0) {
                        block[i][j].setText("F");
                        block[i][j].setTextColor(Color.RED);
                        --left;
                        flag.setText(String.valueOf(left));
                    }
                    else if (block[i][j].getText() == "F"){
                        block[i][j].setText("");
                        ++left;
                        flag.setText(String.valueOf(left));
                    }
                }
            }
        return true;
    }

    public interface sendScore {
        void sendWin(String game);
        void sendLose(String game);
    }
}
