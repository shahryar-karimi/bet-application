package model;

import model.bets.Bet;
import model.bets.ListBet;
import model.bets.NormalBet;
import model.bets.RangeBet;

import java.util.LinkedList;
import java.util.Random;

public class Game {
    private final Player player;
    private Random random;
    private LinkedList<Bet> bets;

    public Game(Player player) {
        this.bets = new LinkedList<>();
        this.player = player;
        random = new Random();
    }

    public static boolean canStart(Player player) {
        return player.getCash() > 0;
    }

    public String roll() {
        if (bets.isEmpty()) {
            return "There is nothing to roll plz add a bet.";
        }
        String msg = "";
        while (!bets.isEmpty()) {
            Bet bet = bets.pop();
            if (rollEach(bet) == 0) {
                msg += addMsg("lost", bet);
            } else {
                msg += addMsg("won", bet);
            }
        }
        return msg;
    }

    private String addMsg(String result, Bet bet) {
        return "You " + result + " this : " + bet + "\nYour cash is : " + player.getCash() + "\n";
    }

    private double rollEach(Bet bet) {
        double prize;
        if (bet instanceof ListBet) {
            ListBet listBet = (ListBet) bet;
            int size = listBet.getSize();
            int[] results = new int[size];
            for (int i = 0; i < size; i++) {
                results[i] = rollOnce();
            }
            prize = listBet.getPrize(results);
        } else {
            int[] results = new int[1];
            results[0] = rollOnce();
            prize = bet.getPrize(results);
        }
        player.setCash(player.getCash() + prize);
        return prize;
    }

    private int rollOnce() {
        return random.nextInt(Constant.RANDOM_SIZE) + 1;
    }

    public NormalBet createNormalBet(double cost, int guess, LinkedList<Bet> bets, boolean isForList) {
        if (player.getCash() >= cost) {
            player.setCash(player.getCash() - cost);
            NormalBet normalBet = new NormalBet(cost, guess, isForList);
            bets.add(normalBet);
            return normalBet;
        }
        return null;
    }

    public RangeBet createRangeBet(double cost, int start, int end, LinkedList<Bet> bets, boolean isForList) {
        if (player.getCash() >= cost) {
            player.setCash(player.getCash() - cost);
            RangeBet rangeBet = new RangeBet(cost, start, end, isForList);
            bets.add(rangeBet);
            return rangeBet;
        }
        return null;
    }

    public ListBet createListBet(double cost, LinkedList<Bet> bets) {
        if (player.getCash() >= cost) {
            player.setCash(player.getCash() - cost);
            ListBet listBet = new ListBet(cost, bets);
            this.bets.add(listBet);
            return listBet;
        }
        return null;
    }

    public boolean isValidCost(double cost) {
        return player.getCash() >= cost && cost > 0;
    }

    public String mainMenu() {
        return "1.roll\n" +
                "2.add bet\n" +
                "3.log out";
    }

    public String betsMenu() {
        return "1.normal\n" +
                "2.range\n" +
                "3.list\n" +
                "4.back";
    }

    public String listMenu() {
        return "1.normal\n" +
                "2.range\n" +
                "3.done\n" +
                "4.cancel";
    }

    public LinkedList<Bet> getBets() {
        return bets;
    }

    public Player getPlayer() {
        return player;
    }
}
