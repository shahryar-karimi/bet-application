package model.bets;

import java.util.LinkedList;

public class ListBet extends Bet {
    private LinkedList<Bet> bets;

    public ListBet(double cost, LinkedList<Bet> bets) {
        super(cost);
        this.bets = bets;
    }

    public int getSize() {
        return bets.size();
    }

    @Override
    protected double prizeRatio(int[] numbers) {
        double result = 1;
        for (int i = 0; i < numbers.length; i++) {
            Bet bet = bets.get(i);
            result *= bet.prizeRatio(new int[]{numbers[i]});
            if (result == 0) return 0;
        }
        return result;
    }

    @Override
    protected boolean checkForWin(int[] numbers) {
        for (int i = 0; i < numbers.length; i++)
            if (!bets.get(i).checkForWin(new int[]{numbers[i]}))
                return false;
        return true;
    }

    @Override
    public String toString() {
        return "ListBet{" +
                "cost=" + cost +
                ", bets=" + bets +
                '}';
    }

    public LinkedList<Bet> getBets() {
        return bets;
    }
}
