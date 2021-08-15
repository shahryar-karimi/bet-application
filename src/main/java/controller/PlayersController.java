package controller;

import database.DBManager;
import model.Player;

import java.util.ArrayList;

public class PlayersController {
    private ArrayList<Player> players;
    private DBManager dbManager;

    public PlayersController(ArrayList<Player> players, DBManager dbManager) {
        this.players = players;
        this.dbManager = dbManager;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public Player getPlayer(String name) {
        for (Player player : players)
            if (player.getName().equals(name))
                return player;
        return null;
    }

    public void save() {
        dbManager.savePlayers(players);
    }
}
