import controller.PlayersController;
import database.DBManager;
import model.Player;
import view.CLI;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Player> players = DBManager.getInstance().loadPlayer();
        for (Player player : players)
            System.out.println(player);
        PlayersController playersController = new PlayersController(players, DBManager.getInstance());
        CLI cli = new CLI(playersController);
        cli.start();
        playersController.save();
        DBManager.getInstance().close();
    }
}
