package model.bets;

public abstract class Bet {
    protected double cost;

    public Bet(double cost) {
        this.cost = cost;
    }

    protected abstract double prizeRatio(int[] numbers);

    public double getPrize(int[] numbers) {
        return cost * prizeRatio(numbers);
    }

    protected abstract boolean checkForWin(int[] numbers);
}
