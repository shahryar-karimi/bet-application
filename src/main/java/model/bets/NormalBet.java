package model.bets;

import model.Constant;

public class NormalBet extends Bet {
    private int guess;
    private boolean isForList;

    public NormalBet(double cost, int guess, boolean isForList) {
        super(cost);
        this.guess = guess;
        this.isForList = isForList;
    }

    public int getGuess() {
        return guess;
    }

    public boolean isForList() {
        return isForList;
    }

    @Override
    protected double prizeRatio(int[] numbers) {
        if (checkForWin(numbers))
            return Constant.RANDOM_SIZE;
        return 0;
    }

    @Override
    protected boolean checkForWin(int[] numbers) {
        return guess == numbers[0];
    }

    @Override
    public String toString() {
        if (isForList) return "NormalBet{" +
                    "guess=" + guess +
                    '}';
        return "NormalBet{" +
                "cost=" + cost +
                ", guess=" + guess +
                '}';
    }
}
