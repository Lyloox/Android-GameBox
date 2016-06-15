package epita.gamebox;

/**
 * Created by Jérémy on 12/06/2016.
 */
public class ScoreInfo {

    private String date_time;
    private String player;
    private String game;
    private boolean win;

    public ScoreInfo(String date_time, String name, String game, boolean win) {
        this.date_time = date_time;
        this.player = name;
        this.game = game;
        this.win = win;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getGame() {
        return game;
    }

    public String getPlayer() {
        return player;
    }

    public boolean isWin() {
        return win;
    }
}
