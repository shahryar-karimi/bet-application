package database;

import model.Player;

import java.sql.*;
import java.util.ArrayList;

public class DBManager {
    private Connection connection;
    private static DBManager instance;

    private DBManager() {
        String url = "jdbc:sqlite:bet-data.db";
        try {
            this.connection = DriverManager.getConnection(url);
        } catch (SQLException sqlException) {
            System.err.println("Failed to connect to database");
        }
    }

    public static DBManager getInstance() {
        if (instance == null) instance = new DBManager();
        return instance;
    }

    public ArrayList<Player> loadPlayer() {
        ArrayList<Player> players = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM \"players\";";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("Name");
                double cash = rs.getDouble("Cash");
                players.add(new Player(name, cash));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return players;
    }

    public void savePlayers(ArrayList<Player> players) {
        try {
            Statement statement = connection.createStatement();
            String deleteQuery = "DELETE FROM players";
            statement.executeUpdate(deleteQuery);
            String query = "INSERT INTO players (Name, Cash) VALUES ('%s', %f);";
            for (Player player : players) {
                statement.executeUpdate(String.format(query, player.getName(), player.getCash()));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
